package qa.tecnositafgulf.dao.properties;

import org.springframework.stereotype.Repository;
import qa.tecnositafgulf.model.properties.Property;
import qa.tecnositafgulf.searchcriteria.PropertySearchCriteria;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by ameljo on 5/15/18.
 */
@Repository
public class PropertyDaoImpl implements PropertyDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void saveProperty(Property property) {
        em.merge(property);
    }

    public void removeProperty(Property property){
        em.remove(em.contains(property) ? property : em.merge(property));
    }

    @Override
    public List<Property> listAll() {
        TypedQuery<Property> query = em.createNamedQuery("Property.listAll", Property.class);
        return query.getResultList();
    }

    @Override
    public List<Property> listProperties(PropertySearchCriteria propertySearchCriteria) {
        Query query = propertySearchCriteria.toQuery(em);
        return query.getResultList();
    }

    @Override
    public Integer countProperty(PropertySearchCriteria propertySearchCriteria) {
        Query query = propertySearchCriteria.getCountQuery(em);
        Number cResults = (Number) query.getSingleResult();
        return cResults.intValue();
    }
}
