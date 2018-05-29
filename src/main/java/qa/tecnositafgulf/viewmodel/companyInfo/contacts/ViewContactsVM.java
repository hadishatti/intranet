package qa.tecnositafgulf.viewmodel.companyInfo.contacts;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import qa.tecnositafgulf.config.MyProperties;
import qa.tecnositafgulf.model.companyInfo.Contact;
import qa.tecnositafgulf.searchcriteria.ContactSearchCriteria;
import qa.tecnositafgulf.service.CompanyInfoService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ameljo on 5/13/18.
 */
public class ViewContactsVM extends IntranetVM {

    private List<Contact> contacts;
    private Contact contact;
    private String htmlContent;
    private CompanyInfoService service;
    private Integer activePage;
    private Integer totalSize;
    private ContactSearchCriteria searchCriteria;


    @Init
    public void Init(){
        super.init();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        service = context.getBean(CompanyInfoService.class);
        searchCriteria = new ContactSearchCriteria();
        this.setActivePage(this.searchCriteria.getStartIndex());
        loadData();
    }

    @AfterCompose
    public void afterCompose(){

    }

    public void loadData(){
        this.setTotalSize(this.service.getContactsCount(searchCriteria));
        this.searchCriteria.setStartIndex(getActivePage());
        this.contacts = this.service.getContactsList(searchCriteria);
    }

//    @Command
//    @NotifyChange({"activePage", "totalSize", "contactSearchCriteria", "contacts"})
//    public void order(@BindingParam("orderBy") String orderBy){
//        this.searchCriteria.setOrderByFieldName(orderBy);
//        this.searchCriteria.setPageOrderMode("asc".equalsIgnoreCase(this.searchCriteria.getPageOrderMode()) ? "desc" : "asc");
//        //load method call to load data
//        loadData();
//    }

    @Command
    @NotifyChange({"activePage", "totalSize", "contactSearchCriteria", "contacts"})
    public void clearFilters() {
        this.searchCriteria.clear();
        this.searchCriteria.setPageOrderMode("desc");
        this.searchCriteria.clearFilters();
        this.loadData();
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "contactSearchCriteria", "contacts"})
    public void filter() {
        this.searchCriteria.clear();
        this.searchCriteria.setPageOrderMode("desc");
        this.activePage = this.searchCriteria.getStartIndex();
        this.loadData();
    }

    @NotifyChange({"activePage", "totalSize", "contactSearchCriteria", "contacts"})
    public void setActivePage(Integer activePage) {
        this.activePage = activePage;
        loadData();
    }

    @Command
    @NotifyChange("contacts")
    public void add(){
        final Map<String, Contact> params = new HashMap<String, Contact>();
        params.put("contactToModify", null);
        ((Window) Executions.getCurrent().createComponents("/pages/company-info/contacts/saveContact.zul", null, params)).doModal();
    }

    @Command
    @NotifyChange("contact")
    public void modify(@BindingParam("item") Contact contact){
        final Map<String, Contact> params = new HashMap<String, Contact>();
        params.put("contactToModify", contact);
        ((Window) Executions.getCurrent().createComponents("/pages/company-info/contacts/saveContact.zul", null, params)).doModal();
    }

    @Command
    @NotifyChange("contact")
    public void delete(@BindingParam("item") final Contact contact){
        Messagebox.show("Do you want to delete this contact?", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener<Event>() {
            public void onEvent(Event event) throws Exception {
                if (event.getName().equals("onOK")) {
                    contacts.remove(contact);
                    service.removeContact(contact);
                    Executions.sendRedirect("/pages/company-info/contacts/viewContacts.zul");
                }
            }
        });
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public String getNameSurname(Contact contact){
        String ret = "Name Surname: ";

        if(contact.getName() != null && !contact.getName().isEmpty())
            ret += contact.getName() + " ";
        if(contact.getSurname() != null && !contact.getSurname().isEmpty())
            ret += contact.getSurname();

        return ret;
    }

    public String getEmailAddress(Contact contact){
        String ret ="";
         if(contact.getEmail() != null && !contact.getEmail().isEmpty())
             ret += "Email: " + contact.getEmail();

        return  ret;
    }

    public String getPhoneNumber(Contact contact){
        String ret ="";
        if(contact.getNumber() != null && !contact.getNumber().isEmpty())
            ret += "Mobile: " + contact.getNumber();

        return ret;
    }

    public String getPositionString(Contact contact){
        String ret ="Position: ";
        if(contact.getPosition() != null && !contact.getPosition().isEmpty())
            ret += contact.getPosition();

        return ret;
    }

    public String getImage(Contact contact){
        if(contact.getImage().equals("NONE"))
            return  MyProperties.getInstance().getImagePath() + "/NONE.png";
        else
            return contact.getImage();
    }

    public void setContacts(List<Contact> contacts) {
        contacts = contacts;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public Integer getActivePage() {
        return activePage;
    }

    public Integer getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Integer totalSize) {
        this.totalSize = totalSize;
    }

    public ContactSearchCriteria getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(ContactSearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }
}
