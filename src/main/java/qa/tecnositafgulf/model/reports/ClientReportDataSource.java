package qa.tecnositafgulf.model.reports;

/**
 * Created by klajdi on 08/05/18.
 */

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import qa.tecnositafgulf.config.MyProperties;
import qa.tecnositafgulf.model.companyInfo.Client;
import qa.tecnositafgulf.service.CompanyInfoService;
import qa.tecnositafgulf.spring.config.AppConfig;

import java.util.List;

public class ClientReportDataSource implements JRDataSource {

    private CompanyInfoService clientService;
    private List<Client> data;
    private int index = -1;

    public ClientReportDataSource() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        clientService = context.getBean(CompanyInfoService.class);
        data = clientService.listClients();
    }

    public boolean next() throws JRException {
        index++;
        return (index < data.size());
    }

    public Object getFieldValue(JRField field) throws JRException {
        Object value = null;
        String fieldName = field.getName();

        if ("description".equals(fieldName)) {
            value = data.get(index).getDescription();
        } else if ("img".equals(fieldName)) {
            value = MyProperties.getInstance().getImagePath() +  "/" + data.get(index).getImg();
        } else if ("name".equals(fieldName)) {
            value = data.get(index).getName();
        } else if ("link".equals(fieldName)) {
            value = data.get(index).getLink();
        }

        return value;
    }

    public CompanyInfoService getClientService() {
        return clientService;
    }

    public void setClientService(CompanyInfoService clientService) {
        this.clientService = clientService;
    }

    public List<Client> getData() {
        return data;
    }

    public void setData(List<Client> data) {
        this.data = data;
    }
}