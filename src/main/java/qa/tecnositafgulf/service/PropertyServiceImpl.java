package qa.tecnositafgulf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qa.tecnositafgulf.dao.properties.PropertyDao;
import qa.tecnositafgulf.model.properties.Property;
import qa.tecnositafgulf.searchcriteria.PropertySearchCriteria;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ameljo on 5/15/18.
 */

@Service
public class PropertyServiceImpl implements PropertyService{

    @Autowired
    PropertyDao propertyDao;

    @Transactional
    public void saveProperty(Property property) {
        propertyDao.saveProperty(property);
    }

    @Transactional
    public void removeProperty(Property property){
        propertyDao.removeProperty(property);
    }

    @Transactional
    public List<Property> getAllProperties() {
        return propertyDao.listAll();
    }

    @Override
    @Transactional
    public List<Property> getAllProperties(PropertySearchCriteria propertySearchCriteria) {
        return propertyDao.listProperties(propertySearchCriteria);
    }

    @Override
    @Transactional
    public Integer countProperty(PropertySearchCriteria propertySearchCriteria){
        return propertyDao.countProperty(propertySearchCriteria);
    }
}
