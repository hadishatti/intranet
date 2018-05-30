package qa.tecnositafgulf.viewmodel.hr;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Window;
import qa.tecnositafgulf.config.LeaveRequestStates;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.leaves.LeaveBalance;
import qa.tecnositafgulf.model.leaves.LeaveRequest;
import qa.tecnositafgulf.service.AdministrationService;
import qa.tecnositafgulf.service.LeaveRequestService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by hadi on 1/3/18.
 */
public class ApproveLeaveRequestViewModel extends IntranetVM {
    private LeaveRequestService service;
    private AdministrationService adminService;
    private LeaveRequest leaveRequest;
    private boolean allowRefuse;
    private int days;
    private int leaveBalance;
    private int newBalance;
    private List<Employee> others;

    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view) {
        init();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        service = context.getBean(LeaveRequestService.class);
        adminService = context.getBean(AdministrationService.class);
        Map map = Executions.getCurrent().getArg();
        leaveRequest = (LeaveRequest) map.get("request");
        if (leaveRequest.getStatus() == LeaveRequestStates.WaitingForTickets)
            allowRefuse = false;
        else
            allowRefuse = true;
        leaveBalance = service.getLeaveBalance(leaveRequest.getApplicant(), leaveRequest.getType());
        days = (int) TimeUnit.DAYS.convert(leaveRequest.getLeaveTo().getTime()-leaveRequest.getLeaveFrom().getTime(),TimeUnit.MILLISECONDS)+1;
        newBalance = leaveBalance-days;
        others = new ArrayList<>();
        if(leaveRequest.getStatus()==LeaveRequestStates.New || leaveRequest.getStatus()==LeaveRequestStates.WaitingForHR)
            checkOtherEmployees();
    }

    public void checkOtherEmployees(){
        List<Employee> employees = new ArrayList<>();
        if(leaveRequest.getStatus()==LeaveRequestStates.New)
            employees = adminService.listEmployeesByManager(((Employee) Sessions.getCurrent().getAttribute("employee")));
        else if(leaveRequest.getStatus()==LeaveRequestStates.WaitingForHR)
            employees = adminService.listEmployeesByHRManager(((Employee) Sessions.getCurrent().getAttribute("employee")));
        for(Employee employee : employees){
            if(!employee.equals(leaveRequest.getApplicant())) {
                List<LeaveRequest> requests = service.listLastLeaveRequestsByApplicant(employee, leaveRequest.getLeaveFrom(), leaveRequest.getLeaveTo());
                if (requests.size() > 0)
                    others.add(employee);
            }
        }

    }

    public LeaveRequest getLeaveRequest() {
        return leaveRequest;
    }

    public void setLeaveRequest(LeaveRequest request) {
        this.leaveRequest = request;
    }

    public boolean isAllowRefuse() {
        return allowRefuse;
    }

    public void setAllowRefuse(boolean allowRefuse) {
        this.allowRefuse = allowRefuse;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getLeaveBalance() {
        return leaveBalance;
    }

    public void setLeaveBalance(int leaveBalance) {
        this.leaveBalance = leaveBalance;
    }

    public int getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(int newBalance) {
        this.newBalance = newBalance;
    }

    public List<Employee> getOthers() {
        return others;
    }

    public void setOthers(List<Employee> others) {
        this.others = others;
    }

    @Command
    public void approve() throws ParseException {
        final LeaveRequest leaveRequest = new LeaveRequest();
        //ReportLeaveRequest reportLeaveRequest = null;
        leaveRequest.setNumber(this.leaveRequest.getNumber());
        leaveRequest.setType(this.leaveRequest.getType());
        leaveRequest.setTickets(this.leaveRequest.isTickets());
        leaveRequest.setLeaveFrom(this.leaveRequest.getLeaveFrom());
        leaveRequest.setLeaveTo(this.leaveRequest.getLeaveTo());
        leaveRequest.setApplicant(this.leaveRequest.getApplicant());
        leaveRequest.setCreatedOn(this.leaveRequest.getCreatedOn());
        leaveRequest.setApprovedOn(new Date());
        leaveRequest.setAddressOnHoliday(this.leaveRequest.getAddressOnHoliday());
        leaveRequest.setPhoneNumber(this.leaveRequest.getPhoneNumber());
        leaveRequest.setEmployeeOnBehalf(this.leaveRequest.getEmployeeOnBehalf());
        leaveRequest.setCasualLeaveReason(this.leaveRequest.getCasualLeaveReason());
        switch (this.leaveRequest.getStatus()) {
            case LeaveRequestStates.New:
                leaveRequest.setApprover(this.leaveRequest.getApplicant().getHRLeaveManagerEmployee());
                leaveRequest.setManager(this.leaveRequest.getApprover());
                leaveRequest.setStatus(LeaveRequestStates.WaitingForHR);
                break;
            case LeaveRequestStates.WaitingForHR:
                leaveRequest.setApprover(this.leaveRequest.getApplicant().getFinanceLeaveManagerEmployee());
                leaveRequest.setStatus(LeaveRequestStates.WaitingForFinance);
                break;
            case LeaveRequestStates.WaitingForFinance:
                if (this.leaveRequest.isTickets()) {
                    leaveRequest.setApprover(this.leaveRequest.getApplicant().getTicketLeaveManagerEmployee());
                    leaveRequest.setStatus(LeaveRequestStates.WaitingForTickets);
                } else {
                    leaveRequest.setApprover(this.leaveRequest.getApplicant().getFinanceLeaveManagerEmployee());
                    leaveRequest.setStatus(LeaveRequestStates.Approved);
                }
                break;
            case LeaveRequestStates.WaitingForTickets:
                leaveRequest.setApprover(this.leaveRequest.getApplicant().getFinanceLeaveManagerEmployee());
                leaveRequest.setStatus(LeaveRequestStates.TicketsToBePurchased);
                break;
            case LeaveRequestStates.TicketsToBePurchased:
                leaveRequest.setApprover(this.leaveRequest.getApplicant().getFinanceLeaveManagerEmployee());
                leaveRequest.setStatus(LeaveRequestStates.Approved);
                break;
            default:
                break;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("approved",true);
        map.put("request", leaveRequest);
        Window window = (Window) Executions.createComponents("/pages/hr/leaveRequestReasonMessageBox.zul", null, map);
        window.doModal();

    }

    @Command
    public void refuse() {
        final LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setNumber(this.leaveRequest.getNumber());
        leaveRequest.setType(this.leaveRequest.getType());
        leaveRequest.setTickets(this.leaveRequest.isTickets());
        leaveRequest.setLeaveFrom(this.leaveRequest.getLeaveFrom());
        leaveRequest.setLeaveTo(this.leaveRequest.getLeaveTo());
        leaveRequest.setApplicant(this.leaveRequest.getApplicant());
        leaveRequest.setCreatedOn(this.leaveRequest.getCreatedOn());
        leaveRequest.setApprovedOn(new Date());
        leaveRequest.setApprover(this.leaveRequest.getApprover());
        leaveRequest.setAddressOnHoliday(this.leaveRequest.getAddressOnHoliday());
        leaveRequest.setPhoneNumber(this.leaveRequest.getPhoneNumber());
        leaveRequest.setEmployeeOnBehalf(this.leaveRequest.getEmployeeOnBehalf());
        leaveRequest.setCasualLeaveReason(this.leaveRequest.getCasualLeaveReason());
        switch (this.leaveRequest.getStatus()) {
            case LeaveRequestStates.New:
                leaveRequest.setStatus(LeaveRequestStates.RefusedByManagement);
                leaveRequest.setManager(this.leaveRequest.getApprover());
                break;
            case LeaveRequestStates.WaitingForHR:
                leaveRequest.setStatus(LeaveRequestStates.RefusedByHR);
                break;
            case LeaveRequestStates.WaitingForFinance:
                leaveRequest.setStatus(LeaveRequestStates.RefusedByFinance);
                break;
            case LeaveRequestStates.TicketsToBePurchased:
                leaveRequest.setStatus(LeaveRequestStates.RefusedByFinanceAfterTicketSelection);
                break;
            default:
                break;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("approved",false);
        map.put("request", leaveRequest);
        Window window = (Window) Executions.createComponents("/pages/hr/leaveRequestReasonMessageBox.zul", null, map);
        window.doModal();

    }

    @Command
    public void goBack(){
        Executions.sendRedirect("/pages/hr/viewLeaveRequestsToManage.zul");
    }
}
