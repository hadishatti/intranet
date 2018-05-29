package qa.tecnositafgulf.viewmodel.administration.companies;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;
import qa.tecnositafgulf.model.administration.Company;
import qa.tecnositafgulf.service.AdministrationService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import javax.persistence.PersistenceException;

public class SaveCompanyViewModel extends IntranetVM {

	private Company company;
	private AdministrationService service;


	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("companyToModify") Company companyToModify) {
		init();
		company = new Company();
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		service = context.getBean(AdministrationService.class);
		if (companyToModify != null) {
			company = companyToModify;
		} else {
			company = new Company();
		}
	}

	@Command
	@NotifyChange("company")
	public void saveCompany() {
		try {
			service.saveCompany(company);
			Messagebox.show("OK! Do you want to view all the Companies?", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener<Event>() {
				public void onEvent(Event event) throws Exception {
					if (event.getName().equals("onOK")) {
						Executions.sendRedirect("/pages/administration/companies/viewCompanies.zul");
					}
				}
			});
		}catch (PersistenceException persistenceException){
			Messagebox.show("Company with same name already exits", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION);
		}
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
}
