package qa.tecnositafgulf.service;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qa.tecnositafgulf.dao.administration.inventory.item.ItemDao;
import qa.tecnositafgulf.dao.administration.inventory.transfer.TransferDao;
import qa.tecnositafgulf.dao.administration.inventory.inventoryLog.InventoryLogDao;
import qa.tecnositafgulf.dao.administration.inventory.warehouse.WarehouseDao;
import qa.tecnositafgulf.dao.inventory.InventoryDao;
import qa.tecnositafgulf.model.administration.inventory.Item;
import qa.tecnositafgulf.model.administration.inventory.Transfer;
import qa.tecnositafgulf.model.administration.inventory.InventoryLog;
import qa.tecnositafgulf.model.administration.inventory.Warehouse;
import qa.tecnositafgulf.model.inventory.location.Location;
import qa.tecnositafgulf.model.inventory.product.Product;
import qa.tecnositafgulf.model.inventory.productCategory.ProductCategory;
import qa.tecnositafgulf.model.inventory.stocks.ProductStock;
import qa.tecnositafgulf.model.inventory.stocks.TransferStock;
import qa.tecnositafgulf.model.inventory.supplier.ProductSupplier;
import qa.tecnositafgulf.searchcriteria.inventory.*;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    WarehouseDao warehouseDao;

    @Autowired
    InventoryLogDao inventoryLogDao;

    @Autowired
    ItemDao itemDao;

    @Autowired
    TransferDao transferDao;

    @Override
    public List<Warehouse> getWarehouseList() {
        return warehouseDao.listAllWarehouses();
    }

    @Override
    @Transactional
    public void saveWarehouse(Warehouse warehouse) {
        warehouseDao.save(warehouse);
    }

    @Override
    public List<InventoryLog> getAllInventoryLogs() {
        return inventoryLogDao.getAllLogs();
    }

    @Override
    public void removeItem(Item item){
        itemDao.remove(item);
    }

    @Override
    public void saveItem(Item item){
        itemDao.save(item);
    }

    @Override
    public List<Item> getAllItems(){
        return itemDao.listAllItems();
    }

    @Override
    public void saveTransfer(Transfer transfer) {
        transferDao.saveTransfer(transfer);
    }

    @Override
    public List<Transfer> getAllTransfers() {
        return null;
    }

    //TODO implement all methods

}
