package qa.tecnositafgulf.viewmodel.companyInfo.clients;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;
import qa.tecnositafgulf.model.companyInfo.Client;
import qa.tecnositafgulf.service.CompanyInfoService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import javax.persistence.PersistenceException;

/**
 * Created by klajdi on 06/05/18.
 */
public class addClientsVM extends IntranetVM {

    private Client client;
    private CompanyInfoService clientService;


    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("clientToModify") Client clientToModify){
        init();
        client = new Client();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        clientService = context.getBean(CompanyInfoService.class);
        if(clientToModify != null){
            client = clientToModify;
        }else{
            client = new Client();
        }

    }

    @Command
    @NotifyChange("client")
    public void saveClient() {
        try {
            clientService.saveClient(client);
            Messagebox.show("OK! Do you want to view all the Clients?", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener<Event>() {
                public void onEvent(Event event) throws Exception {
                    if (event.getName().equals("onOK")) {
                        Executions.sendRedirect("/pages/company-info/clients/viewClients.zul");
                    }
                }
            });
        }catch (PersistenceException persistenceException){
            Messagebox.show("Client with same name already exits", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION);
        }
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
