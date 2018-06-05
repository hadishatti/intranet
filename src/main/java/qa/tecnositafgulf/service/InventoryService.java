package qa.tecnositafgulf.service;

import qa.tecnositafgulf.model.administration.inventory.Item;
import qa.tecnositafgulf.model.administration.inventory.Transfer;
import qa.tecnositafgulf.model.administration.inventory.Warehouse;
import qa.tecnositafgulf.model.inventory.location.Location;
import qa.tecnositafgulf.model.inventory.product.Product;
import qa.tecnositafgulf.model.inventory.productCategory.ProductCategory;
import qa.tecnositafgulf.model.inventory.stocks.ProductStock;
import qa.tecnositafgulf.model.inventory.stocks.TransferStock;
import qa.tecnositafgulf.model.inventory.supplier.ProductSupplier;
import qa.tecnositafgulf.searchcriteria.inventory.*;

import java.util.List;

public interface InventoryService {

    //TODO define all methods

    List<Warehouse> getWarehouseList();

    void removeItem(Item item);

    void saveItem(Item item);

    List<Item> getAllItems();

    void saveTransfer(Transfer transfer);

    List<Transfer> getAllTransfers();

}
