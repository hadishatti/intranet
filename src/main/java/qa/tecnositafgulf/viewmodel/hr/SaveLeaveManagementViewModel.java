package qa.tecnositafgulf.viewmodel.hr;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.sys.PageCtrl;
import org.zkoss.zul.Messagebox;
import qa.tecnositafgulf.config.MyProperties;
import qa.tecnositafgulf.dao.administration.profiles.ProfileDao;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.administration.Profile;
import qa.tecnositafgulf.model.leaves.LeaveBalance;
import qa.tecnositafgulf.service.AdministrationService;
import qa.tecnositafgulf.service.LeaveRequestService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hadi on 12/31/17.
 */
public class SaveLeaveManagementViewModel extends IntranetVM {
    private Employee employee;
    private AdministrationService service;
    private LeaveRequestService leaveService;
    private List<Employee> leaveManagersFromDB;
    private List<Employee> HRLeaveManagersFromDB;
    private List<Employee> financeLeaveManagersFromDB;
    private List<Employee> ticketLeaveManagersFromDB;
    private List<String> leaveManagers;
    private List<String> HRLeaveManagers;
    private List<String> financeLeaveManagers;
    private List<String> ticketLeaveManagers;
    private String selectedLeaveManager;
    private String selectedHRLeaveManager;
    private String selectedFinanceLeaveManager;
    private String selectedTicketLeaveManager;
    private boolean leaveManager;
    private boolean HRLeaveManager;
    private boolean financeLeaveManager;
    private boolean ticketLeaveManager;
    private List<Profile> profilesDB;
    private int currentAnnualLeaveBalance;
    private int currentCasualLeaveBalance;
    private int currentEmergencyLeaveBalance;
    private int currentSickLeaveBalance;

    private ProfileDao profileDao;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("employeeToModify") Employee employeeToModify) {
        init();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        service = context.getBean(AdministrationService.class);
        leaveService = context.getBean(LeaveRequestService.class);
        employee = employeeToModify;
        leaveManager = authenticationService.isProfilePresent(employee, "LM");
        HRLeaveManager = authenticationService.isProfilePresent(employee, "HRLM");
        financeLeaveManager = authenticationService.isProfilePresent(employee, "FLM");
        ticketLeaveManager = authenticationService.isProfilePresent(employee, "TLM");
        leaveManagers = new ArrayList<String>();
        HRLeaveManagers = new ArrayList<String>();
        financeLeaveManagers = new ArrayList<String>();
        ticketLeaveManagers = new ArrayList<String>();
        leaveManagersFromDB = service.listLeaveManagers();
        HRLeaveManagersFromDB = service.listHRLeaveManagers();
        financeLeaveManagersFromDB = service.listFinanceLeaveManagers();
        ticketLeaveManagersFromDB = service.listTicketLeaveManagers();
        profilesDB = service.listProfiles();
        for(Employee employee : leaveManagersFromDB)
            this.leaveManagers.add(employee.toString());
        this.leaveManagers.add("NO MANAGER");
        for(Employee employee : HRLeaveManagersFromDB)
            this.HRLeaveManagers.add(employee.toString());
        this.HRLeaveManagers.add("NO MANAGER");
        for(Employee employee : financeLeaveManagersFromDB)
            this.financeLeaveManagers.add(employee.toString());
        this.financeLeaveManagers.add("NO MANAGER");
        for(Employee employee : ticketLeaveManagersFromDB)
            this.ticketLeaveManagers.add(employee.toString());
        this.ticketLeaveManagers.add("NO MANAGER");


        selectedLeaveManager = (employeeToModify.getLeaveManagerEmployee()!=null)?employeeToModify.getLeaveManagerEmployee().toString():this.leaveManagers.get(0);

        selectedHRLeaveManager = (employeeToModify.getHRLeaveManagerEmployee()!=null)?employeeToModify.getHRLeaveManagerEmployee().toString():this.HRLeaveManagers.get(0);

        selectedFinanceLeaveManager = (employeeToModify.getFinanceLeaveManagerEmployee()!=null)?employeeToModify.getFinanceLeaveManagerEmployee().toString():this.financeLeaveManagers.get(0);

        selectedTicketLeaveManager = (employeeToModify.getTicketLeaveManagerEmployee()!=null)?employeeToModify.getTicketLeaveManagerEmployee().toString():this.ticketLeaveManagers.get(0);

        currentAnnualLeaveBalance = leaveService.getLeaveBalance(employee,"Annual");
        currentCasualLeaveBalance = leaveService.getLeaveBalance(employee,"Casual");
        currentEmergencyLeaveBalance = leaveService.getLeaveBalance(employee,"Emergency");
        currentSickLeaveBalance = leaveService.getLeaveBalance(employee,"Sick");

        addCommonTags((PageCtrl) view.getPage());
    }

    @Command
    @NotifyChange("employee")
    public void saveEmployee() {
        if (leaveManager){
            employee.getProfiles().add(getProfileByName("LM"));
            employee.setLeaveManager(true);
        }else {
            if (employee.getProfiles().contains(getProfileByName("LM"))) {
                employee.getProfiles().remove(getProfileByName("LM"));
                employee.setLeaveManager(false);
            }
        }
        if (HRLeaveManager){
            employee.getProfiles().add(getProfileByName("HRLM"));
            employee.setHRLeaveManager(true);
        }else {
            if (employee.getProfiles().contains(getProfileByName("HRLM"))) {
                employee.getProfiles().remove(getProfileByName("HRLM"));
                employee.setHRLeaveManager(false);
            }
        }
        if (financeLeaveManager){
            employee.getProfiles().add(getProfileByName("FLM"));
            employee.setFinanceLeaveManager(true);
        }else {
            if (employee.getProfiles().contains(getProfileByName("FLM"))) {
                employee.getProfiles().remove(getProfileByName("FLM"));
                employee.setFinanceLeaveManager(false);
            }
        }
        if (ticketLeaveManager){
            employee.getProfiles().add(getProfileByName("TLM"));
            employee.setTicketLeaveManager(true);
        }else {
            if (employee.getProfiles().contains(getProfileByName("TLM"))) {
                employee.getProfiles().remove(getProfileByName("TLM"));
                employee.setTicketLeaveManager(false);
            }
        }
        if(!selectedLeaveManager.equals("NO MANAGER"))
            employee.setLeaveManagerEmployee(leaveManagersFromDB.get(leaveManagers.indexOf(selectedLeaveManager)));
        else
            employee.setLeaveManagerEmployee(null);

        if(!selectedHRLeaveManager.equals("NO MANAGER"))
            employee.setHRLeaveManagerEmployee(HRLeaveManagersFromDB.get(HRLeaveManagers.indexOf(selectedHRLeaveManager)));
        else
            employee.setHRLeaveManagerEmployee(null);

        if(!selectedFinanceLeaveManager.equals("NO MANAGER"))
            employee.setFinanceLeaveManagerEmployee(financeLeaveManagersFromDB.get(financeLeaveManagers.indexOf(selectedFinanceLeaveManager)));
        else
        employee.setFinanceLeaveManagerEmployee(null);

        if(!selectedTicketLeaveManager.equals("NO MANAGER"))
            employee.setTicketLeaveManagerEmployee(ticketLeaveManagersFromDB.get(ticketLeaveManagers.indexOf(selectedTicketLeaveManager)));
        else
            employee.setTicketLeaveManagerEmployee(null);

        LeaveBalance annualBalance = new LeaveBalance();
        annualBalance.setEmployee(employee);
        annualBalance.setType("Annual");
        annualBalance.setValue(currentAnnualLeaveBalance);
        leaveService.updateLeaveBalance(annualBalance);
        LeaveBalance causalBalance = new LeaveBalance();
        causalBalance.setEmployee(employee);
        causalBalance.setType("Casual");
        causalBalance.setValue(currentCasualLeaveBalance);
        leaveService.updateLeaveBalance(causalBalance);
        LeaveBalance emergencyBalance = new LeaveBalance();
        emergencyBalance.setEmployee(employee);
        emergencyBalance.setType("Emergency");
        emergencyBalance.setValue(currentEmergencyLeaveBalance);
        leaveService.updateLeaveBalance(emergencyBalance);
        LeaveBalance sickBalance = new LeaveBalance();
        sickBalance.setEmployee(employee);
        sickBalance.setType("Sick");
        sickBalance.setValue(currentSickLeaveBalance);
        leaveService.updateLeaveBalance(sickBalance);

        service.saveEmployee(employee);
        Messagebox.show("OK! Do you want to view the Leave Configurations for all the Employees?", "Warning", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
            public void onEvent(Event event) throws Exception {
                if (event.getName().equals("onOK")) {
                    Executions.sendRedirect("/pages/hr/viewLeaveManagement.zul");
                }
            }
        });
    }

    @Command
    @NotifyChange({"currentAnnualLeaveBalance"})
    public void resetAnnualLeaveBalance(){
        final int defaultAnnualBalance= Integer.parseInt(MyProperties.getInstance().getProperty("defaultAnnualBalance"));
        boolean detract = false;
        if(currentAnnualLeaveBalance<0){
            Messagebox.show("Current Annual Leave Balance is Negative ("+currentAnnualLeaveBalance+"). The reset balance will be detracted to "+(defaultAnnualBalance+currentAnnualLeaveBalance)+" (Default Balance - Current Negative Balance).", "Warning", Messagebox.OK, Messagebox.INFORMATION);
            currentAnnualLeaveBalance+=defaultAnnualBalance;
        }else{
            currentAnnualLeaveBalance=defaultAnnualBalance;
        }
    }

    @Command
    @NotifyChange({"currentCasualLeaveBalance"})
    public void resetCasualLeaveBalance(){
        final int defaultCasualBalance= Integer.parseInt(MyProperties.getInstance().getProperty("defaultCasualBalance"));
        if(currentCasualLeaveBalance<0){
            Messagebox.show("Current Casual Leave Balance is negative ("+currentCasualLeaveBalance+"). The reset balance will be detracted to "+(defaultCasualBalance+currentCasualLeaveBalance)+" (Default Balance - Current Negative Balance).", "Warning", Messagebox.OK, Messagebox.INFORMATION);
            currentAnnualLeaveBalance+=defaultCasualBalance;
        }else{
            currentAnnualLeaveBalance=defaultCasualBalance;
        }
    }

    @Command
    @NotifyChange({"currentEmergencyLeaveBalance"})
    public void resetEmergencyLeaveBalance(){
        final int defaultEmergencyBalance= Integer.parseInt(MyProperties.getInstance().getProperty("defaultEmergencyBalance"));
        if(currentEmergencyLeaveBalance<0){
            Messagebox.show("Current Emergency Leave Balance is negative ("+currentEmergencyLeaveBalance+"). The reset Balance will be detracted to "+(defaultEmergencyBalance+currentEmergencyLeaveBalance)+" (Default Balance - Current Negative Balance).", "Warning", Messagebox.OK, Messagebox.INFORMATION);
            currentEmergencyLeaveBalance+=defaultEmergencyBalance;
        }else{
            currentEmergencyLeaveBalance=defaultEmergencyBalance;
        }
    }

    @Command
    @NotifyChange({"currentSickLeaveBalance"})
    public void resetSickLeaveBalance(){
        final int defaultSickBalance= Integer.parseInt(MyProperties.getInstance().getProperty("defaultEmergencyBalance"));
        if(currentSickLeaveBalance<0){
            Messagebox.show("Current Sick Leave Balance is negative ("+currentSickLeaveBalance+"). The reset Balance will be detracted to "+(defaultSickBalance+currentSickLeaveBalance)+" (Default Balance - Current Negative Balance).", "Warning", Messagebox.OK, Messagebox.INFORMATION);
            currentSickLeaveBalance+=defaultSickBalance;
        }else{
            currentSickLeaveBalance=defaultSickBalance;
        }
    }

    private Profile getProfileByName(String name){
        for (Profile p : profilesDB){
            if (p.getName().equals(name))
                return p;
        }
        return null;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }


    public List<String> getLeaveManagers() {
        return leaveManagers;
    }

    public void setLeaveManagers(List<String> leaveManagers) {
        this.leaveManagers = leaveManagers;
    }

    public List<String> getHRLeaveManagers() {
        return HRLeaveManagers;
    }

    public void setHRLeaveManagers(List<String> HRLeaveManagers) {
        this.HRLeaveManagers = HRLeaveManagers;
    }

    public List<String> getFinanceLeaveManagers() {
        return financeLeaveManagers;
    }

    public void setFinanceLeaveManagers(List<String> financeLeaveManagers) {
        this.financeLeaveManagers = financeLeaveManagers;
    }

    public List<String> getTicketLeaveManagers() {
        return ticketLeaveManagers;
    }

    public void setTicketLeaveManagers(List<String> ticketLeaveManagers) {
        this.ticketLeaveManagers = ticketLeaveManagers;
    }

    public String getSelectedLeaveManager() {
        return selectedLeaveManager;
    }

    public void setSelectedLeaveManager(String selectedLeaveManager) {
        this.selectedLeaveManager = selectedLeaveManager;
    }

    public String getSelectedHRLeaveManager() {
        return selectedHRLeaveManager;
    }

    public void setSelectedHRLeaveManager(String selectedHRLeaveManager) {
        this.selectedHRLeaveManager = selectedHRLeaveManager;
    }

    public String getSelectedFinanceLeaveManager() {
        return selectedFinanceLeaveManager;
    }

    public void setSelectedFinanceLeaveManager(String selectedFinanceLeaveManager) {
        this.selectedFinanceLeaveManager = selectedFinanceLeaveManager;
    }

    public String getSelectedTicketLeaveManager() {
        return selectedTicketLeaveManager;
    }

    public void setSelectedTicketLeaveManager(String selectedTicketLeaveManager) {
        this.selectedTicketLeaveManager = selectedTicketLeaveManager;
    }

    public boolean isTicketLeaveManager() {
        return ticketLeaveManager;
    }

    public void setTicketLeaveManager(boolean ticketLeaveManager) {
        this.ticketLeaveManager = ticketLeaveManager;
    }

    public boolean isLeaveManager() {
        return leaveManager;
    }

    public void setLeaveManager(boolean leaveManager) {
        this.leaveManager = leaveManager;
    }

    public boolean isHRLeaveManager() {
        return HRLeaveManager;
    }

    public void setHRLeaveManager(boolean HRLeaveManager) {
        this.HRLeaveManager = HRLeaveManager;
    }

    public boolean isFinanceLeaveManager() {
        return financeLeaveManager;
    }

    public void setFinanceLeaveManager(boolean financeLeaveManager) {
        this.financeLeaveManager = financeLeaveManager;
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
}
