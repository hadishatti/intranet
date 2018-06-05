package qa.tecnositafgulf.viewmodel.administration.inventory.items;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.sys.PageCtrl;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import qa.tecnositafgulf.model.administration.inventory.Item;
import qa.tecnositafgulf.service.InventoryService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ameljo on 6/5/18.
 */
public class ViewItemsViewModel extends IntranetVM {

    //TODO SeachCriteria
    private List<Item> items;
    private InventoryService service;


    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.COMPONENT) Component view){
        init();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        service = context.getBean(InventoryService.class);
        loadData();
        addCommonTags((PageCtrl) view.getPage());
    }

    public void loadData(){
        items = service.getAllItems();
        //TODO implement get items by warehouse id
    }

    @Command
    @NotifyChange("items")
    public void modify(@BindingParam("item") Item item){
        final Map<String, Item> params = new HashMap<String, Item>();
        params.put("itemToModify", item);
        ((Window) Executions.getCurrent().createComponents("l", null, params)).doModal(); //TODO
    }

    @Command
    @NotifyChange("items")
    public void delete(@BindingParam("item") final Item item){
        Messagebox.show("Do you want to delete this Item?", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener<Event>() {
            public void onEvent(Event event) throws Exception {
                if (event.getName().equals("onOK")) {
                    items.remove(item);
                    service.removeItem(item);
                    loadData();
                    Executions.sendRedirect(""); //TODO
                }
            }
        });
    }

    @Command
    @NotifyChange("items")
    public void add(){
        final Map<String, Item> params = new HashMap<String, Item>();
        params.put("itemToModify", null);
        ((Window) Executions.getCurrent().createComponents("", null, params)).doModal(); //TODO
    }
}
