package qa.tecnositafgulf.viewmodel.administration.inventory.transfers;

import org.omg.CORBA.Context;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;
import qa.tecnositafgulf.model.administration.inventory.Transfer;
import qa.tecnositafgulf.service.InventoryService;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import javax.persistence.PersistenceException;

/**
 * Created by ameljo on 6/5/18.
 */
public class SaveTransferViewModel extends IntranetVM{

    private Transfer transfer;
    private InventoryService service;

    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.COMPONENT)Component view, @ExecutionArgParam("transferToModify") Transfer transferToModify){
        if (transferToModify != null){
            transfer = transferToModify;
        } else {
            transfer = new Transfer();
        }
    }

    @Command
    public void saveItem(){
        if (transfer.getAmount() == 0){
            return;
        }

        if (transfer.getDestinationWarehouse() == null) {
            return;
        }
        if (transfer.getOriginWareHouse() == null) {
            return;
        }

        if(transfer.getItems() == null || transfer.getItems().isEmpty()){
            return;
        }

        try {
            service.saveTransfer(transfer);
            Messagebox.show("OK! Do you want to view all the Transfers?", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener<Event>() {
                public void onEvent(Event event) throws Exception {
                    if (event.getName().equals("onOK")) {
                        Executions.sendRedirect(""); //TODO
                    }
                }
            });
        }catch (PersistenceException persistenceException){
            Messagebox.show("This transfer already exits", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION);
        }
    }
}
