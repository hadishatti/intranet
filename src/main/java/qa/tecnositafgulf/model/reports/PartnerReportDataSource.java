package qa.tecnositafgulf.model.reports;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import qa.tecnositafgulf.config.MyProperties;
import qa.tecnositafgulf.model.companyInfo.Partner;
import qa.tecnositafgulf.service.CompanyInfoService;
import qa.tecnositafgulf.spring.config.AppConfig;

import java.net.URL;
import java.util.List;

/**
 * Created by ameljo on 5/8/18.
 */
public class PartnerReportDataSource implements JRDataSource {

    CompanyInfoService service;

    private List<Partner> partnerList;

    public PartnerReportDataSource(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        service = context.getBean(CompanyInfoService.class);
        partnerList = service.getAllPartners();
    }

    private int index = -1;

    @Override
    public boolean next() throws JRException {
        index++;
        return (index < partnerList.size());
    }

    @Override
    public Object getFieldValue(JRField jrField) throws JRException {
        Object val = null;
        String name = jrField.getName();
        URL url = null;

        if(name.equals("name")){
            val = partnerList.get(index).getName();
        } else if (name.equals("href")) {
            val = partnerList.get(index).getHref();
        } else if (name.equals("img")) {
            val = MyProperties.getInstance().getImagePath()  + partnerList.get(index).getImg();
        }
        if ("imagePath".equals(name))
            val = MyProperties.getInstance().getImagePath()+"/gbdg-logo.jpeg";

        return val;
    }
}
