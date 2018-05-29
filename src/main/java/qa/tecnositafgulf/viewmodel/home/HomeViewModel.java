package qa.tecnositafgulf.viewmodel.home;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.web.fn.ServletFns;
import org.zkoss.web.servlet.Servlets;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import javax.servlet.ServletRequest;

public class HomeViewModel extends IntranetVM {

	private String welcome;
	private boolean floatingMenuVisible = true;
	private boolean isMobile = false;
	private String orientation;
	private String windowMode = "embedded";
	private String menuAreaWidth= "200px";
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		init();
		Selectors.wireComponents(view, this, false);
		Employee employee = (Employee) Sessions.getCurrent().getAttribute("employee");
		welcome = "Hello, "+employee.getName()+"! Welcome in the Gulf Business Development Group's Intranet!";
        ServletRequest request = ServletFns.getCurrentRequest();
        // Detect if client is mobile device (such as Android or iOS devices)
        isMobile = Servlets.getBrowser(request, "mobile") != null;
	}



	public String getWelcome() {
		return welcome;
	}

	public void setWelcome(String welcome) {
		this.welcome = welcome;
	}

	@Override
	public boolean isMobile() {
		return isMobile;
	}

	@Override
	public void setMobile(boolean mobile) {
		isMobile = mobile;
	}

    /*    @Command
    @NotifyChange("floatingMenuVisible")
    public void toggleMenu(){
        floatingMenuVisible = !floatingMenuVisible;
    }

    @Command
    @NotifyChange({"windowMode","menuAreaWidth","floatingMenuVisible"})
    public void updateClientInfo(@BindingParam("orientation") String orientation, @BindingParam("width")int width ){

        if (isMobile){
            if (!orientation.equals(this.orientation)){
                this.orientation = orientation;
                if (orientation.equals("protrait") || width < 800){
                    toFloatingMenu();
                }else{
                    toFixedMenu();
                }
            }
        }
    }*/

/*    private void toFloatingMenu(){
        floatingMenuVisible = false;
        windowMode = "overlapped";
        menuAreaWidth = "0px";
    }

    private void toFixedMenu(){
        floatingMenuVisible = true;
        windowMode = "embedded";
        menuAreaWidth = "200px";
    }


	public String getWelcome() {
		return welcome;
	}

	public void setWelcome(String welcome) {
		this.welcome = welcome;
	}

    @Override
    public boolean isFloatingMenuVisible() {
        return floatingMenuVisible;
    }

    @Override
    public void setFloatingMenuVisible(boolean floatingMenuVisible) {
        this.floatingMenuVisible = floatingMenuVisible;
    }

    @Override
    public String getOrientation() {
        return orientation;
    }

    @Override
    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    @Override
    public String getWindowMode() {
        return windowMode;
    }

    @Override
    public void setWindowMode(String windowMode) {
        this.windowMode = windowMode;
    }

    @Override
    public String getMenuAreaWidth() {
        return menuAreaWidth;
    }

    @Override
    public void setMenuAreaWidth(String menuAreaWidth) {
        this.menuAreaWidth = menuAreaWidth;
    }*/
}
