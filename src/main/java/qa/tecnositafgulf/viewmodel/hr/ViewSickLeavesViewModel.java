package qa.tecnositafgulf.viewmodel.hr;

import net.sf.jasperreports.engine.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Filedownload;
import org.zkoss.zk.ui.sys.PageCtrl;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import qa.tecnositafgulf.config.LeaveRequestStates;
import qa.tecnositafgulf.config.MyProperties;
import qa.tecnositafgulf.model.administration.Company;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.leaves.LeaveRequest;
import qa.tecnositafgulf.model.reports.LeaveRequestReportDataSource;
import qa.tecnositafgulf.searchcriteria.hr.LeaveRequestSearchCriteria;
import qa.tecnositafgulf.service.AdministrationService;
import qa.tecnositafgulf.service.LeaveRequestService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * Created by hadi on 1/4/18.
 */
public class ViewSickLeavesViewModel extends IntranetVM {
    private LeaveRequestService service;
    private AdministrationService adminService;
    private List<LeaveRequest> leaveRequests;
    private LeaveRequestSearchCriteria leaveSearchCriteria;
    private Integer totalSize;
    private Integer activePage;
    private Employee employee;
    private List<Employee> applicants;
    private List<Employee> selectedApplicants;
    private String reportPath;
    private String leaveRequestReportTemplateName;


    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
        init();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        service = context.getBean(LeaveRequestService.class);
        adminService = context.getBean(AdministrationService.class);
        employee = (Employee) Sessions.getCurrent().getAttribute("employee");
        if(!authenticationService.isProfilePresent(employee, "HRLM")){
            Executions.sendRedirect("/pages/home.zul");
            return;
        }
        List<Employee> employees = adminService.listEmployeesByHRManager(employee);
        applicants = new ArrayList<>(employees);
        selectedApplicants = new ArrayList<>(applicants);
        leaveSearchCriteria = new LeaveRequestSearchCriteria();

        reportPath = MyProperties.getInstance().getProperty("resPath")+"/reports/";
        leaveRequestReportTemplateName = "LeaveRequestReport.jrxml";

        load();
        addCommonTags((PageCtrl) view.getPage());
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

    @NotifyChange({"leaveRequests"})
    private void load(){
        this.leaveSearchCriteria.setApplicants(selectedApplicants);
        this.leaveSearchCriteria.setApprover(employee);
        if(selectedApplicants.isEmpty())
            leaveRequests = new ArrayList<>();
        else
            leaveRequests = service.listSickLeavesByApprover(leaveSearchCriteria);
    }

    public List<LeaveRequest> getLeaveRequests() {
        return leaveRequests;
    }

    public void setLeaveRequests(List<LeaveRequest> leaveRequests) {
        this.leaveRequests = leaveRequests;
    }

    @Command
    @NotifyChange({"leaveRequests"})
    public void delete(@BindingParam("item") final LeaveRequest request){
        Messagebox.show("Are you sure to cancel ?", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.EXCLAMATION, new EventListener<Event>() {
            public void onEvent(Event event) throws Exception {
                if (event.getName().equals("onOK")) {
                    service.deleteSickLeave(request);
                    Executions.sendRedirect("/pages/hr/viewSickLeaveRegistrations.zul");
                }
            }
        });
    }

    public String getDescription(int status){
        return LeaveRequestStates.toString(status);
    }

    @Command
    @NotifyChange("leaveRequests")
    public void modify(@BindingParam("item") final LeaveRequest request){
        final Map<String, LeaveRequest> params = new HashMap<String, LeaveRequest>();
        params.put("sickToModify", request);
        ((Window) Executions.getCurrent().createComponents("/pages/hr/sickLeaveRegistration.zul", null, params)).doModal();
    }

    @Command
    @NotifyChange("leaveRequests")
    public void add(){
        List<Employee> employeesDB = adminService.listEmployeesByHRManager(employee);
        if (employeesDB.isEmpty()){
            Messagebox.show("This account is not configured as manager to any employee!", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }else {
            final Map<String, Company> params = new HashMap<String, Company>();
            params.put("sickToModify", null);
            ((Window) Executions.getCurrent().createComponents("/pages/hr/sickLeaveRegistration.zul", null, params)).doModal();
        }
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
            Filedownload.save(document, "application/pdf", "Sick_Leave_Report_"+request.getNumber()+"_"+(new Date()).toString());
        }catch (Exception e){
            Messagebox.show("An error occurred! \n"+e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
            return;
        }

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

    public void setActivePage(Integer activePage) {
        this.activePage = activePage;
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
}
