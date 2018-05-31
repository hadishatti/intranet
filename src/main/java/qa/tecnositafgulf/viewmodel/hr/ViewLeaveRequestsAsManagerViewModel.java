package qa.tecnositafgulf.viewmodel.hr;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.sys.PageCtrl;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;
import qa.tecnositafgulf.config.LeaveRequestStates;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.leaves.LeaveRequest;
import qa.tecnositafgulf.searchcriteria.hr.LeaveRequestSearchCriteria;
import qa.tecnositafgulf.service.AdministrationService;
import qa.tecnositafgulf.service.LeaveRequestService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.util.*;

/**
 * Created by hadi on 1/3/18.
 */
public class ViewLeaveRequestsAsManagerViewModel extends IntranetVM {
    private LeaveRequestService service;
    private AdministrationService adminService;
    private List<LeaveRequest> leaveRequests;
    private List<LeaveRequest> approvedLeaveRequests;
    Employee employee;
    private Timer timer;
    private Desktop desktop;
    private LeaveRequestSearchCriteria leaveSearchCriteria;
    private LeaveRequestSearchCriteria approvedLeaveSearchCriteria;
    private Integer totalSize;
    private Integer activePage;
    private Integer totalSizeApproved;
    private Integer activePageApproved;
    private List<String> types;
    private List<String> tickets;
    private List<String> states;
    private List<Employee> applicants;
    private List<Employee> selectedApplicants;
    private List<Employee> selectedApprovedApplicants;

    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
        init();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        service = context.getBean(LeaveRequestService.class);
        adminService = context.getBean(AdministrationService.class);
        employee = (Employee) Sessions.getCurrent().getAttribute("employee");
        if(!isManager())
            Executions.sendRedirect("/pages/home.zul");
        leaveSearchCriteria = new LeaveRequestSearchCriteria();
        approvedLeaveSearchCriteria = new LeaveRequestSearchCriteria();
        this.setActivePage(this.leaveSearchCriteria.getStartIndex());
        this.setActivePageApproved(this.approvedLeaveSearchCriteria.getStartIndex());
        types = new ArrayList<>();
        types.add("All");
        types.add("Annual");
        types.add("Casual");
        types.add("Emergency");
        tickets = new ArrayList<>();
        tickets.add("Both");
        tickets.add("Yes");
        tickets.add("No");
        states = new ArrayList<>();
        states.add("All");
        for(int i = 1; i< 13; i++){
            if(i!=11)
                states.add(LeaveRequestStates.toString(i));
        }
        applicants = new ArrayList<>();
        if(super.isLeaveManager()) {
            List<Employee> employees = adminService.listEmployeesByManager(employee);
            for(Employee e : employees)
                if(!applicants.contains(e))
                    applicants.add(e);
        }
        if(super.isHRLeaveManager()) {
            List<Employee> employees = adminService.listEmployeesByHRManager(employee);
            for(Employee e : employees)
                if(!applicants.contains(e))
                    applicants.add(e);
        }
        if(super.isFinanceLeaveManager()) {
            List<Employee> employees = adminService.listEmployeesByFinanceManager(employee);
            for(Employee e : employees)
                if(!applicants.contains(e))
                    applicants.add(e);
        }
        if(super.isTicketLeaveManager()) {
            List<Employee> employees = adminService.listEmployeesByTicketManager(employee);
            for(Employee e : employees)
                if(!applicants.contains(e))
                    applicants.add(e);
        }
        selectedApplicants = new ArrayList<>(applicants);
        selectedApprovedApplicants = new ArrayList<>(applicants);
        load();
        loadApproved();
        desktop = Executions.getCurrent().getDesktop();
        if(timer!=null)
            timer.cancel();
        timer = new Timer();
        timer.schedule(update(),0,1000);
        addCommonTags((PageCtrl) view.getPage());
    }
    
    public void load(){
        this.leaveSearchCriteria.setApplicants(selectedApplicants);
        this.leaveSearchCriteria.setApprover(employee);
        this.setTotalSize(service.listLastLeaveRequestsByApproverCount(leaveSearchCriteria));
        this.leaveSearchCriteria.setStartIndex(getActivePage());
        if(selectedApplicants.isEmpty())
            leaveRequests = new ArrayList<>();
        else
            leaveRequests = service.listLastLeaveRequestsByApprover(leaveSearchCriteria);
        for(LeaveRequest request : leaveRequests){
            service.deleteLeaveRequestManagerNotification(request, employee);
        }
        BindUtils.postNotifyChange(null, null, this, "leaveRequests");
    }

    public void loadApproved(){
        this.approvedLeaveSearchCriteria.setApplicants(selectedApprovedApplicants);
        this.setTotalSizeApproved(service.listLastLeaveRequestsByApplicantsCount(approvedLeaveSearchCriteria));
        this.approvedLeaveSearchCriteria.setStartIndex(getActivePageApproved());
        if(selectedApprovedApplicants.isEmpty())
            approvedLeaveRequests = new ArrayList<>();
        else
            approvedLeaveRequests = service.listLastLeaveRequestsByApprover(leaveSearchCriteria);
        approvedLeaveRequests = service.listLastLeaveRequestsByApplicants(approvedLeaveSearchCriteria);
        BindUtils.postNotifyChange(null, null, this, "approvedLeaveRequests");
    }

    @Command
    public void viewDetails(@BindingParam("request") LeaveRequest request){
        final Map<String, LeaveRequest> params = new HashMap<>();
        params.put("request", request);
        ((Window) Executions.getCurrent().createComponents("/pages/hr/viewLeaveRequestDetails.zul", null, params)).doModal();
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "leaveSearchCriteria", "leaveRequests"})
    public void order(@BindingParam("orderBy") String orderBy){
        this.leaveSearchCriteria.setOrderByFieldName(orderBy);
        this.leaveSearchCriteria.setPageOrderMode("asc".equalsIgnoreCase(this.leaveSearchCriteria.getPageOrderMode()) ? "desc" : "asc");
        //load method call to load data
        load();
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "leaveSearchCriteria", "leaveRequests"})
    public void clearFilters() {
        this.leaveSearchCriteria.clear();
        this.leaveSearchCriteria.setPageOrderMode("desc");
        this.leaveSearchCriteria.clearFilters();
        this.selectAll();
        load();
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "leaveSearchCriteria", "leaveRequests"})
    public void filter() {
        this.leaveSearchCriteria.clear();
        this.leaveSearchCriteria.setPageOrderMode("desc");
        this.activePage = this.leaveSearchCriteria.getStartIndex();
        load();
    }

    @Command
    @NotifyChange({"activePageApproved", "totalSizeApproved", "approvedLeaveSearchCriteria", "approvedLeaveRequests"})
    public void orderApproved(@BindingParam("orderBy") String orderBy){
        this.approvedLeaveSearchCriteria.setOrderByFieldName(orderBy);
        this.approvedLeaveSearchCriteria.setPageOrderMode("asc".equalsIgnoreCase(this.approvedLeaveSearchCriteria.getPageOrderMode()) ? "desc" : "asc");
        //load method call to load data
        loadApproved();
    }

    @Command
    @NotifyChange({"activePageApproved", "totalSizeApproved", "approvedLeaveSearchCriteria", "approvedLeaveRequests"})
    public void clearApprovedFilters() {
        this.approvedLeaveSearchCriteria.clear();
        this.approvedLeaveSearchCriteria.setPageOrderMode("desc");
        this.approvedLeaveSearchCriteria.clearFilters();
        this.selectAllApproved();
        loadApproved();
    }

    @Command
    @NotifyChange({"activePageApproved", "totalSizeApproved", "approvedLeaveSearchCriteria", "approvedLeaveRequests"})
    public void filterApproved() {
        this.approvedLeaveSearchCriteria.clear();
        this.approvedLeaveSearchCriteria.setPageOrderMode("desc");
        this.activePage = this.approvedLeaveSearchCriteria.getStartIndex();
        loadApproved();
    }

    @Command
    public void selectAll(){
        this.selectedApplicants = new ArrayList<>(applicants);
        BindUtils.postNotifyChange(null, null, this, "selectedApplicants");

    }

    @Command
    public void deselectAll(){
        this.selectedApplicants.removeAll(applicants);
        BindUtils.postNotifyChange(null, null, this, "selectedApplicants");
    }

    @Command
    public void selectAllApproved(){
        this.selectedApprovedApplicants = new ArrayList<>(applicants);
        BindUtils.postNotifyChange(null, null, this, "selectedApprovedApplicants");
    }

    @Command
    public void deselectAllApproved(){
        this.selectedApprovedApplicants.removeAll(applicants);
        BindUtils.postNotifyChange(null, null, this, "selectedApprovedApplicants");
    }

    public List<LeaveRequest> getLeaveRequests() {
        return leaveRequests;
    }

    public void setLeaveRequests(List<LeaveRequest> leaveRequests) {
        this.leaveRequests = leaveRequests;
    }

    @Command
    public void manage(@BindingParam("item") final LeaveRequest request){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("request", request);
        Window window = (Window) Executions.createComponents("/pages/hr/approve-request.zul", null, map);
        window.doModal();
    }

    @Command
    public void showMessage(@BindingParam("item") LeaveRequest request){
        if(request.getStatus() == LeaveRequestStates.RefusedByManagement || request.getStatus() == LeaveRequestStates.RefusedByHR || request.getStatus() == LeaveRequestStates.RefusedByFinance || request.getStatus() == LeaveRequestStates.RefusedByFinanceAfterTicketSelection){
            Clients.showNotification("Reason of Refusal: "+request.getReason());
        }else if(request.getMessage()!=null){
            Clients.showNotification("Message to "+request.getApprover().getName()+" "+request.getApprover().getFamilyName()+" : "+request.getMessage());
        }
    }

    public boolean isVisible(LeaveRequest request){
        if(request.getStatus()==LeaveRequestStates.New || request.getStatus()==LeaveRequestStates.WaitingForHR || request.getStatus()==LeaveRequestStates.WaitingForFinance || request.getStatus()==LeaveRequestStates.WaitingForTickets || request.getStatus()==LeaveRequestStates.TicketsToBePurchased)
            return true;
        return false;
    }

    public String getDescription(int status){
        return LeaveRequestStates.toString(status);
    }

    public List<Employee> getApplicants() {
        return applicants;
    }

    public void setApplicants(List<Employee> applicants) {
        this.applicants = applicants;
    }

    public List<Employee> getSelectedApplicants() {
        return selectedApplicants;
    }

    public void setSelectedApplicants(List<Employee> selectedApplicants) {
        this.selectedApplicants = selectedApplicants;
    }

    public List<Employee> getSelectedApprovedApplicants() {
        return selectedApprovedApplicants;
    }

    public void setSelectedApprovedApplicants(List<Employee> selectedApprovedApplicants) {
        this.selectedApprovedApplicants = selectedApprovedApplicants;
    }

    public List<String> getStates() {
        return states;
    }

    public void setStates(List<String> states) {
        this.states = states;
    }

    public List<String> getTickets() {
        return tickets;
    }

    public void setTickets(List<String> tickets) {
        this.tickets = tickets;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public Integer getActivePage() {
        return activePage;
    }

    public void setActivePage(Integer activePage) {
        this.activePage = activePage;
    }

    public Integer getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Integer totalSize) {
        this.totalSize = totalSize;
    }

    public LeaveRequestSearchCriteria getLeaveSearchCriteria() {
        return leaveSearchCriteria;
    }

    public void setLeaveSearchCriteria(LeaveRequestSearchCriteria leaveSearchCriteria) {
        this.leaveSearchCriteria = leaveSearchCriteria;
    }

    public LeaveRequestSearchCriteria getApprovedLeaveSearchCriteria() {
        return approvedLeaveSearchCriteria;
    }

    public void setApprovedLeaveSearchCriteria(LeaveRequestSearchCriteria approvedLeaveSearchCriteria) {
        this.approvedLeaveSearchCriteria = approvedLeaveSearchCriteria;
    }

    public List<LeaveRequest> getApprovedLeaveRequests() {
        return approvedLeaveRequests;
    }

    public void setApprovedLeaveRequests(List<LeaveRequest> approvedLeaveRequests) {
        this.approvedLeaveRequests = approvedLeaveRequests;
    }

    public Integer getTotalSizeApproved() {
        return totalSizeApproved;
    }

    public void setTotalSizeApproved(Integer totalSizeApproved) {
        this.totalSizeApproved = totalSizeApproved;
    }

    public Integer getActivePageApproved() {
        return activePageApproved;
    }

    public void setActivePageApproved(Integer activePageApproved) {
        this.activePageApproved = activePageApproved;
    }

    private TimerTask update() {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    if(desktop == null) {
                        timer.cancel();
                        return;
                    }
                    Executions.activate(desktop);
                    try {
                        load();
                        loadApproved();
                    } finally {
                        Executions.deactivate(desktop);
                    }
                }catch(DesktopUnavailableException ex) {
                    System.out.println("Desktop currently unavailable");
                    timer.cancel();
                }catch(InterruptedException e) {
                    System.out.println("The server push thread interrupted");
                    timer.cancel();
                }
            }
        };
    }
}
