package qa.tecnositafgulf.dao.administration.inventory.warehouse;

import qa.tecnositafgulf.model.administration.inventory.Warehouse;

import javax.persistence.*;
import java.util.List;

/**
 * Created by ledio on 6/4/18.
 */
public class WarehouseDaoImpl implements WarehouseDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(Warehouse warehouse) {
        em.merge(warehouse);
    }

    @Override
    public void remove(Warehouse warehouse) {
        em.remove(em.contains(warehouse) ? warehouse : em.merge(warehouse));
    }

    @Override
    public List<Warehouse> listAllWarehouses() {
        TypedQuery<Warehouse> query = em.createNamedQuery("Warehouse.listAllWarehouses", Warehouse.class);
        return query.getResultList();
    }

    @Override
    public Integer countAllWarehouses() {
        Query query = em.createNamedQuery("Warehouse.countAllWarehouses");
        return ((Long)query.getSingleResult()).intValue();
    }

    @Override
    public Warehouse getWarehouseByName(String name) {
        TypedQuery<Warehouse> query = em.createNamedQuery("Warehouse.getWarehouseByName", Warehouse.class);
        query.setParameter("name", name);
        Warehouse warehouse;
        try {
            warehouse = (Warehouse) query.getSingleResult();
        } catch (NoResultException e){
            warehouse=null;
        }
        return warehouse;
    }
}
