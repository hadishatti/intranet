package qa.tecnositafgulf.config;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;
import qa.tecnositafgulf.model.properties.Property;
import qa.tecnositafgulf.service.PropertyService;
import qa.tecnositafgulf.spring.config.AppConfig;

import javax.persistence.PersistenceException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ameljo on 5/9/18.
 */
public class MyProperties{
    private static MyProperties instance = null;
    private HashMap<String, String> properties;
    private PropertyService service;

    private MyProperties() {
    }

    public static MyProperties getInstance() {
        if (instance == null) {
            instance = new MyProperties();
            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
            instance.service = context.getBean(PropertyService.class);

            List<Property> list = instance.service.getAllProperties();
            HashMap<String, String> map = new HashMap<>();
            for(Property property: list)
                map.put(property.getKey(), property.getValue());
            instance.properties = map;
        }
        return instance;
    }

    public String getProperty(String key){
        String value = instance.properties.get(key);
        if (value == null) {
             return "";
        }
        return value;
    }

    public void saveProperty(Property property){
        instance.properties.put(property.getKey(), property.getValue());
        instance.service.saveProperty(property);
    }

    public void deleteProperty(Property property){
        instance.properties.remove(property.getKey());
        instance.service.removeProperty(property);
    }


    public String getImagePath(){
        return instance.getProperty("imagePath");
    }

    public String getResourcePath() { return  instance.getProperty("resPath");}
}