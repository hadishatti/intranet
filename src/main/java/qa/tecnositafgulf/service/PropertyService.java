package qa.tecnositafgulf.service;

import qa.tecnositafgulf.model.properties.Property;
import qa.tecnositafgulf.searchcriteria.PropertySearchCriteria;

import java.util.List;

/**
 * Created by ameljo on 5/15/18.
 */
public interface PropertyService {

    void saveProperty(Property property);

    void removeProperty(Property property);

    List<Property> getAllProperties();

    List<Property> getAllProperties(PropertySearchCriteria propertySearchCriteria);

    Integer countProperty(PropertySearchCriteria propertySearchCriteria);
}
