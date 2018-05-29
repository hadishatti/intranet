package qa.tecnositafgulf.viewmodel.administration.departments;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import qa.tecnositafgulf.model.administration.Company;
import qa.tecnositafgulf.model.administration.Department;
import qa.tecnositafgulf.service.AdministrationService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

public class SaveDepartmentViewModel extends IntranetVM{

	private Department department;
	private AdministrationService service;
	private List<String> companies;
	private List<Company> companiesDB;
	private String selectedCompany;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("departmentToModify") Department departmentToModify) {
		init();
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		service = context.getBean(AdministrationService.class);
		if (departmentToModify != null) {
			department = departmentToModify;
		} else {
			department = new Department();
		}
		companiesDB = service.listCompanies();
		if(companiesDB.isEmpty()){
			Clients.showNotification("A Company must be created first!", true);
			Executions.sendRedirect("/pages/administration/companies/saveCompany.zul");
		}
		companies = new ArrayList<String>();
		for(Company company : companiesDB)
			companies.add(company.toString());
		if(departmentToModify!=null)
			selectedCompany = department.getCompany().toString();
		else
			selectedCompany = companies.get(0);
	}

	@Command
	@NotifyChange("department")
	public void saveDepartment() {
		int b = companies.indexOf(selectedCompany);
		department.setCompany(companiesDB.get(b));
		try{
			service.saveDepartment(department);
			Messagebox.show("OK! Do you want to view all the Departments?", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener<Event>() {
				public void onEvent(Event event) throws Exception {
					if (event.getName().equals("onOK")) {
						Executions.sendRedirect("/pages/administration/departments/viewDepartments.zul");
					}
				}
			});
		}catch (PersistenceException persistenceException){
            Messagebox.show("Department with same name already exits", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION);
        }

	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public List<String> getCompanies() {
		return companies;
	}

	public void setCompanies(List<String> companies) {
		this.companies = companies;
	}

	public String getSelected() {
		return selectedCompany;
	}

	public void setSelectedCompany(String selectedCompany) {
		this.selectedCompany = selectedCompany;
	}

	public List<Company> getCompaniesDB() {
		return companiesDB;
	}

	public AdministrationService getService() {
		return service;
	}

	public void setService(AdministrationService service) {
		this.service = service;
	}

	public String getSelectedCompany() {
		return selectedCompany;
	}

	public void setCompaniesDB(List<Company> companiesDB) {
		this.companiesDB = companiesDB;
	}


}
