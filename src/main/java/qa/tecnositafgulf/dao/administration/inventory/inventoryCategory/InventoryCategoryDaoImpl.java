package qa.tecnositafgulf.dao.administration.inventory.inventoryCategory;

import org.springframework.stereotype.Repository;
import qa.tecnositafgulf.model.administration.inventory.InventoryCategory;
import qa.tecnositafgulf.model.administration.inventory.Warehouse;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by ledio on 6/5/18.
 */

@Repository
public class InventoryCategoryDaoImpl implements InventoryCategoryDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(InventoryCategory inventoryCategory) {
        em.merge(inventoryCategory);
    }

    @Override
    public void remove(InventoryCategory inventoryCategory) {
        em.remove(em.contains(inventoryCategory) ? inventoryCategory : em.merge(inventoryCategory));
    }

    @Override
    public List<InventoryCategory> getAllByWarehouse(Warehouse warehouse) {
        TypedQuery<InventoryCategory> query = em.createNamedQuery("InventoryCategory.getAllByWarehouse", InventoryCategory.class);
        query.setParameter("warehouse", warehouse);
        return query.getResultList();
    }

    @Override
    public Integer countAllByWarehouse(Warehouse warehouse) {
        Query query = em.createNamedQuery("InventoryCategory.countAllByWarehouse", InventoryCategory.class);
        query.setParameter("warehouse", warehouse);
        return ((Long)query.getSingleResult()).intValue();
    }
}
