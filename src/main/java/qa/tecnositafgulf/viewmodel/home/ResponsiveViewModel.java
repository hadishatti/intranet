package qa.tecnositafgulf.viewmodel.home;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.A;
import org.zkoss.zul.Div;
import org.zkoss.zul.Menubar;
import qa.tecnositafgulf.viewmodel.IntranetVM;

public class ResponsiveViewModel   extends IntranetVM {

    private  Div div;
    private A anchor;
    private boolean toggle=true;


    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){

    }
    @Command
    public void onClickIcon(@SelectorParam("#menus") Div div, @SelectorParam("#menubar") Menubar menubar) throws Exception{
        if(toggle){
            menubar.setOrient("vertical");
            menubar.setSclass("toggle");
            toggle = false;
        }else {
            menubar.setOrient("horizontal");
            menubar.setSclass("display: none;");
            toggle = true;

        }
    }

    public Div getDiv() {
        return div;
    }

    public void setDiv(Div div) {
        this.div = div;
    }

    public A getAnchor() {
        return anchor;
    }

    public void setAnchor(A anchor) {
        this.anchor = anchor;
    }
}
