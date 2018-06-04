package qa.tecnositafgulf.dao.administration.inventory.inventoryLog;

import qa.tecnositafgulf.config.inventoryEnums.InventoryActionEnum;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.administration.inventory.InventoryLog;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * Created by ledio on 6/4/18.
 */
public class InventoryLogDaoImpl implements InventoryLogDao {

    @PersistenceContext
    private EntityManager em; //TODO implement all queries


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
        return null;
    }

    @Override
    public List<InventoryLog> getAllLogsByEmployee(Employee employee) {
        return null;
    }

    @Override
    public Integer countAllLogsByEmployee(Employee employee) {
        return null;
    }

    @Override
    public List<InventoryLog> getAllLogsByActionDate(Date actionDate) {
        return null;
    }

    @Override
    public Integer countAllLogsByActionDate(Date actionDate) {
        return null;
    }

    @Override
    public List<InventoryLog> getAllLogsByAction(InventoryActionEnum actionEnum) {
        return null;
    }

    @Override
    public Integer countAllLogsByAction(InventoryActionEnum actionEnum) {
        return null;
    }
}
