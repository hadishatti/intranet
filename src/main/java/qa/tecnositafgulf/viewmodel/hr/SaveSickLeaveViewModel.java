package qa.tecnositafgulf.viewmodel.hr;

import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.sys.PageCtrl;
import org.zkoss.zul.Messagebox;
import qa.tecnositafgulf.config.MyProperties;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.leaves.LeaveBalance;
import qa.tecnositafgulf.model.leaves.LeaveRequest;
import qa.tecnositafgulf.service.AdministrationService;
import qa.tecnositafgulf.service.LeaveRequestService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static qa.tecnositafgulf.config.LeaveRequestStates.SickRegistered;

/**
 * Created by hadi on 1/4/18.
 */
public class SaveSickLeaveViewModel extends IntranetVM {

    private LeaveRequest request;
    private LeaveRequestService service;
    private AdministrationService adminService;
    private List<Employee> employeesDB;
    private List<String> employees;
    private String selectedEmployee;
    private boolean modify=false;
    private Date leaveFrom;
    private Date leaveTo;
    private final String path= MyProperties.getInstance().getProperty("certificate");
    private boolean imageChanged;
    private String message;
    private String fileName;
    private Media certificate;

    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("sickToModify") LeaveRequest sickToModify){
        init();
        Employee employee = (Employee) Sessions.getCurrent().getAttribute("employee");
        if(!super.isAdministrator() && !authenticationService.isProfilePresent(employee, "HRLM")){
            Executions.sendRedirect("/pages/home.zul");
            return;
        }
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        service = context.getBean(LeaveRequestService.class);
        adminService = context.getBean(AdministrationService.class);
        employees = new ArrayList<String>();
        employeesDB = adminService.listEmployeesByHRManager(employee);
        for (Employee e : employeesDB) {
            employees.add(e.toString());
        }
        if(sickToModify!=null){
            modify = true;
            request = sickToModify;
            selectedEmployee = request.getApplicant().toString();
            employees = new ArrayList<String>();
            employees.add(selectedEmployee);
            leaveFrom = request.getLeaveFrom();
            leaveTo = request.getLeaveTo();
            message = request.getMessage();
        }else {
            request = new LeaveRequest();
            request.setApprover(employee);
            request.setCreatedOn(new Date());
            request.setStatus(SickRegistered);
            selectedEmployee = employees.get(0);
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, 8);
            c.set(Calendar.MINUTE, 30);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            leaveFrom = c.getTime();
            c.add(Calendar.HOUR,9);
            leaveTo = c.getTime();
        }
        imageChanged=false;

        addCommonTags((PageCtrl) view.getPage());
    }

    public String checkNumber(String number, int i){
        if(service.findByNumber(number).size()==0)
            return number;
        String[] numbers = number.split("_");
        return checkNumber(numbers[0]+"_"+i, ++i);
    }

    @Command
    public void save(){
        Employee emp;
        if(!modify) {
            int e = employees.indexOf(selectedEmployee);
            emp = employeesDB.get(e);
        }else{
            emp = request.getApplicant();
        }
        request.setApplicant(emp);
        if(leaveFrom.after(leaveTo)){
            Messagebox.show("Starting Leave Date cannot be after the Ending Leave Date!", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }else {
            request.setLeaveFrom(leaveFrom);
            request.setLeaveTo(leaveTo);
            String number = "S-";
            number += request.getApplicant().getInitials() + "-";
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmm");
            number += sdf.format(leaveFrom) + "-";
            number += sdf.format(leaveTo);
            request.setNumber(checkNumber(number,1));
            request.setType("Sick");
            request.setTickets(false);
            request.setMessage(message);
            if (request.getApprovedOn() == null)
                request.setApprovedOn(new Date());
            final List<LeaveRequest> requests = service.listLastLeaveRequestsByApplicant(request.getApplicant(), request.getLeaveFrom(), request.getLeaveTo());
            if(requests.size()>0){
                Messagebox.show("Warning: There is already a Leave Request overlapping the selected Dates! Please check the status of the overlapping Leave Request before ignoring this warning.", "Warning", Messagebox.ABORT | Messagebox.IGNORE, Messagebox.EXCLAMATION, new EventListener<Event>() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if(event.getName().equals("onAbort")){
                            Executions.sendRedirect("/pages/hr/viewSickLeaveRegistrations.zul");
                        }else if(event.getName().equals("onIgnore")){
                            service.saveLeaveRequest(request);
                            Messagebox.show("Leave Request Registered! The Sick Leave Balance is not changed for the Employee.", "Success", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                                public void onEvent(Event event) throws Exception {
                                    Executions.sendRedirect("/pages/hr/viewSickLeaveRegistrations.zul");
                                }
                            });
                        }
                    }
                });
            }else{
                if (imageChanged) {
                    try {
                        saveCertificate();
                    } catch (IOException e1) {
                        showInfo("Error in saving the certificate!");
                        return;
                    }
                }
                long daysInMillis = leaveTo.getTime() - leaveFrom.getTime();
                int days = (int) TimeUnit.DAYS.convert(daysInMillis, TimeUnit.MILLISECONDS) + 1;
                LeaveBalance bal = new LeaveBalance();
                bal.setEmployee(request.getApplicant());
                bal.setValue(days);
                bal.setType(request.getType());
                service.updateLeaveBalance(bal);
                service.saveLeaveRequest(request);
                Messagebox.show("Leave Request Registered! New Sick Leave Balance For the Employee is " + days + " Days", "Success", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                    public void onEvent(Event event) throws Exception {
                        Executions.sendRedirect("/pages/hr/viewSickLeaveRegistrations.zul");
                    }
                });
            }
        }
    }


    @Command
    @NotifyChange({"certificate", "fileName"})
    public void upload(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) {
        UploadEvent upEvent = null;
        Object objUploadEvent = ctx.getTriggerEvent();
        if (objUploadEvent != null && (objUploadEvent instanceof UploadEvent)) {
            upEvent = (UploadEvent) objUploadEvent;
        }
        if (upEvent != null) {
            Media media = upEvent.getMedia();
            int lengthofImage;
            if(media.isBinary())
                lengthofImage = media.getByteData().length;
            else
                lengthofImage = media.getStringData().length();
            if (lengthofImage > 1500 * 1024) {
                showInfo("Please Select a Image of size less than 1,5 Mb.");
                return;
            }
            else{
                certificate = media;
                SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmm");
                String number = "S-";
                Employee emp;
                if(!modify) {
                    int e = employees.indexOf(selectedEmployee);
                    emp = employeesDB.get(e);
                }else{
                    emp = request.getApplicant();
                }
                number += emp.getInitials();
                number += "-"+sdf.format(leaveFrom) + "-";
                number += sdf.format(leaveTo);
                fileName = number+"__"+emp.getName()+"-"+emp.getFamilyName()+"__"+"Certificate."+media.getFormat();
                imageChanged = true;
            }
        } else {
            showInfo("Certificate Upload Failed.");
        }
    }

    protected void showInfo(String message) {
        Messagebox.show(message, "Error!", Messagebox.OK, Messagebox.INFORMATION);
    }

    public void saveCertificate() throws IOException {
        request.setCertificate(path+fileName);
        if(certificate.isBinary()) {
            OutputStream os = new FileOutputStream(new File(path + fileName));
            InputStream is;
            is = certificate.getStreamData();
            byte[] buffer = new byte[1024];
            for (int count; (count = is.read(buffer)) != -1; ) {
                os.write(buffer, 0, count);
            }
            os.flush();
            os.close();
            is.close();
        }else{
            FileUtils.writeStringToFile(new File(path + fileName), certificate.getStringData());
        }
    }

    public List<String> getEmployees() {
        return employees;
    }

    public void setEmployees(List<String> employees) {
        this.employees = employees;
    }

    public String getSelectedEmployee() {
        return selectedEmployee;
    }

    public void setSelectedEmployee(String selectedEmployee) {
        this.selectedEmployee = selectedEmployee;
    }

    public Date getLeaveFrom() {
        return leaveFrom;
    }

    public void setLeaveFrom(Date leaveFrom) {
        this.leaveFrom = leaveFrom;
    }

    public Date getLeaveTo() {
        return leaveTo;
    }

    public void setLeaveTo(Date leaveTo) {
        this.leaveTo = leaveTo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
