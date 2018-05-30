package qa.tecnositafgulf.viewmodel.helpDesk;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.sys.PageCtrl;
import org.zkoss.zk.ui.util.Clients;
import qa.tecnositafgulf.config.MyProperties;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.service.AdministrationService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ledio on 5/19/18.
 */
public class ContactAdminVM extends IntranetVM{
    private List<Employee> employeeList;
    private AdministrationService service;
    private List<Employee> administrators;

    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
        init();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        service = context.getBean(AdministrationService.class);
        employeeList = service.listEmployees();

        administrators = new ArrayList<>();

        for (Employee employee : employeeList){
            if (authenticationService.isProfilePresent(employee, "Admin"))
                administrators.add(employee);
        }

        showAdministrators();
        addCommonTags((PageCtrl) view.getPage());
    }

    public void showAdministrators(){
        String html = "";

        if (!administrators.isEmpty() && administrators != null)
            for(Employee employee : administrators){
                html += "<div class=\"col-xs-12 col-sm-6 col-md-6\">" +
                        "            <div class=\"well well-sm\">" +
                        "                <div class=\"row\">" +
                        "                    <div class=\"col-sm-6 col-md-4\">" +
                        "                        <img src=\""+MyProperties.getInstance().getImagePath()+"/staff/"+employee.getImage()+"\" alt=\"\" class=\"img-rounded img-responsive\" />" + //TODO update image path once config is on server
                        "                    </div>" +
                        "                    <div class=\"col-sm-6 col-md-8\">" +
                        "                        <h4 style=\"color: #1a4280\">" +
                        "                            "+employee.getName()+" "+employee.getFamilyName()+"</h4>" +
                        "                        <small><cite title=\"Doha, Qatar\">Doha, Qatar <i class=\"glyphicon glyphicon-map-marker\">" +
                        "                        </i></cite></small>" +
                        "                        <p>" +
                        "                            <i class=\"glyphicon glyphicon-phone\"></i><a href=\"tel:"+employee.getOfficePhoneNumber()+"\">"+employee.getOfficePhoneNumber()+"</a>" +
                        "                            <br />" +
                        "                            <i class=\"glyphicon glyphicon-envelope\"></i><a href=\"mailto:"+employee.getMail1()+"\">"+employee.getMail1()+"</a>" +
                        "                            <br />";
                                                    if (employee.getMail2() != null && !employee.getMail2().isEmpty())
                                                        html += "<i class=\"glyphicon glyphicon-envelope\"></i><a href=\"mailto:"+employee.getMail2()+"\">"+employee.getMail2()+"</a>" +
                                                        "<br />";
                                                    if (employee.getMail3() != null && !employee.getMail3().isEmpty())
                                                         html += "<i class=\"glyphicon glyphicon-envelope\"></i><a href=\"mailto:"+employee.getMail3()+"\">"+employee.getMail3()+"</a>";

                html += "                    </div>" +
                        "                </div>" +
                        "            </div>" +
                        "        </div>";
            }

        String script = "";

        if (!html.isEmpty()) {
            script += "document.getElementById(\"administrators\").innerHTML = '" + html + "';";
        }
        Clients.evalJavaScript(script);
    }

    public List<Employee> getAdministrators() {
        return administrators;
    }
}
