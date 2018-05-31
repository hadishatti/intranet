package qa.tecnositafgulf.viewmodel.companyInfo;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.sys.PageCtrl;
import qa.tecnositafgulf.viewmodel.IntranetVM;

/**
 * Created by hadi on 1/27/18.
 */
public class CompanyInfoVM extends IntranetVM {
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        init();
        addCommonTags((PageCtrl) view.getPage());
    }
}
