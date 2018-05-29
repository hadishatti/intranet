package qa.tecnositafgulf.init;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Initiator;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.administration.Profile;

import java.util.Map;

/**
 * Created by ameljo on 07/05/18.
 */
public class AdministratorInit implements Initiator {
    @Override
    public void doInit(Page page, Map<String, Object> map) throws Exception {
        Employee employee = (Employee) Sessions.getCurrent().getAttribute("employee");
        if(employee != null) {
            boolean isAdminnistrator = false;
            for(Profile profile : employee.getProfiles())
                if (profile.getName().equals("Admin"))
                    isAdminnistrator = true;

            if (!isAdminnistrator) {
                Sessions.getCurrent().setAttribute("path", page.getDesktop().getRequestPath());
                Executions.sendRedirect("/pages/home.zul");
            }
        } else {
            Sessions.getCurrent().setAttribute("path", page.getDesktop().getRequestPath());
            Executions.sendRedirect("/pages/login/login.zul");
        }
    }
}
