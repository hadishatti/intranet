package qa.tecnositafgulf.dao.administration.inventory.inventoryCategory;

import qa.tecnositafgulf.model.administration.inventory.InventoryCategory;
import qa.tecnositafgulf.model.administration.inventory.Warehouse;

import java.util.List;

/**
 * Created by ledio on 6/5/18.
 */
public interface InventoryCategoryDao {

    void save(InventoryCategory inventoryCategory);

    void remove(InventoryCategory inventoryCategory);

    List<InventoryCategory> getAllByWarehouse(Warehouse warehouse);

    Integer countAllByWarehouse(Warehouse warehouse);
}
