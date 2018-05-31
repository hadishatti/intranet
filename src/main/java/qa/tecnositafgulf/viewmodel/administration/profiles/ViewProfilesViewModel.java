package qa.tecnositafgulf.viewmodel.administration.profiles;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.sys.PageCtrl;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import qa.tecnositafgulf.model.administration.Profile;
import qa.tecnositafgulf.searchcriteria.ProfileSearchCriteria;
import qa.tecnositafgulf.service.AdministrationService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hadi on 12/17/17.
 */
public class ViewProfilesViewModel extends IntranetVM{
    private AdministrationService service;
    private List<Profile> profiles;
    private ProfileSearchCriteria profileSearchCriteria;
    private Integer totalSize;
    private Integer activePage;
    private boolean defaultProfile;


    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
        init();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        service = context.getBean(AdministrationService.class);
        profileSearchCriteria = new ProfileSearchCriteria();
        this.setActivePage(this.profileSearchCriteria.getStartIndex());
        defaultProfile=false;
        loadData();

        addCommonTags((PageCtrl) view.getPage());
    }

    private void loadData(){
        this.setTotalSize(this.service.getUserProfileCount(profileSearchCriteria));
        this.profileSearchCriteria.setStartIndex(getActivePage());
        this.profiles = this.service.getProfileList(profileSearchCriteria);
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "profileSearchCriteria", "profiles"})
    public void order(@BindingParam("orderBy") String orderBy){
        this.profileSearchCriteria.setOrderByFieldName(orderBy);
        this.profileSearchCriteria.setPageOrderMode("asc".equalsIgnoreCase(this.profileSearchCriteria.getPageOrderMode()) ? "desc" : "asc");
        //load method call to load data
        loadData();
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "profileSearchCriteria", "profiles"})
    public void clearFilters() {
        this.profileSearchCriteria.clear();
        this.profileSearchCriteria.setPageOrderMode("desc");
        this.profileSearchCriteria.clearFilters();
        this.loadData();
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "profileSearchCriteria", "profiles"})
    public void filter() {
        this.profileSearchCriteria.clear();
        this.profileSearchCriteria.setPageOrderMode("desc");
        this.activePage = this.profileSearchCriteria.getStartIndex();
        this.loadData();
    }

    @NotifyChange("profiles")
    private void load(){
        loadData();
    }

    public List<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
    }

    @Command
    @NotifyChange("profiles")
    public void modify(@BindingParam("item") Profile profile){
        final Map<String, Profile> params = new HashMap<String, Profile>();
        params.put("profileToModify", profile);
        ((Window) Executions.getCurrent().createComponents("/pages/administration/profiles/saveProfile.zul", null, params)).doModal();
    }

    @Command
    @NotifyChange("profiles")
    public void delete(@BindingParam("item") final Profile profile){
        if(profile.isDefaultProfile()){
            Messagebox.show("You must set an other profile as default before deleting this profile!", "Warning", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        try {
            Messagebox.show("Do you want to delete this Profile?", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener<Event>() {
                public void onEvent(Event event) throws Exception {
                    if (event.getName().equals("onOK")) {
                        profiles.remove(profile);
                        service.removeProfile(profile);
                        loadData();
                        Executions.sendRedirect("/pages/administration/profiles/viewProfiles.zul");
                    }
                }
            });
        }catch(ConstraintViolationException e){/** @Todo FIX **/
                Clients.showNotification("The Profile is used by a User.\nYou Cannot Delete it unless you delete first the User!", true);
                return;
        }
    }

    @Command
    @NotifyChange("profiles")
    public void add(){
        final Map<String, Profile> params = new HashMap<String, Profile>();
        params.put("profileToModify", null);
        ((Window) Executions.getCurrent().createComponents("/pages/administration/profiles/saveProfile.zul", null, params)).doModal();
    }

    public AdministrationService getService() {
        return service;
    }

    public void setService(AdministrationService service) {
        this.service = service;
    }

    public ProfileSearchCriteria getProfileSearchCriteria() {
        return profileSearchCriteria;
    }

    public void setProfileSearchCriteria(ProfileSearchCriteria profileSearchCriteria) {
        this.profileSearchCriteria = profileSearchCriteria;
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
    @NotifyChange({"activePage", "totalSize", "profileSearchCriteria", "profiles"})
    public void setActivePage(Integer activePage) {
        this.activePage = activePage;
        loadData();
    }
}
