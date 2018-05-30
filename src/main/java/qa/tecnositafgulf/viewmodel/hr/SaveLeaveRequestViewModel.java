package qa.tecnositafgulf.viewmodel.hr;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Messagebox;
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

import static qa.tecnositafgulf.config.LeaveRequestStates.New;

/**
 * Created by hadi on 12/31/17.
 */
public class SaveLeaveRequestViewModel extends IntranetVM{
    private LeaveRequestService service;
    private AdministrationService adminService;
    private LeaveRequest request;
    private List<String> types;
    private String selectedType;
    private Date leaveFrom;
    private Date leaveTo;
    private Employee applicant;
    private String message;
    private String addressOnHoliday;
    private String phoneNumber;
    private Employee employeeOnBehalf;
    private String actingPosition;
    private boolean tickets;
    private boolean addPhone1;
    private boolean addPhone2;
    private boolean viewOtherPhone;
    private String otherPhoneNumber;
    private String casualLeaveReason;
    private Integer selectedPrefixIndex;
    private List<Employee> employees;



    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
        init();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        adminService = context.getBean(AdministrationService.class);
        service = context.getBean(LeaveRequestService.class);
        Employee employee = (Employee) Sessions.getCurrent().getAttribute("employee");
        employees = adminService.listEmployees();
        applicant = employee;
        request = new LeaveRequest();
        request.setApplicant(applicant);
        request.setApprover(applicant.getLeaveManagerEmployee());
        request.setCreatedOn(new Date());
        request.setStatus(New);
        types = new ArrayList<String>();
        types.add("Annual");
        types.add("Casual");
        types.add("Emergency");
        selectedType = types.get(0);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 8);
        c.set(Calendar.MINUTE, 30);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        leaveFrom = c.getTime();
        c.add(Calendar.HOUR,9);
        leaveTo = c.getTime();
        viewOtherPhone=false;
    }

    @Command
    @NotifyChange({"viewOtherPhone","otherPhoneNumber"})
    public void viewOtherPhone(){
        viewOtherPhone = !viewOtherPhone;
        if(!viewOtherPhone) {
            otherPhoneNumber = "";
        }
    }

    @Command
    public void save(){
        if(leaveFrom.after(leaveTo)){
            Messagebox.show("Starting Leave Date cannot be after the Ending Leave Date!", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }else if(service.countPendingRequestsByApplicantAndDates(applicant, leaveFrom, leaveTo)>0){
            Messagebox.show("You have already at least one leave request overlapping with this new request!", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
        }else {
            request.setLeaveFrom(leaveFrom);
            request.setLeaveTo(leaveTo);
            long daysInMillis = leaveTo.getTime() - leaveFrom.getTime();
            int days = (int) TimeUnit.DAYS.convert(daysInMillis, TimeUnit.MILLISECONDS)+1;
            String number = "";
            if (selectedType.equals("Annual"))
                number = "A-";
            else if (selectedType.equals("Casual"))
                number = "C-";
            else if(selectedType.equals("Emergency"))
                number = "E-";
            number += applicant.getInitials() + "-";
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmm");
            number += sdf.format(leaveFrom) + "-";
            number += sdf.format(leaveTo);
            request.setNumber(checkNumber(number, 1));
            request.setType(selectedType);
            request.setTickets(tickets);
            request.setMessage(message);
            request.setEmployeeOnBehalf(employeeOnBehalf);
            request.setAddressOnHoliday(addressOnHoliday);
            request.setCasualLeaveReason(casualLeaveReason);
            if(!getAndValidatePhoneNumbers())
                return;
            else if(phoneNumber==null || phoneNumber.isEmpty()){
                Messagebox.show("Select at least one phone number or insert a new one!", "Error", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }
            request.setPhoneNumber(phoneNumber);
            request.setManager(request.getApprover());
            days = service.getLeaveBalance(applicant, selectedType)-days;
            Messagebox.show("Your "+selectedType+" Leave Balance will be updated to "+days+". Do you confirm?", "Warning", Messagebox.OK|Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener<Event>() {
                public void onEvent(Event event) throws Exception {
                    if(event.getName().equals(Events.ON_OK)) {
                        service.saveLeaveRequest(request);
                        Executions.sendRedirect("/pages/hr/viewLeaveRequests.zul");
                    }
                }
            });
        }
    }

    private boolean getAndValidatePhoneNumbers(){
        phoneNumber="";
        if(addPhone1){
            if(!phoneNumber.isEmpty())
                phoneNumber += ", "+applicant.getPhoneNumber1();
            else
                phoneNumber += applicant.getPhoneNumber1();
        }
        if(addPhone2){
            if(!phoneNumber.isEmpty())
                phoneNumber += ", "+applicant.getPhoneNumber2();
            else
                phoneNumber += applicant.getPhoneNumber2();
        }
        if(viewOtherPhone){
            if(selectedPrefixIndex==null || selectedPrefixIndex.intValue()==-1) {
                Messagebox.show("Select the prefix from the list!", "Error", Messagebox.OK, Messagebox.EXCLAMATION);
                return false;
            }
            else if(otherPhoneNumber==null || otherPhoneNumber.isEmpty() || !otherPhoneNumber.matches("[0-9]+")) {
                Messagebox.show("Insert a valid phone number!", "Error", Messagebox.OK, Messagebox.EXCLAMATION);
                return false;
            }else{
                if(!phoneNumber.isEmpty())
                    phoneNumber += ", "+getPrefixes().get(selectedPrefixIndex.intValue()).getCode() + " " + otherPhoneNumber;
                else
                    phoneNumber += getPrefixes().get(selectedPrefixIndex.intValue()).getCode() + " " + otherPhoneNumber;
            }
        }
        return true;
    }

    public String checkNumber(String number, int i){
        if(service.findByNumber(number).size()==0)
            return number;
        String[] numbers = number.split("_");
        return checkNumber(numbers[0]+"_"+i, ++i);
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public String getSelectedType() {
        return selectedType;
    }

    public void setSelectedType(String selectedType) {
        this.selectedType = selectedType;
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


    public String getAddressOnHoliday() {
        return addressOnHoliday;
    }

    public void setAddressOnHoliday(String addressOnHoliday) {
        this.addressOnHoliday = addressOnHoliday;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Employee getEmployeeOnBehalf() {
        return employeeOnBehalf;
    }

    public void setEmployeeOnBehalf(Employee employeeOnBehalf) {
        this.employeeOnBehalf = employeeOnBehalf;
    }

    public boolean isTickets() {
        return tickets;
    }

    public void setTickets(boolean tickets) {
        this.tickets = tickets;
    }

    public Employee getApplicant() {
        return applicant;
    }

    public boolean isAddPhone1() {
        return addPhone1;
    }

    public void setAddPhone1(boolean addPhone1) {
        this.addPhone1 = addPhone1;
    }

    public boolean isAddPhone2() {
        return addPhone2;
    }

    public void setAddPhone2(boolean addPhone2) {
        this.addPhone2 = addPhone2;
    }

    public boolean isViewOtherPhone() {
        return viewOtherPhone;
    }

    public void setViewOtherPhone(boolean viewOtherPhone) {
        this.viewOtherPhone = viewOtherPhone;
    }

    public String getOtherPhoneNumber() {
        return otherPhoneNumber;
    }

    public void setOtherPhoneNumber(String otherPhoneNumber) {
        this.otherPhoneNumber = otherPhoneNumber;
    }

    public String getCasualLeaveReason() {
        return casualLeaveReason;
    }

    public void setCasualLeaveReason(String casualLeaveReason) {
        this.casualLeaveReason = casualLeaveReason;
    }

    public Integer getSelectedPrefixIndex() {
        return selectedPrefixIndex;
    }

    public void setSelectedPrefixIndex(Integer selectedPrefixIndex) {
        this.selectedPrefixIndex = selectedPrefixIndex;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}
