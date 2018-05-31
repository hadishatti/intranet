package qa.tecnositafgulf.viewmodel.administration.roles;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.sys.PageCtrl;
import org.zkoss.zul.Messagebox;
import qa.tecnositafgulf.model.administration.Role;
import qa.tecnositafgulf.service.AdministrationService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import javax.persistence.PersistenceException;

public class SaveRoleViewModel extends IntranetVM{

	private Role role;
	private AdministrationService service;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("roleToModify") Role roleToModify) {
		init();
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		service = context.getBean(AdministrationService.class);
		if (roleToModify != null) {
			role = roleToModify;
		} else {
			role = new Role();
		}
		addCommonTags((PageCtrl) view.getPage());
	}

	@Command
	@NotifyChange("role")
	public void saveRole() {
		try {
			service.saveRole(role);
			Messagebox.show("OK! Do you want to view all the Roles?", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener<Event>() {
				public void onEvent(Event event) throws Exception {
					if (event.getName().equals("onOK")) {
						Executions.sendRedirect("/pages/administration/roles/viewRoles.zul");
					}
				}
			});
		}catch (PersistenceException persistenceException){
            Messagebox.show("Role with same name already exits", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION);
        }
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}
