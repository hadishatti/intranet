package qa.tecnositafgulf.viewmodel.home;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Div;
import qa.tecnositafgulf.config.MyProperties;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.viewmodel.IntranetVM;

/**
 * Created by hadi on 1/23/18.
 */
public class TitlebarViewModel extends IntranetVM {

    String MOBILE = "mobile";
    String DESKTOP = "desktop";
    String template = "menu";

    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
        init();
    }

    public String getImage(){
        Employee e = ((Employee) Sessions.getCurrent().getAttribute("employee"));
        return MyProperties.getInstance().getImagePath()+"/staff/"+(e.getImage()==null?"userpic.png":e.getImage());
    }

    @MatchMedia("all and (max-width: 500px)")
    public void handleMax500() {
        switchTemplate(MOBILE);
    }

    @MatchMedia("all and (min-width: 501px)")
    public void handleMin500() {
        switchTemplate(DESKTOP);
    }
    @Command
    public void setResponseDesign(@SelectorParam("#myTopnav") Div div){
            if(div != null){
                String className = div.getSclass();
                if(className.equals("topnav")){
                    className += " responsive";
                }else {
                    className = "topnav";
                }
                div.setClass(className);
                String command = "changeCss('" + className + "')";
                Clients.evalJavaScript("changeCss");
            }
    }

    public void switchTemplate(String newTemplate) {
        if (template == null || !newTemplate.equals(template)) {
            this.template = newTemplate;
            BindUtils.postNotifyChange(null, null, this, "template");
            BindUtils.postNotifyChange(null, null, this, "mobile");
        }
    }

}
