package qa.tecnositafgulf.viewmodel.home;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.web.fn.ServletFns;
import org.zkoss.web.servlet.Servlets;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import javax.servlet.ServletRequest;

public class SidebarVM  extends IntranetVM {
    private boolean floatMenuVisible = true;
    private boolean isMobile = false;
    private String orientation;
    private String windowMode = "embedded";
    private String menuAreaWidth = "200px";

    @AfterCompose
    public void init() {
        ServletRequest request = ServletFns.getCurrentRequest();
        // Detect if client is mobile device (such as Android or iOS devices)
        isMobile = Servlets.getBrowser(request, "mobile") != null;
        if (isMobile) {
            toFloatingMenu();
        }
    }

    @Command
    @NotifyChange("floatMenuVisible")
    public void toggleMenu() {
        this.floatMenuVisible = !floatMenuVisible;
    }

    @Command
    @NotifyChange({"windowMode", "menuAreaWidth", "floatMenuVisible"})
    public void updateClientInfo(@BindingParam("orientation") String orientation, @BindingParam("width") int width) {
        if (isMobile) {
            if (!orientation.equals(this.orientation)) {
                this.orientation = orientation;
                if (orientation.equals("protrait") || width < 800) {
                    toFloatingMenu();
                } else {
                    toFixedMenu();
                }
            }
        }
    }

    private void toFloatingMenu() {
        floatMenuVisible = false;
        windowMode = "overlapped";
        menuAreaWidth = "0px";
    }

    private void toFixedMenu() {
        floatMenuVisible = true;
        windowMode = "embedded";
        menuAreaWidth = "200px";
    }

    public boolean isFloatMenuVisible() {
        return floatMenuVisible;
    }

    public void setFloatMenuVisible(boolean floatMenuVisible) {
        this.floatMenuVisible = floatMenuVisible;
    }

    public boolean isMobile() {
        return isMobile;
    }

    public void setMobile(boolean mobile) {
        isMobile = mobile;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getWindowMode() {
        return windowMode;
    }

    public void setWindowMode(String windowMode) {
        this.windowMode = windowMode;
    }

    public String getMenuAreaWidth() {
        return menuAreaWidth;
    }

    public void setMenuAreaWidth(String menuAreaWidth) {
        this.menuAreaWidth = menuAreaWidth;
    }
}
