package qa.tecnositafgulf.viewmodel.hr;

import net.sf.jasperreports.engine.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.sys.PageCtrl;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import qa.tecnositafgulf.config.LeaveRequestStates;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.leaves.LeaveRequest;
import qa.tecnositafgulf.model.reports.LeaveRequestReportDataSource;
import qa.tecnositafgulf.searchcriteria.hr.LeaveRequestSearchCriteria;
import qa.tecnositafgulf.service.LeaveRequestService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * Created by hadi on 12/31/17.
 */
public class ViewLeaveRequestsViewModel extends IntranetVM{
    private LeaveRequestService service;
    private List<LeaveRequest> leaveRequests;
    private LeaveRequestSearchCriteria leaveSearchCriteria;
    Employee employee;
    private Timer timer;
    private Desktop desktop;
    private Integer totalSize;
    private Integer activePage;
    private List<String> types;
    private List<String> tickets;
    private List<String> states;
    private int currentAnnualLeaveBalance;
    private int currentCasualLeaveBalance;
    private int currentEmergencyLeaveBalance;
    private int currentSickLeaveBalance;
    private String reportPath;
    private String leaveRequestReportTemplateName;



    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
        init();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        service = context.getBean(LeaveRequestService.class);
        leaveSearchCriteria = new LeaveRequestSearchCriteria();
        employee = ((Employee) Sessions.getCurrent().getAttribute("employee"));
        this.setActivePage(this.leaveSearchCriteria.getStartIndex());
        types = new ArrayList<>();
        types.add("All");
        types.add("Annual");
        types.add("Casual");
        types.add("Emergency");
        types.add("Sick");
        tickets = new ArrayList<>();
        tickets.add("Both");
        tickets.add("Yes");
        tickets.add("No");
        states = new ArrayList<>();
        states.add("All");
        for(int i = 1; i< 12; i++){
            states.add(LeaveRequestStates.toString(i));
        }
        load();
        desktop = Executions.getCurrent().getDesktop();
        if(timer!=null)
            timer.cancel();
        timer = new Timer();
        timer.schedule(updatePosts(),0,1000);
        currentAnnualLeaveBalance = service.getLeaveBalance(employee,"Annual");
        currentCasualLeaveBalance = service.getLeaveBalance(employee,"Casual");
        currentEmergencyLeaveBalance = service.getLeaveBalance(employee,"Emergency");
        currentSickLeaveBalance = service.getLeaveBalance(employee,"Sick");
        
        reportPath = "http://"+Executions.getCurrent().getServerName()+":"+Executions.getCurrent().getServerPort()+Executions.getCurrent().getContextPath()+"/static/reports/";
        leaveRequestReportTemplateName = "LeaveRequestReport.jrxml";

        addCommonTags((PageCtrl) view.getPage());
    }

    public void load(){
        this.leaveSearchCriteria.setApplicant(employee);
        this.setTotalSize(service.listLastLeaveRequestsByApplicantCount(leaveSearchCriteria));
        this.leaveSearchCriteria.setStartIndex(getActivePage());
        this.leaveRequests = service.listLastLeaveRequestsByApplicant(leaveSearchCriteria);
        for(LeaveRequest request : leaveRequests){
            service.deleteLeaveRequestEmployeeNotification(request, employee);
        }
        BindUtils.postNotifyChange(null, null, this, "leaveRequests");

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
    public void add(){
        ((Window) Executions.getCurrent().createComponents("/pages/hr/saveLeaveRequest.zul", null, null)).doModal();
    }

    @Command
    public void exportPDF(@BindingParam("item") final LeaveRequest request) {
        try {
            HashMap map = new HashMap<>();
            JRDataSource dataSource = new LeaveRequestReportDataSource(request, service.getLeaveBalance(request.getApplicant(), request.getType()));
            JasperPrint jasperPrint;
            URL url = new URL(reportPath + leaveRequestReportTemplateName);
            InputStream reportTemplate = url.openStream();
            JasperReport report = JasperCompileManager.compileReport(reportTemplate);
            jasperPrint = JasperFillManager.fillReport(report, map, dataSource);
            byte[] document = JasperExportManager.exportReportToPdf(jasperPrint);
            Filedownload.save(document, "application/pdf", "Leave_Request_Report_"+request.getNumber()+"_"+(new Date()).toString());
        }catch (Exception e){
            Messagebox.show("An error occurred! \n"+e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
            return;
        }

    }


    private int requestSize(){
        List<LeaveRequest> leaveRequests = service.listLastLeaveRequestsByApplicant(leaveSearchCriteria);
        return leaveRequests.size();
    }

    private boolean update(){
        if(requestSize()!=leaveRequests.size())
            return true;
        return false;
    }


    public List<LeaveRequest> getLeaveRequests() {
        return leaveRequests;
    }

    public void setLeaveRequests(List<LeaveRequest> leaveRequests) {
        this.leaveRequests = leaveRequests;
    }

    public boolean isApproverConfigured(){
        Employee employee = (Employee) Sessions.getCurrent().getAttribute("employee");
        if(employee.getLeaveManagerEmployee()==null)
            return false;
        return true;
    }

    public LeaveRequestSearchCriteria getLeaveSearchCriteria() {
        return leaveSearchCriteria;
    }

    public void setLeaveSearchCriteria(LeaveRequestSearchCriteria leaveSearchCriteria) {
        this.leaveSearchCriteria = leaveSearchCriteria;
    }

    public Integer getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Integer totalSize) {
        this.totalSize = totalSize;
    }

    public Integer getActivePage() {
        return activePage;
    }

    @NotifyChange({"activePage", "totalSize", "leaveSearchCriteria", "leaveRequests"})
    public void setActivePage(Integer activePage) {
        this.activePage = activePage;
        load();
    }

    public boolean isCancellable(LeaveRequest request){
        if(
                request.getStatus()!= LeaveRequestStates.WaitingForTickets &&
                request.getStatus()!= LeaveRequestStates.TicketsToBePurchased &&
                request.getStatus()!= LeaveRequestStates.Approved &&
                request.getStatus()!= LeaveRequestStates.CancelledByApplicant &&
                request.getStatus()!= LeaveRequestStates.RefusedByFinance &&
                request.getStatus()!= LeaveRequestStates.RefusedByHR &&
                request.getStatus()!= LeaveRequestStates.RefusedByManagement &&
                request.getStatus()!= LeaveRequestStates.RefusedByFinanceAfterTicketSelection &&
                request.getStatus()!= LeaveRequestStates.SickRegistered &&
                request.getStatus()!= LeaveRequestStates.EmergencyRegistered
        )
            return true;
        return false;
    }

    public boolean isExportReport(LeaveRequest request){
        if (
                request.getStatus() == LeaveRequestStates.Approved ||
                request.getStatus() == LeaveRequestStates.RefusedByManagement ||
                request.getStatus() == LeaveRequestStates.RefusedByHR ||
                request.getStatus() == LeaveRequestStates.RefusedByFinanceAfterTicketSelection ||
                request.getStatus() == LeaveRequestStates.RefusedByFinance ||
                request.getStatus()!= LeaveRequestStates.SickRegistered ||
                request.getStatus()!= LeaveRequestStates.EmergencyRegistered)
            return true;
        return false;
    }

    @Command
    @NotifyChange({"leaveRequests"})
    public void delete(@BindingParam("item") final LeaveRequest request){
        if(isCancellable(request))
            Messagebox.show("Are you sure to cancel the leave request?", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.EXCLAMATION, new EventListener<Event>() {
                public void onEvent(Event event) throws Exception {
                    if (event.getName().equals("onOK")) {
                        service.cancelLeaveRequest(request);
                        Executions.sendRedirect("/pages/hr/viewLeaveRequests.zul");
                    }
                }
            });
        else
            Messagebox.show("Request cannot be deleted!", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
    }

    @Command
    public void showMessage(@BindingParam("item") LeaveRequest request){
        if(request.getStatus() == LeaveRequestStates.RefusedByManagement || request.getStatus() == LeaveRequestStates.RefusedByHR || request.getStatus() == LeaveRequestStates.RefusedByFinance || request.getStatus() == LeaveRequestStates.RefusedByFinanceAfterTicketSelection){
            Clients.showNotification("Reason of Refusal: "+request.getReason());
        }else if(request.getMessage()!=null && request.getStatus()!=LeaveRequestStates.Approved){
            Clients.showNotification("Message to "+request.getApprover().getName()+" "+request.getApprover().getFamilyName()+" : "+request.getMessage());
        }
    }

    public String getDescription(int status){
        return LeaveRequestStates.toString(status);
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public List<String> getTickets() {
        return tickets;
    }

    public void setTickets(List<String> tickets) {
        this.tickets = tickets;
    }

    public List<String> getStates() {
        return states;
    }

    public void setStates(List<String> states) {
        this.states = states;
    }

    public int getCurrentAnnualLeaveBalance() {
        return currentAnnualLeaveBalance;
    }

    public void setCurrentAnnualLeaveBalance(int currentAnnualLeaveBalance) {
        this.currentAnnualLeaveBalance = currentAnnualLeaveBalance;
    }

    public int getCurrentCasualLeaveBalance() {
        return currentCasualLeaveBalance;
    }

    public void setCurrentCasualLeaveBalance(int currentCasualLeaveBalance) {
        this.currentCasualLeaveBalance = currentCasualLeaveBalance;
    }

    public int getCurrentEmergencyLeaveBalance() {
        return currentEmergencyLeaveBalance;
    }

    public void setCurrentEmergencyLeaveBalance(int currentEmergencyLeaveBalance) {
        this.currentEmergencyLeaveBalance = currentEmergencyLeaveBalance;
    }

    public int getCurrentSickLeaveBalance() {
        return currentSickLeaveBalance;
    }

    public void setCurrentSickLeaveBalance(int currentSickLeaveBalance) {
        this.currentSickLeaveBalance = currentSickLeaveBalance;
    }

     private TimerTask updatePosts() {
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
                        if(update())
                            load();
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
