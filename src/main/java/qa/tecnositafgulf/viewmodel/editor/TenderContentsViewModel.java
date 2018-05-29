package qa.tecnositafgulf.viewmodel.editor;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.util.Map;

public class TenderContentsViewModel  extends IntranetVM {

    private String contents;


    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
        Map map = Executions.getCurrent().getArg();
        this.contents = (String) map.get("htmlContents");
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
