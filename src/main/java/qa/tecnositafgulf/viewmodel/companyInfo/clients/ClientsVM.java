package qa.tecnositafgulf.viewmodel.companyInfo.clients;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.DesktopUnavailableException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.sys.PageCtrl;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import qa.tecnositafgulf.config.MyProperties;
import qa.tecnositafgulf.model.companyInfo.Client;
import qa.tecnositafgulf.searchcriteria.ClientsSearchCriteira;
import qa.tecnositafgulf.service.CompanyInfoService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.util.*;

/**
 * Created by klajdi on 06/05/18.
 */
public class ClientsVM extends IntranetVM{

    private CompanyInfoService service;
    private List<Client> clients;
    private String str;
    private ClientsSearchCriteira clientsSearchCriteria;
    private Integer totalSize;
    private Integer activePage;
    private Timer timer;
    private Desktop desktop;
    private String resource = MyProperties.getInstance().getResourcePath();

    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
        init();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        service = context.getBean(CompanyInfoService.class);
        clients = service.listClients();
        str = createString();
        clientsSearchCriteria = new ClientsSearchCriteira();
        desktop = Executions.getCurrent().getDesktop();
        this.setActivePage(this.clientsSearchCriteria.getStartIndex());
        this.loadData();
        if (timer != null)
            timer.cancel();
        timer = new Timer();
        timer.schedule(readNotifications(), 0, 1000);
        addCommonTags((PageCtrl) view.getPage());
    }

    public void loadData(){
        this.setTotalSize(this.service.getClientCount(clientsSearchCriteria));
        this.clientsSearchCriteria.setStartIndex(getActivePage());
        this.clients = this.service.getClientList(clientsSearchCriteria);
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "clientsSearchCriteria", "clients"})
    public void order(@BindingParam("orderBy") String orderBy){
        this.clientsSearchCriteria.setOrderByFieldName(orderBy);
        this.clientsSearchCriteria.setPageOrderMode("asc".equalsIgnoreCase(this.clientsSearchCriteria.getPageOrderMode()) ? "desc" : "asc");
        //load method call to load data
        loadData();
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "clientsSearchCriteria", "clients"})
    public void clearFilters() {
        this.clientsSearchCriteria.clear();
        this.clientsSearchCriteria.setPageOrderMode("desc");
        this.clientsSearchCriteria.clearFilters();
        this.loadData();
    }

    @Command
    @NotifyChange({"activePage", "totalSize", "clientsSearchCriteria", "clients"})
    public void filter() {
        this.clientsSearchCriteria.clear();
        this.clientsSearchCriteria.setPageOrderMode("desc");
        this.activePage = this.clientsSearchCriteria.getStartIndex();
        this.loadData();
    }

    @NotifyChange({"activePage", "totalSize", "clientsSearchCriteria", "clients"})
    public void setActivePage(Integer activePage) {
        this.activePage = activePage;
        loadData();
    }

    public String createString() {
        String string = "";

        string += "<head>\n" +
                "\n" +
                "                                    <meta charset=\"utf-8\">\n" +
                "                                    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n" +
                "                                    <meta name=\"description\" content=\"\">\n" +
                "                                    <meta name=\"author\" content=\"\">\n" +
                "\n" +
                "                                    <title>Intranet</title>\n" +
                "\n" +
                "                                    <!-- Bootstrap core CSS -->\n" +
                "                                    <link href=\"" + resource + "/bootstrap/css/bootstrap.min.css\" rel=\"stylesheet\">\n" +
                "\n" +
                "                                    <!-- Custom styles for this template -->\n" +
                "                                    <link href=\"" + resource + "/customized/css/partners_thumbnail_gallery.css\" rel=\"stylesheet\">\n" +
                "\n" +
                "                                  </head>\n" +
                "\n" +
                "                                  <body>\n" +
                "\n" +
                "                                    <!-- Page Content -->\n" +
                "                                    <div class=\"container\">\n" +
                "\n" +
                "                                      <h1 class=\"my-4 text-center text-lg-left\" style=\"color:#1a4280\">Our Clients</h1>\n" +
                "\n" +
                "                                      <div class=\"row text-center text-lg-left\">";

        for(int i = 0; i < clients.size(); i++) {
            string += "<div class=\"col-lg-3 col-md-4 col-xs-6\"> " +
                    "<a href=\"" + clients.get(i).getLink() + "\" class=\"d-block mb-4 h-100\"> " +
                    "<img class=\"img-fluid img-thumbnail\" src=\"" + MyProperties.getInstance().getImagePath() + clients.get(i).getImg() +
                    " \" alt=\" \"> " +
                    "</a> " +
                    "</div>";
        }

        string += "</div>\n" +
                "\n" +
                "                                    </div>\n" +
                "                                    <!-- /.container -->\n" +
                "\n" +
                "                                    <!-- Bootstrap core JavaScript -->\n" +
                "                                    <script src=\"" + resource + "/customized/jquery/jquery.min.js\"></script>\n" +
                "                                    <script src=\"" + resource + "/bootstrap/js/bootstrap.bundle.min.js\"></script>\n" +
                "\n" +
                "                                  </body>";

        return string;
    }

    @Command
    @NotifyChange("clients")
    public void modify(@BindingParam("item") Client client){
        final Map<String, Client> params = new HashMap<String, Client>();
        params.put("clientToModify", client);
        ((Window) Executions.getCurrent().createComponents("/pages/company-info/clients/addClient.zul", null, params)).doModal();
    }

    @Command
    public void edit(){
        Executions.sendRedirect("/pages/company-info/clients/editClient.zul");
    }

    @Command
    @NotifyChange("clients")
    public void delete(@BindingParam("item") final Client client){

        Messagebox.show("Do you want to delete this user?", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener<Event>() {
            public void onEvent(Event event) throws Exception {
                if (event.getName().equals("onOK")) {
                    clients.remove(client);
                    service.removeClient(client);
                    Executions.sendRedirect("/pages/company-info/clients/editClient.zul");
                }
            }
        });
    }

    @Command
    @NotifyChange("clients")
    public void add(){
        final Map<String, Client> params = new HashMap<String, Client>();
        params.put("clientsToModify", null);
        ((Window) Executions.getCurrent().createComponents("/pages/company-info/clients/addClient.zul", null, params)).doModal();
    }


    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public CompanyInfoService getService() {
        return service;
    }

    public void setService(CompanyInfoService service) {
        this.service = service;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
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


    public ClientsSearchCriteira getClientsSearchCriteria() {
        return clientsSearchCriteria;
    }

    public void setClientsSearchCriteria(ClientsSearchCriteira clientsSearchCriteria) {
        this.clientsSearchCriteria = clientsSearchCriteria;
    }

    @NotifyChange("clients")
    public void load(){
        loadData();
    }

    @NotifyChange({"clients"})
    public void loadClients(){
        clients = service.listClients();
        str = createString();
        BindUtils.postNotifyChange(null, null, this, "str");
    }

    private TimerTask readNotifications(){
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    if(desktop == null) {
                        timer.cancel();
                        return;
                    }
                    Executions.activate(desktop);
                    try {
                        loadClients();
                    } finally {
                        Executions.deactivate(desktop);
                    }
                }catch(DesktopUnavailableException ex) {
                    System.out.println("Desktop currently unavailable");
                    timer.cancel();
                }catch(InterruptedException e) {
                    System.out.println("The server push thread interrupted");
                    timer.cancel();
                }
            }
        };
    }
}
