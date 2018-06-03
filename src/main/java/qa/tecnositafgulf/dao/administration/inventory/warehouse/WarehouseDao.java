package qa.tecnositafgulf.dao.administration.inventory.warehouse;

import qa.tecnositafgulf.model.administration.inventory.Warehouse;

import java.util.List;

/**
 * Created by ledio on 6/3/18.
 */
public interface WarehouseDao {

    void save(Warehouse warehouse);

    void remove(Warehouse warehouse);

    List<Warehouse> listAllWarehouses();
    Integer countAllWarehouses();

    Warehouse getWarehouseByName(String name);
}
