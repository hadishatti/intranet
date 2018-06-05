package qa.tecnositafgulf.viewmodel.administration.inventory.transfers;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.sys.PageCtrl;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import qa.tecnositafgulf.model.administration.inventory.Transfer;
import qa.tecnositafgulf.service.InventoryService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ameljo on 6/5/18.
 */
public class ViewTransfersViewModel extends IntranetVM{

    private List<Transfer> transfers;
    private InventoryService service;
    //TODO SearchCriteria

    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.COMPONENT) Component view){
        init();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        service = context.getBean(InventoryService.class);
        loadData();
        addCommonTags((PageCtrl) view.getPage());
    }

    public void loadData(){
        transfers = service.getAllTransfers();
        //TODO implement get items by warehouse id
    }

    @Command
    @NotifyChange("transfers")
    public void modify(@BindingParam("item") Transfer item){
        final Map<String, Transfer> params = new HashMap<String, Transfer>();
        params.put("transferToModify", item);
        ((Window) Executions.getCurrent().createComponents("", null, params)).doModal(); //TODO
    }


    @Command
    @NotifyChange("transfers")
    public void add(){
        final Map<String, Transfer> params = new HashMap<String, Transfer>();
        params.put("transferToModify", null);
        ((Window) Executions.getCurrent().createComponents("", null, params)).doModal(); //TODO
    }
}
