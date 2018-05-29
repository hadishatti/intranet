package qa.tecnositafgulf.viewmodel;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.web.fn.ServletFns;
import org.zkoss.web.servlet.Servlets;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.DesktopUnavailableException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Window;
import qa.tecnositafgulf.common.PhonePrefix;
import qa.tecnositafgulf.config.MyProperties;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.administration.Role;
import qa.tecnositafgulf.service.AuthenticationService;
import qa.tecnositafgulf.service.CompanyInfoService;
import qa.tecnositafgulf.spring.config.AppConfig;

import javax.servlet.ServletRequest;
import java.util.*;

/**
 * Created by hadi on 12/20/17.
 */
public class IntranetVM {

    private boolean mobile;
    private String orient = "landscape";
    public static final  String EMP_SM_ROLE = "SM";
    private int width;
    private int height;
    private String css;

    private boolean floatingMenuVisible = true;
    private boolean isMobile = false;
    private String orientation;
    private String windowMode = "embedded";
    private String menuAreaWidth= "200px";

    private ServletRequest request = ServletFns.getCurrentRequest();
    private Employee employee;
    private boolean isAdministrator;
    private boolean isPublisher;
    private boolean isHRLeaveManager;
    private boolean isLeaveManager;
    private boolean isFinanceLeaveManager;
    private boolean isTicketLeaveManager;
    private boolean isStoreManager;
    private Timer timer;
    private Desktop desktop;
    private int notifications, managerNotifications;
    private boolean readManagerNotifications = false;
    private String imagePath;
    private List<qa.tecnositafgulf.model.companyInfo.Project> activeProjects;
    private List<PhonePrefix> prefixes;

    public AuthenticationService authenticationService;
    private CompanyInfoService companyInfoService;


    @Init
    public void init() {
        // Detect if client is mobile (such as Android or iOS devices)
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        authenticationService = context.getBean(AuthenticationService.class);
        companyInfoService = context.getBean(CompanyInfoService.class);
        mobile = Servlets.getBrowser(request, "mobile") != null;

        // For flexible design
        if (isMobile){
            toFloatingMenu();
        }
        css = "/style/intranet.css";
        isAdministrator = authenticationService.isAdministrator();
        isPublisher = authenticationService.isPublisher();
        desktop = Executions.getCurrent().getDesktop();
        employee = (Employee) Sessions.getCurrent().getAttribute("employee");
//        employee = user.getEmployee();
        if (employee != null && !desktop.isServerPushEnabled()) {
            desktop.enableServerPush(true);
            activateNotifications();
        }
//        if (employee != null && (authenticationService.isLeaveManager() || authenticationService.isHRApprover() || authenticationService.isFinanceApprover() || authenticationService.isTicketManager())) {
//            readManagerNotifications = true;
//        }

        if (employee != null && (authenticationService.isProfilePresent(employee, "LM") || authenticationService.isProfilePresent(employee, "HRLM") || authenticationService.isProfilePresent(employee, "FLM") || authenticationService.isProfilePresent(employee, "TLM"))) {
            readManagerNotifications = true;
        }

        isLeaveManager = authenticationService.isProfilePresent(employee, "LM");
        isHRLeaveManager = authenticationService.isProfilePresent(employee, "HRLM");
        isFinanceLeaveManager = authenticationService.isProfilePresent(employee, "FLM");
        isTicketLeaveManager = authenticationService.isProfilePresent(employee, "TLM");
        //For Role to assign for Store Manager
        if(isAdministrator) {
            setStoreManager(Boolean.TRUE);
        }else{
            Role role = employee.getRole();
            if (role.getName().equals(EMP_SM_ROLE)) {
                setStoreManager(Boolean.TRUE);
            } else
                setStoreManager(Boolean.FALSE);
        }
        prefixes = new ArrayList<>(Arrays.asList(new PhonePrefix("Afghanistan","+93"), new PhonePrefix("Aland Islands","+358"), new PhonePrefix("Albania","+355"), new PhonePrefix("Algeria","+213"), new PhonePrefix("American Samoa","+1"), new PhonePrefix("Andorra","+376"), new PhonePrefix("Angola","+244"), new PhonePrefix("Anguilla","+1"), new PhonePrefix("Antarctica","+672"), new PhonePrefix("Antigua and Barbuda","+1"), new PhonePrefix("Argentina","+54"), new PhonePrefix("Armenia","+374"), new PhonePrefix("Aruba","+297"), new PhonePrefix("Ascension Island","+247"), new PhonePrefix("Australia","+61"), new PhonePrefix("Austria","+43"), new PhonePrefix("Azerbaijan","+994"), new PhonePrefix("Bahamas","+1"), new PhonePrefix("Bahrain","+973"), new PhonePrefix("Bangladesh","+880"), new PhonePrefix("Barbados","+1"), new PhonePrefix("Belarus","+375"), new PhonePrefix("Belgium","+32"), new PhonePrefix("Belize","+501"), new PhonePrefix("Benin","+229"), new PhonePrefix("Bermuda","+1"), new PhonePrefix("Bhutan","+975"), new PhonePrefix("Bolivia","+591"), new PhonePrefix("Bosnia and Herzegovina","+387"), new PhonePrefix("Botswana","+267"), new PhonePrefix("Brazil","+55"), new PhonePrefix("British Indian Ocean Territory","+246"), new PhonePrefix("Brunei Darussalam","+673"), new PhonePrefix("Burkina Faso","+226"), new PhonePrefix("Burundi","+257"), new PhonePrefix("Cambodia","+855"), new PhonePrefix("Cameroon","+237"), new PhonePrefix("Canada","+1"), new PhonePrefix("Canary Islands","+34"), new PhonePrefix("Cape Verde","+238"), new PhonePrefix("Cayman Islands","+1"), new PhonePrefix("Central African Republic","+236"), new PhonePrefix("Ceuta and Melilla","+34"), new PhonePrefix("Chad","+235"), new PhonePrefix("Chile","+56"), new PhonePrefix("China","+86"), new PhonePrefix("Christmas Island","+61"), new PhonePrefix("Cocos (Keeling) Island","+61"), new PhonePrefix("Colombia","+57"), new PhonePrefix("Comoros","+269"), new PhonePrefix("Congo","+242"), new PhonePrefix("Congo - Democratic Republic of the","+243"), new PhonePrefix("Cook Islands","+682"), new PhonePrefix("Costa Rica","+506"), new PhonePrefix("Côte D'Ivoire","+225"), new PhonePrefix("Croatia","+385"), new PhonePrefix("Cuba","+53"), new PhonePrefix("Cyprus - Republic of","+357"), new PhonePrefix("Cyprus - Turkish Republic of Northern","+90"), new PhonePrefix("Czech Republic","+420"), new PhonePrefix("Denmark","+45"), new PhonePrefix("Djibouti","+253"), new PhonePrefix("Dominica","+1"), new PhonePrefix("Dominican Republic","+1"), new PhonePrefix("Ecuador","+593"), new PhonePrefix("Egypt","+20"), new PhonePrefix("El Salvador","+503"), new PhonePrefix("Equatorial Guinea","+240"), new PhonePrefix("Eritrea","+291"), new PhonePrefix("Estonia","+372"), new PhonePrefix("Ethiopia","+251"), new PhonePrefix("Falkland Islands","+500"), new PhonePrefix("Faroe Islands","+298"), new PhonePrefix("Fiji","+679"), new PhonePrefix("Finland","+358"), new PhonePrefix("France","+33"), new PhonePrefix("French Guiana","+594"), new PhonePrefix("French Polynesia","+689"), new PhonePrefix("French Southern and Antarctic Lands","+262"), new PhonePrefix("Gabon","+241"), new PhonePrefix("Gambia","+220"), new PhonePrefix("Georgia","+995"), new PhonePrefix("Germany","+49"), new PhonePrefix("Ghana","+233"), new PhonePrefix("Gibraltar","+350"), new PhonePrefix("Greece","+30"), new PhonePrefix("Greenland","+299"), new PhonePrefix("Grenada","+1"), new PhonePrefix("Guadeloupe","+590"), new PhonePrefix("Guam","+1"), new PhonePrefix("Guatemala","+502"), new PhonePrefix("Guernsey","+44"), new PhonePrefix("Guinea","+224"), new PhonePrefix("Guinea-Bissau","+245"), new PhonePrefix("Guyana","+592"), new PhonePrefix("Haiti","+509"), new PhonePrefix("Holy See (Vatican City State)","+39"), new PhonePrefix("Honduras","+504"), new PhonePrefix("Hong Kong","+852"), new PhonePrefix("Hungary","+36"), new PhonePrefix("Iceland","+354"), new PhonePrefix("India","+91"), new PhonePrefix("Indonesia","+62"), new PhonePrefix("Iran","+98"), new PhonePrefix("Iraq","+964"), new PhonePrefix("Ireland","+353"), new PhonePrefix("Isle of Man","+44"), new PhonePrefix("Israel","+972"), new PhonePrefix("Italy","+39"), new PhonePrefix("Jamaica","+1"), new PhonePrefix("Japan","+81"), new PhonePrefix("Jersey","+44"), new PhonePrefix("Jordan","+962"), new PhonePrefix("Kazakhstan","+7"), new PhonePrefix("Kenya","+254"), new PhonePrefix("Kiribati","+686"), new PhonePrefix("Korea - Democratic People's Republic of","+850"), new PhonePrefix("Korea - Republic of","+82"), new PhonePrefix("Kuwait","+965"), new PhonePrefix("Kyrgyz Republic","+996"), new PhonePrefix("Laos","+856"), new PhonePrefix("Latvia","+371"), new PhonePrefix("Lebanon","+961"), new PhonePrefix("Lesotho","+266"), new PhonePrefix("Liberia","+231"), new PhonePrefix("Libya","+218"), new PhonePrefix("Liechtenstein","+423"), new PhonePrefix("Lithuania","+370"), new PhonePrefix("Luxembourg","+352"), new PhonePrefix("Macao","+853"), new PhonePrefix("Macedonia","+389"), new PhonePrefix("Madagascar","+261"), new PhonePrefix("Malawi","+265"), new PhonePrefix("Malaysia","+60"), new PhonePrefix("Maldives","+960"), new PhonePrefix("Mali","+223"), new PhonePrefix("Malta","+356"), new PhonePrefix("Marshall Islands","+692"), new PhonePrefix("Martinique","+596"), new PhonePrefix("Mauritania","+222"), new PhonePrefix("Mauritius","+230"), new PhonePrefix("Mayotte","+269"), new PhonePrefix("Mexico","+52"), new PhonePrefix("Micronesia","+691"), new PhonePrefix("Moldova","+373"), new PhonePrefix("Monaco","+377"), new PhonePrefix("Mongolia","+976"), new PhonePrefix("Montenegro","+382"), new PhonePrefix("Montserrat","+1"), new PhonePrefix("Morocco","+212"), new PhonePrefix("Mozambique","+258"), new PhonePrefix("Myanmar","+95"), new PhonePrefix("Namibia","+264"), new PhonePrefix("Nauru","+674"), new PhonePrefix("Nepal","+977"), new PhonePrefix("Netherlands","+31"), new PhonePrefix("Netherlands Antilles","+599"), new PhonePrefix("New Caledonia","+687"), new PhonePrefix("New Zealand","+64"), new PhonePrefix("Nicaragua","+505"), new PhonePrefix("Niger","+227"), new PhonePrefix("Nigeria","+234"), new PhonePrefix("Niue","+683"), new PhonePrefix("Norfolk Island","+672"), new PhonePrefix("Northern Mariana Islands","+1"), new PhonePrefix("Norway","+47"), new PhonePrefix("Oman","+968"), new PhonePrefix("Pakistan","+92"), new PhonePrefix("Palau","+680"), new PhonePrefix("Palestine","+970"), new PhonePrefix("Panama","+507"), new PhonePrefix("Papua New Guinea","+675"), new PhonePrefix("Paraguay","+595"), new PhonePrefix("Peru","+51"), new PhonePrefix("Philippines","+63"), new PhonePrefix("Pitcairn","+872"), new PhonePrefix("Poland","+48"), new PhonePrefix("Portugal","+351"), new PhonePrefix("Puerto Rico","+1"), new PhonePrefix("Qatar","+974"), new PhonePrefix("Réunion","+262"), new PhonePrefix("Romania","+40"), new PhonePrefix("Russian Federation","+7"), new PhonePrefix("Rwanda","+250"), new PhonePrefix("Saint Helena","+290"), new PhonePrefix("Saint Kitts and Nevis","+1"), new PhonePrefix("Saint Lucia","+1"), new PhonePrefix("Saint Pierre and Miquelon","+508"), new PhonePrefix("Saint Vincent and the Grenadines","+1"), new PhonePrefix("Samoa","+685"), new PhonePrefix("San Marino","+378"), new PhonePrefix("São Tome and Principe","+239"), new PhonePrefix("Saudi Arabia","+966"), new PhonePrefix("Senegal","+221"), new PhonePrefix("Serbia","+381"), new PhonePrefix("Seychelles","+248"), new PhonePrefix("Sierra Leone","+232"), new PhonePrefix("Singapore","+65"), new PhonePrefix("Slovakia","+421"), new PhonePrefix("Slovenia","+386"), new PhonePrefix("Solomon Islands","+677"), new PhonePrefix("Somalia","+252"), new PhonePrefix("Somaliland","+252"), new PhonePrefix("South Africa","+27"), new PhonePrefix("Spain","+34"), new PhonePrefix("Sri Lanka","+94"), new PhonePrefix("Sudan","+249"), new PhonePrefix("Suriname","+597"), new PhonePrefix("Svalbard and Jan Mayen","+47"), new PhonePrefix("Swaziland","+268"), new PhonePrefix("Sweden","+46"), new PhonePrefix("Switzerland","+41"), new PhonePrefix("Syria","+963"), new PhonePrefix("Taiwan","+886"), new PhonePrefix("Tajikistan","+992"), new PhonePrefix("Tanzania","+255"), new PhonePrefix("Thailand","+66"), new PhonePrefix("Timor-Leste","+670"), new PhonePrefix("Togo","+228"), new PhonePrefix("Tokelau","+690"), new PhonePrefix("Tonga","+676"), new PhonePrefix("Trinidad and Tobago","+1"), new PhonePrefix("Tristan da Cunha","+290"), new PhonePrefix("Tunisia","+216"), new PhonePrefix("Turkey","+90"), new PhonePrefix("Turkmenistan","+993"), new PhonePrefix("Turks and Caicos Islands","+1"), new PhonePrefix("Tuvalu","+688"), new PhonePrefix("Uganda","+256"), new PhonePrefix("Ukraine","+380"), new PhonePrefix("United Arab Emirates","+971"), new PhonePrefix("United Kingdom","+44"), new PhonePrefix("United States","+1"), new PhonePrefix("United States Minor Outlying Islands","+699"), new PhonePrefix("Uruguay","+598"), new PhonePrefix("Uzbekistan","+998"), new PhonePrefix("Vanuatu","+678"), new PhonePrefix("Venezuela","+58"), new PhonePrefix("Viet Nam","+84"), new PhonePrefix("Virgin Islands - British","+1"), new PhonePrefix("Virgin Islands - U.S.","+1"), new PhonePrefix("Wallis and Futuna Islands","+681"), new PhonePrefix("Western Sahara","+212"), new PhonePrefix("Yemen","+967"), new PhonePrefix("Zambia","+260"), new PhonePrefix("Zimbabwe","+263")));

        activeProjects = companyInfoService.getLatestActiveProjects(5);
    }

    private void toFloatingMenu(){
        floatingMenuVisible = false;
        windowMode = "overlapped";
        menuAreaWidth = "0px";
    }

    private void toFixedMenu(){
        floatingMenuVisible = true;
        windowMode = "embedded";
        menuAreaWidth = "200px";
    }

    @Command
    @NotifyChange("floatingMenuVisible")
    public void toggleMenu(){
        floatingMenuVisible = !floatingMenuVisible;
    }

    @Command
    @NotifyChange({"windowMode","menuAreaWidth","floatingMenuVisible"})
    public void updateClientInfo(@BindingParam("orientation") String orientation, @BindingParam("width")int width ){
        if (isMobile){
            if (!orientation.equals(this.orientation)){
                this.orientation = orientation;
                if (orientation.equals("protrait") || width < 800){
                    toFloatingMenu();
                }else{
                    toFixedMenu();
                }
            }
        }
    }

    public void activateNotifications() {
        if (timer != null)
            timer.cancel();
        timer = new Timer();
        timer.schedule(readNotifications(), 0, 1000);
    }

    @Command
    public void logout() {
        authenticationService.logout();
        Executions.sendRedirect("/pages/login/login.zul");
    }

    @Command
    public void changePassword() {
        final Map<String, Employee> params = new HashMap<String, Employee>();
        Employee employee = (Employee) Sessions.getCurrent().getAttribute("employee");
        params.put("employeeToModify", employee);
        ((Window) Executions.getCurrent().createComponents("/pages/administration/employees/changePassword.zul", null, params)).doModal();
    }

    @Command
    public void onClientInfo(@BindingParam("width") int width, @BindingParam("height") int height) {
        this.width = width;
        this.height = height;
    }

    @Command
    public void chat() {
        Window window = (Window) Executions.createComponents(
                "/pages/include/chat.zul", null, null);
        window.doModal();
    }


    public String getImagePath(){
       return MyProperties.getInstance().getImagePath();
    }

    public boolean isStoreManager() {
        return isStoreManager;
    }

    public void setStoreManager(boolean storeManager) {
        isStoreManager = storeManager;
    }

    public boolean isMobile() {
        return mobile;
    }

    public void setMobile(boolean mobile) {
        this.mobile = mobile;
    }

    public String getOrient() {
        return orient;
    }

    public void setOrient(String orient) {
        this.orient = orient;
    }

    public ServletRequest getRequest() {
        return request;
    }

    public void setRequest(ServletRequest request) {
        this.request = request;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isAdministrator() {
        return isAdministrator;
    }

    public void setAdministrator(boolean administrator) {
        isAdministrator = administrator;
    }

    public boolean isPublisher() {
        return isPublisher;
    }

    public void setPublisher(boolean publisher) {
        isPublisher = publisher;
    }

    public boolean isManager() {
        return isLeaveManager || isHRLeaveManager || isFinanceLeaveManager || isTicketLeaveManager;
    }

    public boolean isHRLeaveManager() {
        return isHRLeaveManager;
    }

    public int getNotifications() {
        return notifications;
    }

    public void setNotifications(int notifications) {
        this.notifications = notifications;
    }

    public int getManagerNotifications() {
        return managerNotifications;
    }

    public void setManagerNotifications(int managerNotifications) {
        this.managerNotifications = managerNotifications;
    }

    public static String getEmpSmRole() {
        return EMP_SM_ROLE;
    }

    public boolean isFloatingMenuVisible() {
        return floatingMenuVisible;
    }

    public void setFloatingMenuVisible(boolean floatingMenuVisible) {
        this.floatingMenuVisible = floatingMenuVisible;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getWindowMode() {
        return windowMode;
    }

    public void setWindowMode(String windowMode) {
        this.windowMode = windowMode;
    }

    public String getMenuAreaWidth() {
        return menuAreaWidth;
    }

    public void setMenuAreaWidth(String menuAreaWidth) {
        this.menuAreaWidth = menuAreaWidth;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public void setHRLeaveManager(boolean HRLeaveManager) {
        isHRLeaveManager = HRLeaveManager;
    }

    public boolean isLeaveManager() {
        return isLeaveManager;
    }

    public void setLeaveManager(boolean leaveManager) {
        isLeaveManager = leaveManager;
    }

    public boolean isFinanceLeaveManager() {
        return isFinanceLeaveManager;
    }

    public void setFinanceLeaveManager(boolean financeLeaveManager) {
        isFinanceLeaveManager = financeLeaveManager;
    }

    public boolean isTicketLeaveManager() {
        return isTicketLeaveManager;
    }

    public void setTicketLeaveManager(boolean ticketLeaveManager) {
        isTicketLeaveManager = ticketLeaveManager;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public Desktop getDesktop() {
        return desktop;
    }

    public void setDesktop(Desktop desktop) {
        this.desktop = desktop;
    }

    public boolean isReadManagerNotifications() {
        return readManagerNotifications;
    }

    public void setReadManagerNotifications(boolean readManagerNotifications) {
        this.readManagerNotifications = readManagerNotifications;
    }

    public List<qa.tecnositafgulf.model.companyInfo.Project> getActiveProjects() {
        return activeProjects;
    }

    public void setActiveProjects(List<qa.tecnositafgulf.model.companyInfo.Project> activeProjects) {
        this.activeProjects = activeProjects;
    }

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @NotifyChange({"notifications"})
    public void loadNotifications() {
        notifications = authenticationService.listNotificationsForEmployee(employee).size();
        BindUtils.postNotifyChange(null, null, this, "notifications");
    }

    @NotifyChange({"managerNotifications"})
    public void loadManagerNotifications() {
        managerNotifications = authenticationService.listNotificationsForManager(employee).size();
        BindUtils.postNotifyChange(null, null, this, "managerNotifications");
    }

    @Command
    public void viewManagerNotifications() {
        Window window = (Window) Executions.createComponents("/pages/notification/readNotificationManager.zul", null, null);
        window.doModal();
    }

    @Command
    public void viewEmployeeNotifications() {
        Window window = (Window) Executions.createComponents("/pages/notification/readNotificationEmployee.zul", null, null);
        window.doModal();
    }

    public List<PhonePrefix> getPrefixes() {
        return prefixes;
    }

    public void setPrefixes(List<PhonePrefix> prefixes) {
        this.prefixes = prefixes;
    }

    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = css;
    }

    public Integer getPrefixIndex(String country){
        if(country==null)
            return -2;
        for(int i = 0; i<getPrefixes().size();i++){
            if(getPrefixes().get(i).getCountry().equals(country))
                return i;
        }
        return -1;
    }

    public String concat(String s1, String s2, String s3, String s4){
        return s1+s2+s3+s4;
    }


    private TimerTask readNotifications() {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    if (desktop == null) {
                        timer.cancel();
                        return;
                    } else {
                        Executions.activate(desktop);
                        try {
                            loadNotifications();
                            if (readManagerNotifications)
                                loadManagerNotifications();
                        } finally {
                            Executions.deactivate(desktop);
                        }
                    }
                } catch (DesktopUnavailableException ex) {
                    System.out.println("Desktop currently unavailable");
                    timer.cancel();
                } catch (InterruptedException e) {
                    System.out.println("The server push thread interrupted");
                    timer.cancel();
                }
            }
        };
    }
}
