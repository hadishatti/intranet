package qa.tecnositafgulf.viewmodel.inventory.productLocation;

import qa.tecnositafgulf.model.inventory.stocks.TransferStock;
import qa.tecnositafgulf.searchcriteria.inventory.StockTransferSearchCriteria;
import qa.tecnositafgulf.service.InventoryService;
import qa.tecnositafgulf.viewmodel.IntranetVM;

public class ViewStockTransferViewModel  extends IntranetVM {

    private InventoryService inventoryService;
    private StockTransferSearchCriteria stockTransferSearchCriteria;
    private TransferStock transferStock;
    private Integer totalSize;
    private Integer activePage;


}
