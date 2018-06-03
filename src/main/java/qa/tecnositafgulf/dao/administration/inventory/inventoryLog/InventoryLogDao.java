package qa.tecnositafgulf.dao.administration.inventory.inventoryLog;

import qa.tecnositafgulf.config.inventoryEnums.InventoryActionEnum;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.administration.inventory.InventoryLog;

import java.util.Date;
import java.util.List;

/**
 * Created by ledio on 6/3/18.
 */
public interface InventoryLogDao {

    void save(InventoryLog inventoryLog);

    void remove(InventoryLog inventoryLog);

    List<InventoryLog> getAllLogs();
    Integer countAllLogs();

    List<InventoryLog> getAllLogsByEmployee(Employee employee);
    Integer countAllLogsByEmployee(Employee employee);

    List<InventoryLog> getAllLogsByActionDate(Date actionDate);
    Integer countAllLogsByActionDate(Date actionDate);

    List<InventoryLog> getAllLogsByAction(InventoryActionEnum actionEnum);
    Integer countAllLogsByAction(InventoryActionEnum actionEnum);

}
