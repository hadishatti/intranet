package qa.tecnositafgulf.dao.administration.inventory.item;

import org.springframework.stereotype.Repository;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.zkoss.zkplus.hibernate.HibernateUtil;
import qa.tecnositafgulf.config.inventoryEnums.ItemStatusEnum;
import qa.tecnositafgulf.model.administration.inventory.Item;
import qa.tecnositafgulf.model.administration.inventory.Warehouse;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by ledio on 6/4/18.
 */

@Repository
public class ItemDaoImpl implements ItemDao{

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(Item item) {
        em.merge(item);
    } //TODO Save new categories

    @Override
    public void remove(Item item) {
        em.remove(em.contains(item) ? item : em.merge(item));
    } //TODO Don't remove from db.

    @Override
    public List<Item> listAllItems() {
        TypedQuery<Item> query = em.createNamedQuery("Item.listAllItems", Item.class);
        return query.getResultList();
    }

    @Override
    public Integer countAllItems() {
        Query query = em.createNamedQuery("Item.countAllItems");
        return ((Long)query.getSingleResult()).intValue();
    }

    @Override
    public List<Item> getItemsByStatus(ItemStatusEnum itemStatusEnum) {
        TypedQuery<Item> query = em.createNamedQuery("Item.getItemsByStatus", Item.class);
        query.setParameter("status", itemStatusEnum);
        return query.getResultList();
    }

    @Override
    public Integer countItemsByStatus(ItemStatusEnum itemStatusEnum) {
        Query query = em.createNamedQuery("Item.countItemsByStatus");
        query.setParameter("status", itemStatusEnum);
        return ((Long)query.getSingleResult()).intValue();
    }

    @Override
    public List<Item> getItemsByName(String name) {
        TypedQuery<Item> query = em.createNamedQuery("Item.getItemsByName", Item.class);
        query.setParameter("name", name);
        return query.getResultList();
    }

    @Override
    public Integer countItemsByName(String name) {
        Query query = em.createNamedQuery("Item.countItemsByName");
        query.setParameter("name", name);
        return ((Long)query.getSingleResult()).intValue();
    }

    @Override
    public List<Item> getItemsByCategory(String category) {
        TypedQuery<Item> query = em.createNamedQuery("Item.getItemsByCategory", Item.class);
        query.setParameter("category", category);
        return query.getResultList();
    }

    @Override
    public Integer countItemsByCategory(String category) {
        Query query = em.createNamedQuery("Item.countItemsByCategory");
        query.setParameter("category", category);
        return ((Long)query.getSingleResult()).intValue();
    }

    @Override
    public List<Item> getItemsByBrand(String brand) {
        TypedQuery<Item> query = em.createNamedQuery("Item.getItemsByBrand", Item.class);
        query.setParameter("brand", brand);
        return query.getResultList();
    }

    @Override
    public Integer countItemsByBrand(String brand) {
        Query query = em.createNamedQuery("Item.countItemsByBrand");
        query.setParameter("brand", brand);
        return ((Long)query.getSingleResult()).intValue();
    }

    @Override
    public List<Item> getDistinctItemGroups(Warehouse warehouse) {
        //TODO May not be needed. Used to get Inventory Categories of a Warehouse
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(Item.class);
        criteria.add(Restrictions.eq("warehouse", warehouse));
        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.property("category"));
        projectionList.add(Projections.property("name"));
        projectionList.add(Projections.property("brand"));

        criteria.setProjection(projectionList);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        return criteria.list();
    }

    @Override
    public int countDistinctItemGroups(Warehouse warehouse) {
        return 0;
    }


}
