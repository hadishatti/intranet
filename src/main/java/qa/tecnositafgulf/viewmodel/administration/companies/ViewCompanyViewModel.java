package qa.tecnositafgulf.viewmodel.administration.companies;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import qa.tecnositafgulf.model.administration.Company;
import qa.tecnositafgulf.searchcriteria.CompanySearchCriteria;
import qa.tecnositafgulf.service.AdministrationService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hadi on 12/17/17.
 */
public class ViewCompanyViewModel extends IntranetVM{
    private AdministrationService service;
    private CompanySearchCriteria companySearchCriteria;
    private List<Company> companies;
    private Integer totalSize;
    private Integer activePage;

    @AfterCompose
    @NotifyChange("companies")
    public void doAfterCompose(){
        init();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        service = context.getBean(AdministrationService.class);
        companySearchCriteria = new CompanySearchCriteria();
        this.setActivePage(this.companySearchCriteria.getStartIndex());
        loadData();
    }

    private void loadData(){
        this.setTotalSize(this.service.getCompanyCount(companySearchCriteria));
        this.companySearchCriteria.setStartIndex(getActivePage());
        this.companies = this.service.getCompanyList(companySearchCriteria);
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "companySearchCriteria", "companies"})
    public void order(@BindingParam("orderBy") String orderBy){
        this.companySearchCriteria.setOrderByFieldName(orderBy);
        this.companySearchCriteria.setPageOrderMode("asc".equalsIgnoreCase(this.companySearchCriteria.getPageOrderMode()) ? "desc" : "asc");
        //load method call to load data
        loadData();
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "companySearchCriteria", "companies"})
    public void clearFilters() {
        this.companySearchCriteria.clear();
        this.companySearchCriteria.setPageOrderMode("desc");
        this.companySearchCriteria.clearFilters();
        loadData();
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "companySearchCriteria", "companies"})
    public void filter() {
        this.companySearchCriteria.clear();
        this.companySearchCriteria.setPageOrderMode("desc");
        this.activePage = this.companySearchCriteria.getStartIndex();
        loadData();
    }


    @NotifyChange("companies")
    private void load(){
        loadData();
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }

    @Command
    @NotifyChange("companies")
    public void modify(@BindingParam("item") Company company){
        final Map<String, Company> params = new HashMap<String, Company>();
        params.put("companyToModify", company);
        ((Window) Executions.getCurrent().createComponents("/pages/administration/companies/saveCompany.zul", null, params)).doModal();
    }

    @Command
    @NotifyChange("companies")
    public void delete(@BindingParam("item") final Company company){
        try{
            Messagebox.show("Do you want to delete this Role?", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener<Event>() {
                public void onEvent(Event event) throws Exception {
                    if (event.getName().equals("onOK")) {
                        try {
                            service.removeCompany(company);
                            loadData();
                            Executions.sendRedirect("/pages/administration/companies/viewCompanies.zul");

                        }catch (Exception e){
                            Clients.showNotification("The Company is used by a Department.\nYou Cannot Delete it unless you delete first the Department!", true);
                        }
                    }
                }
            });
        }catch(Exception e){ /** @Todo FIX **/
            Clients.showNotification("The Company is used by a Department.\nYou Cannot Delete it unless you delete first the Department!", true);
            return;
        }
    }

    @Command
    @NotifyChange("companies")
    public void add(){
        final Map<String, Company> params = new HashMap<String, Company>();
        params.put("companyToModify", null);
        ((Window) Executions.getCurrent().createComponents("/pages/administration/companies/saveCompany.zul", null, params)).doModal();
    }

    public AdministrationService getService() {
        return service;
    }

    public void setService(AdministrationService service) {
        this.service = service;
    }

    public CompanySearchCriteria getCompanySearchCriteria() {
        return companySearchCriteria;
    }

    public void setCompanySearchCriteria(CompanySearchCriteria companySearchCriteria) {
        this.companySearchCriteria = companySearchCriteria;
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

    @NotifyChange({"activePage", "totalSize", "companySearchCriteria", "companies"})
    public void setActivePage(Integer activePage) {
        this.activePage = activePage;
        loadData();
    }
}
