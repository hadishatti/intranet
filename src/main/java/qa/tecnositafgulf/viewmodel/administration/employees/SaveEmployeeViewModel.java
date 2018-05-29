package qa.tecnositafgulf.viewmodel.administration.employees;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.*;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import qa.tecnositafgulf.model.leaves.LeaveBalance;
import qa.tecnositafgulf.service.LeaveRequestService;
import qa.tecnositafgulf.viewmodel.IntranetVM;
import qa.tecnositafgulf.model.administration.Department;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.administration.Profile;
import qa.tecnositafgulf.model.administration.Role;
import qa.tecnositafgulf.service.AdministrationService;
import qa.tecnositafgulf.spring.config.AppConfig;

import javax.persistence.PersistenceException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by hadi on 5/14/18.
 */
public class SaveEmployeeViewModel extends IntranetVM{

    private AdministrationService service;
    private LeaveRequestService leaveService;
    private Employee employee;
    private List<String> roles;
    private List<Role> rolesDB;
    private List<String> profiles;
    private List<Profile> profilesDB;
    private List<String> departments;
    private List<Department> departmentsDB;
    private String selectedDepartment;
    private String selectedRole;
    private List<String> selectedProfiles;
    private boolean currentEmployee = false;
    private boolean modify;
    private String repeatedPassword;
    private List<String> nationalities;
    private String birthConstraint;

    private Integer selectedPrefix1Index;
    private String selectedPrefix1;
    private Integer selectedPrefix2Index;
    private String selectedPrefix2;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("employeeToModify") Employee employeeToModify) {
        init();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        service = context.getBean(AdministrationService.class);
        leaveService = context.getBean(LeaveRequestService.class);
        initializeCombos();
        if (employeeToModify != null) {
            modify=true;
            employee = employeeToModify;
            if(employee.equals(((Employee) Sessions.getCurrent().getAttribute("employee"))))
                currentEmployee = true;
            if(employee.getPersonalPhoneNumberPrefix1()!=null){
                selectedPrefix1Index = getPrefixIndex(employee.getPersonalPhoneNumberPrefix1());
            }
            if(employee.getPersonalPhoneNumberPrefix2()!=null){
                selectedPrefix2Index = getPrefixIndex(employee.getPersonalPhoneNumberPrefix2());
            }
        } else {
            modify=false;
            employee = new Employee();
            selectedPrefix1Index = new Integer(179);
        }
        if(employee.getBirthDate()==null){
            Calendar c = Calendar.getInstance();
            c.add(Calendar.YEAR,-18);
            employee.setBirthDate(c.getTime());
        }
        if(employee.getNationality()==null){
            employee.setNationality(nationalities.get(0));
        }
        rolesDB = service.listRoles();
        if(rolesDB.isEmpty()){
            Clients.showNotification("A Role must be created first!", true);
            Executions.sendRedirect("/pages/administration/roles/saveRole.zul");
        }
        departmentsDB = service.listDepartments();
        if(departmentsDB.isEmpty()){
            Clients.showNotification("A Department must be created first!", true);
            Executions.sendRedirect("/pages/administration/departments/saveDepartment.zul");
        }

        profilesDB = service.listProfiles();
        if (profilesDB.isEmpty()){
            Clients.showNotification("A Profile must be created first", true);
            Executions.sendRedirect("/pages/administration/profiles/saveProfile.zul");
        }

        roles = new ArrayList<String>();
        for(Role role : rolesDB)
            roles.add(role.toString());
        departments = new ArrayList<String>();
        for(Department dep : departmentsDB)
            departments.add(dep.toString());
        profiles = new ArrayList<>();
        for(Profile p : profilesDB)
            profiles.add(p.toString());

        if(employeeToModify!=null){
            selectedDepartment = employee.getDepartment().toString();
            selectedRole = employee.getRole().toString();
            selectedProfiles = new ArrayList<>();
            for (Profile p : employeeToModify.getProfiles()){
                selectedProfiles.add(p.toString());
            }

        }else{
            selectedDepartment = departmentsDB.get(0).toString();
            selectedRole = rolesDB.get(0).toString();
            selectedProfiles = new ArrayList<>();
            selectedProfiles.add(profilesDB.get(0).toString());
        }

        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR,-18);
        DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        birthConstraint = "no empty, before "+sdf.format(c.getTime());



    }

    @Command
    @NotifyChange("selectedProfiles")
    public void selectAll(){
        selectedProfiles=profiles;
    }

    @Command
    @NotifyChange("selectedProfiles")
    public void deselectAll(){
        selectedProfiles = new ArrayList<>();
        selectedProfiles.add(service.getDefaultProfile().toString());
    }

    @Command
    public void savePassword(){
        if(employee.getPassword().isEmpty()){
            Messagebox.show("Password Cannot be Empty!", "Error", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        if (!repeatedPassword.equals(employee.getPassword())) {
            Messagebox.show("Password Mismatch!", "Error", Messagebox.OK, Messagebox.EXCLAMATION);
            repeatedPassword = "";
            return;
        }
        employee.setPassword(service.MD5(employee.getPassword()));
        service.saveEmployee(employee);
        Messagebox.show("OK! You will logged out. Please use the new password.", "Information", Messagebox.OK , Messagebox.INFORMATION, new org.zkoss.zk.ui.event.EventListener<Event>() {
            public void onEvent(Event event) throws Exception {
                if (event.getName().equals("onOK")) {
                    logout();
                }
            }
        });
    }

    @Command
    public void saveEmployee(){
        if(selectedProfiles.size()==0){
            Messagebox.show("Select at least a Profile (Select at least the Default Profile).", "Warning", Messagebox.OK , Messagebox.ERROR);
            return;
        }
        int d = departments.indexOf(selectedDepartment);
        int r = roles.indexOf(selectedRole);
        employee.setDepartment(departmentsDB.get(d));
        employee.setRole(rolesDB.get(r));

        Set<Profile> selectedProfiles = new HashSet<>();
        for(int i=0; i< this.selectedProfiles.size(); i++){
            String selectedProfile = this.selectedProfiles.get(i);
            int p = profiles.indexOf(selectedProfile);
            Profile profile = profilesDB.get(p);
            selectedProfiles.add(profile);
        }
        employee.setProfiles(selectedProfiles);

        updateProfileBooleans(selectedProfiles);

        if(selectedPrefix1Index!=null){
            if(selectedPrefix1Index.intValue()==-1) {
                Messagebox.show("Select the prefix from the list!", "Error", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }
            else {
                employee.setPersonalPhoneNumberPrefix1(getPrefixes().get(selectedPrefix1Index.intValue()).getCountry());
                employee.setPhoneNumber1(getPrefixes().get(selectedPrefix1Index.intValue()).getCode() + " " + employee.getPersonalPhoneNumber1());
            }
        }
        employee.setPersonalPhoneNumberPrefix1(getPrefixes().get(selectedPrefix1Index.intValue()).getCountry());

        if(selectedPrefix2Index!=null){
            if(selectedPrefix2Index.intValue()==-1 && !employee.getPersonalPhoneNumber2().isEmpty()) {
                Messagebox.show("Select the prefix from the list!", "Error", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }
            else if(selectedPrefix2Index.intValue()!=-1) {
                if(employee.getPersonalPhoneNumber2()==null || employee.getPersonalPhoneNumber2().isEmpty()){
                    Messagebox.show("Insert a Valid Phone Number!", "Error", Messagebox.OK, Messagebox.EXCLAMATION);
                    return;
                }else {
                    employee.setPersonalPhoneNumberPrefix2(getPrefixes().get(selectedPrefix2Index.intValue()).getCountry());
                    employee.setPhoneNumber2(getPrefixes().get(selectedPrefix2Index.intValue()).getCode()+" "+employee.getPersonalPhoneNumber2());
                }
            }else{
                employee.setPersonalPhoneNumberPrefix2(null);
                employee.setPersonalPhoneNumber2(null);
                employee.setPhoneNumber2(null);
            }
        }else if(employee.getPersonalPhoneNumber2()!=null && !employee.getPersonalPhoneNumber2().isEmpty()){
            Messagebox.show("Select the prefix from the list!", "Error", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }

        if(!modify) {
            if(employee.getPassword().isEmpty()){
                Messagebox.show("Password Cannot be Empty!", "Error", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }
            if (!repeatedPassword.equals(employee.getPassword())) {
                Messagebox.show("Password Mismatch!", "Error", Messagebox.OK, Messagebox.EXCLAMATION);
                repeatedPassword = "";
                return;
            }
            employee.setPassword(service.MD5(employee.getPassword()));
        }

        if(currentEmployee){
            Employee employee = (Employee) Sessions.getCurrent().getAttribute("employee");
            Sessions.getCurrent().setAttribute("employee",employee);
        }
        try {
            if(employee.getId()==null){
                LeaveBalance balance = new LeaveBalance();
                balance.setValue(21);
                balance.setEmployee(employee);
                leaveService.updateLeaveBalance(balance);
            }
            service.saveEmployee(employee);
            Messagebox.show("OK! Do you want to view all the Employees?", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, new org.zkoss.zk.ui.event.EventListener<Event>() {
                public void onEvent(Event event) throws Exception {
                    if (event.getName().equals("onOK")) {
                        Executions.sendRedirect("/pages/administration/employees/viewEmployees.zul");
                    }
                }
            });
        }catch (PersistenceException persistenceException){
            Messagebox.show("Employee with same Id already exits", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION);
        }
    }

    public Validator getEmployeeNumberValidator(){
        return new AbstractValidator() {
            public void validate(ValidationContext ctx) {
                String employeeNumber = (String)ctx.getProperty().getValue();
                if(employeeNumber!=null && !employeeNumber.equals(employee.getEmployeeNumber()) && service.findEmployeeByEmployeeID((String)ctx.getProperty().getValue())!=null)
                    addInvalidMessage(ctx, "employeeID","Employee Number "+(String)ctx.getProperty().getValue()+" already in use.");
            }
        };
    }

    public Validator getEmail2Validator(){
        return new AbstractValidator() {
            public void validate(ValidationContext ctx) {
                String email = (String) ctx.getProperty().getValue();
                if(email!=null && !email.isEmpty() && !email.matches(".+@.+\\.[a-z]+")){
                    addInvalidMessage(ctx, "email2","Please enter a valid e-mail address");
                }
            }
        };
    }

    public Validator getOfficePhoneValidator(){
        return new AbstractValidator() {
            public void validate(ValidationContext ctx) {
                String phone = (String) ctx.getProperty().getValue();
                if(phone!=null && !phone.isEmpty() && !phone.matches("[0-9]{3}")){
                    addInvalidMessage(ctx, "officePhone","Please enter a valid office phone number (3 digits)");
                }
            }
        };
    }

    public Validator getPhone2Validator(){
        return new AbstractValidator() {
            public void validate(ValidationContext ctx) {
                String phone = (String) ctx.getProperty().getValue();
                if(phone!=null && !phone.isEmpty() && !phone.matches("[0-9]+")){
                    addInvalidMessage(ctx, "phone2","Please enter a valid phone number");
                }
            }
        };
    }

    public Validator getEmail3Validator(){
        return new AbstractValidator() {
            public void validate(ValidationContext ctx) {
                String email = (String) ctx.getProperty().getValue();
                if(email!=null && !email.isEmpty() && !email.matches(".+@.+\\.[a-z]+")){
                    addInvalidMessage(ctx, "email3","Please enter a valid e-mail address");
                }
            }
        };
    }

    private void updateProfileBooleans(Set<Profile> selectedProfiles){
        boolean checkIsLeaveManager = false;
        boolean checkIsFinanceLeaveManager = false;
        boolean checkIsHRLeaveManager = false;
        boolean checkIsTicketLeaveManager = false;

        for (Profile p : selectedProfiles){
            if (p.getName().equals("LM"))
                checkIsLeaveManager = true;
            if (p.getName().equals("FLM"))
                checkIsFinanceLeaveManager = true;
            if (p.getName().equals("HRLM"))
                checkIsHRLeaveManager = true;
            if (p.getName().equals("TLM"))
                checkIsTicketLeaveManager = true;
        }

        if (checkIsLeaveManager)
            employee.setLeaveManager(true);
        else employee.setLeaveManager(false);
        if (checkIsFinanceLeaveManager)
            employee.setFinanceLeaveManager(true);
        else employee.setFinanceLeaveManager(false);
        if (checkIsHRLeaveManager)
            employee.setHRLeaveManager(true);
        else employee.setHRLeaveManager(false);
        if (checkIsTicketLeaveManager)
            employee.setTicketLeaveManager(true);
        else employee.setTicketLeaveManager(false);
    }

    private void initializeCombos(){
        nationalities = new ArrayList<>(Arrays.asList("Afghan","Albanian","Algerian","American","Andorran","Angolan","Antiguans","Argentinean","Armenian","Australian","Austrian","Azerbaijani","Bahamian","Bahraini","Bangladeshi","Barbadian","Barbudans","Batswana","Belarusian","Belgian","Belizean","Beninese","Bhutanese","Bolivian","Bosnian","Brazilian","British","Bruneian","Bulgarian","Burkinabe","Burmese","Burundian","Cambodian","Cameroonian","Canadian","Cape Verdean","Central African","Chadian","Chilean","Chinese","Colombian","Comoran","Congolese","Congolese","Costa Rican","Croatian","Cuban","Cypriot","Czech","Danish","Djibouti","Dominican","Dominican","Dutch","Dutchman","Dutchwoman","East Timorese","Ecuadorean","Egyptian","Emirian","Equatorial Guinean","Eritrean","Estonian","Ethiopian","Fijian","Filipino","Finnish","French","Gabonese","Gambian","Georgian","German","Ghanaian","Greek","Grenadian","Guatemalan","Guinea-Bissauan","Guinean","Guyanese","Haitian","Herzegovinian","Honduran","Hungarian","I-Kiribati","Icelander","Indian","Indonesian","Iranian","Iraqi","Irish","Irish","Israeli","Italian","Ivorian","Jamaican","Japanese","Jordanian","Kazakhstani","Kenyan","Kittian and Nevisian","Kuwaiti","Kyrgyz","Laotian","Latvian","Lebanese","Liberian","Libyan","Liechtensteiner","Lithuanian","Luxembourger","Macedonian","Malagasy","Malawian","Malaysian","Maldivan","Malian","Maltese","Marshallese","Mauritanian","Mauritian","Mexican","Micronesian","Moldovan","Monacan","Mongolian","Moroccan","Mosotho","Motswana","Mozambican","Namibian","Nauruan","Nepalese","Netherlander","New Zealander","Ni-Vanuatu","Nicaraguan","Nigerian","Nigerien","North Korean","Northern Irish","Norwegian","Omani","Pakistani","Palauan","Panamanian","Papua New Guinean","Paraguayan","Peruvian","Polish","Portuguese","Qatari","Romanian","Russian","Rwandan","Saint Lucian","Salvadoran","Samoan","San Marinese","Sao Tomean","Saudi","Scottish","Senegalese","Serbian","Seychellois","Sierra Leonean","Singaporean","Slovakian","Slovenian","Solomon Islander","Somali","South African","South Korean","Spanish","Sri Lankan","Sudanese","Surinamer","Swazi","Swedish","Swiss","Syrian","Taiwanese","Tajik","Tanzanian","Thai","Togolese","Tongan","Trinidadian or Tobagonian","Tunisian","Turkish","Tuvaluan","Ugandan","Ukrainian","Uruguayan","Uzbekistani","Venezuelan","Vietnamese","Welsh","Welsh","Yemenite","Zambian","Zimbabwean"));
    }

    public void setRepeatedPassword(String repeatedPassword){
        this.repeatedPassword=repeatedPassword;
    }

    public String getRepeatedPassword(){
        return repeatedPassword;
    }

    public List<String> getNationalities() {
        return nationalities;
    }

    public void setNationalities(List<String> nationalities) {
        this.nationalities = nationalities;
    }

    @Override
    public Employee getEmployee() {
        return employee;
    }

    @Override
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<Role> getRolesDB() {
        return rolesDB;
    }

    public void setRolesDB(List<Role> rolesDB) {
        this.rolesDB = rolesDB;
    }

    public List<String> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<String> profiles) {
        this.profiles = profiles;
    }

    public List<Profile> getProfilesDB() {
        return profilesDB;
    }

    public void setProfilesDB(List<Profile> profilesDB) {
        this.profilesDB = profilesDB;
    }

    public List<String> getDepartments() {
        return departments;
    }

    public void setDepartments(List<String> departments) {
        this.departments = departments;
    }

    public List<Department> getDepartmentsDB() {
        return departmentsDB;
    }

    public void setDepartmentsDB(List<Department> departmentsDB) {
        this.departmentsDB = departmentsDB;
    }

    public String getSelectedDepartment() {
        return selectedDepartment;
    }

    public void setSelectedDepartment(String selectedDepartment) {
        this.selectedDepartment = selectedDepartment;
    }

    public String getSelectedRole() {
        return selectedRole;
    }

    public void setSelectedRole(String selectedRole) {
        this.selectedRole = selectedRole;
    }

    public List<String> getSelectedProfiles() {
        return selectedProfiles;
    }

    public void setSelectedProfiles(List<String> selectedProfiles) {
        this.selectedProfiles = selectedProfiles;
    }

    public boolean isCurrentEmployee() {
        return currentEmployee;
    }

    public void setCurrentEmployee(boolean currentEmployee) {
        this.currentEmployee = currentEmployee;
    }

    public boolean isModify() {
        return modify;
    }

    public void setModify(boolean modify) {
        this.modify = modify;
    }

    public String getBirthConstraint() {
        return birthConstraint;
    }

    public void setBirthConstraint(String birthConstraint) {
        this.birthConstraint = birthConstraint;
    }

    public Integer getSelectedPrefix1Index() {
        return selectedPrefix1Index;
    }

    public void setSelectedPrefix1Index(Integer selectedPrefix1Index) {
        this.selectedPrefix1Index = selectedPrefix1Index;
    }

    public Integer getSelectedPrefix2Index() {
        return selectedPrefix2Index;
    }

    public void setSelectedPrefix2Index(Integer selectedPrefix2Index) {
        this.selectedPrefix2Index = selectedPrefix2Index;
    }

    public String getSelectedPrefix1() {
        return selectedPrefix1;
    }

    public void setSelectedPrefix1(String selectedPrefix1) {
        this.selectedPrefix1 = selectedPrefix1;
    }

    public String getSelectedPrefix2() {
        return selectedPrefix2;
    }

    public void setSelectedPrefix2(String selectedPrefix2) {
        this.selectedPrefix2 = selectedPrefix2;
    }
}
