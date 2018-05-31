package qa.tecnositafgulf.viewmodel.login;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.web.fn.ServletFns;
import org.zkoss.web.servlet.Servlets;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.sys.PageCtrl;
import org.zkoss.zk.ui.util.Clients;
import qa.tecnositafgulf.config.MyProperties;
import qa.tecnositafgulf.service.AuthenticationService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import javax.servlet.ServletRequest;


/**
 * Created by hadi on 12/18/17.
 */
public class LoginViewModel extends IntranetVM {

    private String username;
    private String password;
    private String error;
    private AuthenticationService service;
    private ServletRequest request = ServletFns.getCurrentRequest();
    private String css;
    private boolean mobile;
    private String imagePath;

    @Init
    public void init() {
        imagePath = MyProperties.getInstance().getImagePath();
        // Detect if client is mobile (such as Android or iOS devices)
        mobile = Servlets.getBrowser(request, "mobile") != null;

        // Set the default stylesheet
        if (mobile) {
            css =MyProperties.getInstance().getResourcePath() + "/css/tablet.css.dsp";
        } else {
            css =MyProperties.getInstance().getResourcePath() +  "/css/desktop.css.dsp";
        }
    }
    
    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
        Double ie = Servlets.getBrowser(request, "ie");
        if (ie != null && ie < 8.0) {
            Clients.showNotification("This demo does not support IE6/7", true);
        }
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        service = context.getBean(AuthenticationService.class);
        error = "";
        addCommonTags((PageCtrl) view.getPage());
    }

    @NotifyChange("error")
    @Command
    public void login(){
        if(username==null || username.isEmpty() || password==null || password.isEmpty()){
            error = "Invalid Credentials";
            Clients.evalJavaScript("loginFailed();");
            return;
        }
        boolean loggedIn = false;
        try {
            loggedIn = service.login(username, password);
        }catch(Exception e){
            loggedIn = false;
        }
        if (loggedIn) {
            String path = (String) Sessions.getCurrent().getAttribute("path");
            if (path == null)
                Executions.sendRedirect("/pages/home.zul");
            else
                Executions.sendRedirect(path);
        }else {
            error = "Invalid Credentials";
            Clients.evalJavaScript("loginFailed();");
            //return;
        }

    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = css;
    }
}
