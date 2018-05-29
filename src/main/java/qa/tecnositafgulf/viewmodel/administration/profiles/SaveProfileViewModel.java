package qa.tecnositafgulf.viewmodel.administration.profiles;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;
import qa.tecnositafgulf.model.administration.Profile;
import qa.tecnositafgulf.service.AdministrationService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import javax.persistence.PersistenceException;

public class SaveProfileViewModel extends IntranetVM{

	private Profile profile;
	private AdministrationService service;
	private boolean modify;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("profileToModify") Profile profileToModify) {
		init();
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		service = context.getBean(AdministrationService.class);
		if (profileToModify != null) {
			modify=true;
			profile = profileToModify;
		} else {
			modify=false;
			profile = new Profile();
		}
	}

	@Command
	@NotifyChange("profile")
	public void saveProfile() {
		try{
			if(profile.isDefaultProfile()){
				Profile previousDefaultProfile = service.getDefaultProfile();
				if(previousDefaultProfile!=null) {
					previousDefaultProfile.setDefaultProfile(false);
					service.saveProfile(previousDefaultProfile);
				}
			}
            service.saveProfile(profile);
            Messagebox.show("OK! Do you want to view all the Profiles?", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener<Event>() {
                public void onEvent(Event event) throws Exception {
                    if (event.getName().equals("onOK")) {
                        Executions.sendRedirect("/pages/administration/profiles/viewProfiles.zul");
                    }
					else if (event.getName().equals("onCancel")) {
						profile = service.getProfileByName(profile.getName());
					}
                }
            });
        }catch (PersistenceException persistenceException){
            Messagebox.show("Profile with same name already exits", "Warning", Messagebox.OK , Messagebox.ERROR);
        }
	}

	@Command
	@NotifyChange("profile")
	public void doNotAllowUncheck(){
		if(modify) {
			if (profile.isDefaultProfile()) {
				Messagebox.show("Set an other profile as default before saving!", "Warning", Messagebox.OK , Messagebox.ERROR);
			} else {
				profile.setDefaultProfile(true);
			}
		}else{
			profile.setDefaultProfile(!profile.isDefaultProfile());
		}
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}
}
