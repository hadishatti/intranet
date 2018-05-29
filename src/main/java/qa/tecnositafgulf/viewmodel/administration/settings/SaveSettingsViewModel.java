package qa.tecnositafgulf.viewmodel.administration.settings;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Messagebox;
import qa.tecnositafgulf.config.MyProperties;
import qa.tecnositafgulf.model.properties.Property;
import qa.tecnositafgulf.viewmodel.IntranetVM;

/**
 * Created by klajdi on 26/05/18.
 */
public class SaveSettingsViewModel extends IntranetVM {

    private Property property;
    private String propertyPath;

    @AfterCompose
    public void doAfterCompose(@ExecutionArgParam("propertyToModify") Property propertyToModify){
        init();
        if(propertyToModify != null){
            property = propertyToModify;
        }else{
            property = new Property();
        }
    }

    @Command
    @NotifyChange("property")
    public void save(){
        try {
            propertyPath = formatPath(property.getValue());
            property.setValue(propertyPath);
            MyProperties.getInstance().saveProperty(property);
            Executions.sendRedirect("/pages/administration/settings/settings.zul");
        }catch (Exception e){
            Messagebox.show("Property with same Key already exists!!", "Warning", Messagebox.OK, Messagebox.ERROR);
        }
    }

    private String formatPath(String path) {
        String ret = "";

        if(path.substring(path.length() - 1).equals("/"))
            ret = path.substring(0, path.length() - 1);
        else
            ret = path;

        return ret;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }
}
