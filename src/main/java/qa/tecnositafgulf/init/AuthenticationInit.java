package qa.tecnositafgulf.init;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Initiator;
import qa.tecnositafgulf.model.administration.Employee;

import java.util.Map;

/**
 * Created by hadi on 12/18/17.
 */
public class AuthenticationInit implements Initiator{
    public void doInit(Page page, Map<String, Object> args) throws Exception {
        Employee employee = (Employee) Sessions.getCurrent().getAttribute("employee");
        if(employee==null){
            Sessions.getCurrent().setAttribute("path",page.getDesktop().getRequestPath());
            Executions.sendRedirect("/pages/login/login.zul");
        }
    }
}
