package qa.tecnositafgulf.model.suppliers;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import qa.tecnositafgulf.service.SupplierService;
import qa.tecnositafgulf.spring.config.AppConfig;

import java.util.List;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: CustomDataSource.java 1229 2006-04-19 10:27:35Z teodord $
 */
public class CustomDataSource implements JRDataSource {

    private SupplierService supplierService;

    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

 
    private List<Supplier> data = getDataSource();
 
    private int index = -1;
 
    public CustomDataSource() {
    }
 
    public boolean next() throws JRException {
        index++;
        return (index < data.size());
    }

    private List<Supplier> getDataSource(){
        supplierService = context.getBean(SupplierService.class);
        List<Supplier> suppliers = supplierService.listSuppliers();



        return suppliers;
    }
 
    public Object getFieldValue(JRField field) throws JRException {
        Object value = null;
        String fieldName = field.getName();

        if ("domainURL".equals(fieldName)) {
            value = data.get(index).getDomainURL();
        } else if ("name".equals(fieldName)) {
            value = data.get(index).getName();
        } else if ("imgSrc".equals(fieldName)) {
            value ="/home/ledio/Documents/Projects/Intranet/src/main/webapp/pages/company-info/" + data.get(index).getImgSrc();
        } else if ("id".equals(fieldName)){
            value = data.get(index).getId();
        }

        return value;
    }
}