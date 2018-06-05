package qa.tecnositafgulf.viewmodel.administration.inventory.items;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.sys.PageCtrl;
import org.zkoss.zul.Messagebox;
import qa.tecnositafgulf.model.administration.inventory.Item;
import qa.tecnositafgulf.service.InventoryService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import javax.persistence.PersistenceException;

/**
 * Created by ameljo on 6/5/18.
 */
public class SaveItemViewModel extends IntranetVM {

    private Item item;
    private InventoryService service;

    @AfterCompose
     public void doAfterCompose(@ContextParam(ContextType.COMPONENT)Component view, @ExecutionArgParam("itemToModify") Item itemToModify){
        init();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        service = context.getBean(InventoryService.class);
        if(itemToModify != null){
            item = itemToModify;
        } else {
            item = new Item();
            //TODO implement saving an item in a specific Warehouse
        }
        addCommonTags((PageCtrl) view.getPage());
    }

    @Command
    public void saveItem(){
        if(item.getSerialNumber() == null){
            return;
        }
        if(item.getBrand() == null || item.getBrand().isEmpty()){
            return;
        }
        if(item.getCategory() == null || item.getCategory().isEmpty()){
            return;
        }
        if(item.getName() == null || item.getName().isEmpty()){
            return;
        }
        if(item.getUnit() == null || item.getUnit().isEmpty()){
            return;
        }

        try {
            service.saveItem(item);
            Messagebox.show("OK! Do you want to view all the Items?", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener<Event>() {
                public void onEvent(Event event) throws Exception {
                    if (event.getName().equals("onOK")) {
                        Executions.sendRedirect(""); //TODO
                    }
                }
            });
        }catch (PersistenceException persistenceException){
            Messagebox.show("This item already exits", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION);
        }
    }
}
