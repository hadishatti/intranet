package qa.tecnositafgulf.dao.administration.inventory.inventoryLog;

import qa.tecnositafgulf.config.inventoryEnums.InventoryActionEnum;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.administration.inventory.InventoryLog;
import qa.tecnositafgulf.model.administration.inventory.Warehouse;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by ledio on 6/4/18.
 */
public class InventoryLogDaoImpl implements InventoryLogDao {

    @PersistenceContext
    private EntityManager em;


    @Override
    public void save(InventoryLog inventoryLog) {
        em.merge(inventoryLog);
    }

    @Override
    public void remove(InventoryLog inventoryLog) {
        em.remove(em.contains(inventoryLog) ? inventoryLog : em.merge(inventoryLog));
    }

    @Override
    public List<InventoryLog> getAllLogs() {
        TypedQuery<InventoryLog> query = em.createNamedQuery("InventoryLog.getAllLogs", InventoryLog.class);
        return query.getResultList();
    }

    @Override
    public Integer countAllLogs() {
        Query query = em.createNamedQuery("InventoryLog.countAllLogs");
        return ((Long)query.getSingleResult()).intValue();
    }

    @Override
    public List<InventoryLog> getAllLogsByEmployee(Employee employee) {
        TypedQuery<InventoryLog> query = em.createNamedQuery("InventoryLog.getAllLogsByEmployee", InventoryLog.class);
        query.setParameter("employee", employee);
        return query.getResultList();
    }

    @Override
    public List<InventoryLog> getAllLogsByActionDate(Date actionDate) {
        TypedQuery<InventoryLog> query = em.createNamedQuery("InventoryLog.getAllLogsByActionDate", InventoryLog.class);
        query.setParameter("actionDate", actionDate);
        return query.getResultList();
    }

    @Override
    public List<InventoryLog> getAllLogsByAction(InventoryActionEnum action) {
        TypedQuery<InventoryLog> query = em.createNamedQuery("InventoryLog.getAllLogsByAction", InventoryLog.class);
        query.setParameter("action", action);
        return query.getResultList();
    }

    @Override
    public List<InventoryLog> getAllLogsByWarehouse(Warehouse warehouse) {
        TypedQuery<InventoryLog> query = em.createNamedQuery("InventoryLog.getAllLogsByWarehouse", InventoryLog.class);
        query.setParameter("warehouse", warehouse);
        return query.getResultList();
    }
}
