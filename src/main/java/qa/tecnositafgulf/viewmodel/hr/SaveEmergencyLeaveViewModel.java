package qa.tecnositafgulf.viewmodel.hr;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static qa.tecnositafgulf.config.LeaveRequestStates.EmergencyRegistered;

/**
 * Created by hadi on 5/26/18.
 */
public class SaveEmergencyLeaveViewModel extends IntranetVM {

    private LeaveRequest request;
    private LeaveRequestService service;
    private AdministrationService adminService;
    private List<Employee> employeesDB;
    private List<String> employees;
    private String selectedEmployee;
    private boolean modify=false;
    private Date leaveFrom;
    private int minutes;
    private final String path= MyProperties.getInstance().getProperty("certificate");
    private String message;
    private String fileName;

    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("emergencyToModify") LeaveRequest emergencyToModify){
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
        employeesDB = adminService.listEmployees();
        for (Employee e : employeesDB) {
            employees.add(e.toString());
        }
        if(emergencyToModify!=null){
            modify = true;
            request = emergencyToModify;
            selectedEmployee = request.getApplicant().toString();
            employees = new ArrayList<String>();
            employees.add(selectedEmployee);
            leaveFrom = request.getLeaveFrom();
            message = request.getMessage();
            leaveFrom = request.getLeaveFrom();
            Date leaveTo = request.getLeaveTo();
            minutes = (int) TimeUnit.MINUTES.convert(leaveTo.getTime() - leaveFrom.getTime(),TimeUnit.MILLISECONDS);
        }else {
            request = new LeaveRequest();
            request.setApprover(employee);
            request.setCreatedOn(new Date());
            request.setStatus(EmergencyRegistered);
            selectedEmployee = employees.get(0);
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, 8);
            c.set(Calendar.MINUTE, 30);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            leaveFrom = c.getTime();
            minutes = 30;
        }
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
        request.setLeaveFrom(leaveFrom);
        Calendar c = Calendar.getInstance();
        c.setTime(leaveFrom);
        c.add(Calendar.MINUTE,minutes);
        final Date leaveTo = c.getTime();
        request.setLeaveTo(leaveTo);
        String number = "E-";
        number += request.getApplicant().getInitials() + "-";
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmm");
        number += sdf.format(leaveFrom) + "-";
        number += sdf.format(leaveTo);
        request.setNumber(checkNumber(number,1));
        request.setType("Emergency");
        request.setTickets(false);
        request.setMessage(message);

        if (request.getApprovedOn() == null)
            request.setApprovedOn(new Date());
        final List<LeaveRequest> requests = service.listLastLeaveRequestsByApplicant(request.getApplicant(), request.getLeaveFrom(), request.getLeaveTo());
        if(requests.size()>0){
            Messagebox.show("Error: There is already a Leave Request overlapping the selected Dates!", "Warning", Messagebox.ABORT, Messagebox.EXCLAMATION, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if(event.getName().equals("onAbort")){
                        Executions.sendRedirect("/pages/hr/viewEmergencyLeaveRegistrations.zul");
                    }
                }
            });
        }else{
            long hoursInMillis = leaveTo.getTime() - leaveFrom.getTime();
            int minutes = (int) TimeUnit.MINUTES.convert(hoursInMillis, TimeUnit.MILLISECONDS);
            LeaveBalance bal = new LeaveBalance();
            bal.setEmployee(request.getApplicant());
            bal.setValue(minutes);
            bal.setType(request.getType());
            service.updateLeaveBalance(bal);
            service.saveLeaveRequest(request);
            Messagebox.show("Leave Request Registered! New Emergency Leave Balance For the Employee is " + minutes + " Minutes", "Success", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
                public void onEvent(Event event) throws Exception {
                    Executions.sendRedirect("/pages/hr/viewEmergencyLeaveRegistrations.zul");
                }
            });
        }
    }

    protected void showInfo(String message) {
        Messagebox.show(message, "Error!", Messagebox.OK, Messagebox.INFORMATION);
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

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
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
