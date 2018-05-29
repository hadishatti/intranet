package qa.tecnositafgulf.dao.properties;

import qa.tecnositafgulf.model.properties.Property;
import qa.tecnositafgulf.searchcriteria.PropertySearchCriteria;

import java.util.List;

/**
 * Created by ameljo on 5/15/18.
 */
public interface PropertyDao {

    void saveProperty(Property property);

    void removeProperty(Property property);

    List<Property> listAll();

    List<Property> listProperties(PropertySearchCriteria propertySearchCriteria);

    Integer countProperty(PropertySearchCriteria propertySearchCriteria);
}
