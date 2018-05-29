package qa.tecnositafgulf.viewmodel.companyInfo;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import qa.tecnositafgulf.model.administration.Department;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.service.AdministrationService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hadi on 1/21/18.
 */
public class OrganizationChartVM extends IntranetVM {
    private AdministrationService service;
    private List<Employee> employees;
    private List<Department> departments;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        init();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        service = context.getBean(AdministrationService.class);
        employees = service.listEmployeesByChartId();
        departments = service.listDepartmentsByChartId();
        String script = "var orgChart = new getOrgChart(\n" +
                "            document.getElementById(\"people\"), {\n" +
                "                primaryFields : [\"name\", \"title\"],\n" +
                "                photoFields : [\"image\"],\n" +
                "                secondParentIdField : \"secondParenId\",\n" +
                "                enableEdit : \"false\",\n" +
                "                enableDetailsView : \"false\",\n" +
                "                enableExportToImage : \"false\",\n" +
                "                gridView : true,\n" +
                "                linkType : \"B\",\n" +
                "                expandToLevel : 100,\n" +
                "                layout : getOrgChart.MIXED_HIERARCHY_RIGHT_LINKS,\n" +
                "                dataSource : ["+getDateSource()+"],\n" +
                "                customize : {"+getThemes()+"}\n" +
                "            });";

        Clients.evalJavaScript(script);

    }

    private String getThemes(){
        String ret = "";
        for(Employee employee : employees){
            ret += "\""+employee.getChartId()+"\" : {\"theme\" : \"deborah\", \"color\" : \""+employee.getColor()+"\"},\n";
        }
        for(Department dep : departments){
            ret += "\""+dep.getChartId()+"\" : {\"color\" : \""+dep.getColor()+"\"},\n";
        }
        return ret;
    }

    private String getDateSource(){
        String ret = "";

        for(Employee employee : employees){
            ret += "{id : "+employee.getChartId()+","+
                    "parentId : "+employee.getParentId()+","+
                    (employee.getSecondParentId()!=null?"secondParenId : "+employee.getSecondParentId()+",":"")+
                    "name : \""+employee.getName()+" "+employee.getFamilyName()+ "\","+
                    "title : \""+employee.getRole().getDescription()+"\",";
            String phone = "phone : \"";
            List<String> phones = new ArrayList<String>();
            if(employee.getOfficePhoneNumber()!=null)
                phones.add(employee.getOfficePhoneNumber());
            if(employee.getPersonalPhoneNumber1()!=null)
                phones.add(employee.getPersonalPhoneNumber1());
            if(employee.getPersonalPhoneNumber2()!=null)
                phones.add(employee.getPersonalPhoneNumber2());
            if(phones.size()!=0) {
                int i=0;
                for (String m : phones) {
                    phone += m;
                    if(i<phones.size()-1)
                        phone += " - ";
                    i++;
                }
                ret += phone+"\",";
            }
            String mail = "mail : \"";
            List<String> mails = new ArrayList<String>();
            if(employee.getMail1()!=null)
                mails.add(employee.getMail1());
            if(employee.getMail2()!=null)
                mails.add(employee.getMail2());
            if(employee.getMail3()!=null)
                mails.add(employee.getMail3());
            if(mails.size()!=0) {
                int i=0;
                for (String m : mails) {
                    mail += m;
                    if(i<mails.size()-1)
                        mail += " - ";
                    i++;
                }
                ret += mail+"\",";
            }
            ret += (employee.getAddress()!=null?"address : \""+employee.getAddress()+"\",":"")+"image : \""+ getPath()+(employee.getImage()!=null?employee.getImage():"userpic.png")+"\"},\n";
        }
        for(Department dep : departments){
            ret += "{ id : "+dep.getChartId()+","+
                    "parentId : "+dep.getParentId()+","+
                    "name : \""+dep.getName()+"\",title:\"Department\",image : \""+ getPath()+"department-white.png\"},\n";
        }
        return ret;

    }
    
    private String getPath(){
        String port = ( Executions.getCurrent().getServerPort() == 80 ) ? "" : (":" + Executions.getCurrent().getServerPort());
        return Executions.getCurrent().getScheme() + "://" + Executions.getCurrent().getServerName() + port + Executions.getCurrent().getContextPath()+"/images/staff/";
    }
}
