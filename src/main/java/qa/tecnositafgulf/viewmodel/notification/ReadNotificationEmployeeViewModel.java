package qa.tecnositafgulf.viewmodel.notification;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.*;
import qa.tecnositafgulf.config.MyProperties;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.common.Notification;
import qa.tecnositafgulf.service.AuthenticationService;
import qa.tecnositafgulf.spring.config.AppConfig;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hadi on 1/3/18.
 */
public class ReadNotificationEmployeeViewModel {
    private Timer timer;
    private List<Notification> notifications;
    private Employee employee;
    private Desktop desktop;
    public AuthenticationService authenticationService;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        authenticationService = context.getBean(AuthenticationService.class);
        desktop = Executions.getCurrent().getDesktop();
        employee = (Employee) Sessions.getCurrent().getAttribute("employee");
        if(timer!=null)
            timer.cancel();
        timer = new Timer();
        timer.schedule(readNotifications(),0,1000);
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    @NotifyChange({"notifications"})
    public void loadNotifications(){
        notifications = authenticationService.listNotificationsForEmployee(employee);
        BindUtils.postNotifyChange(null, null, this, "notifications");
    }

    @Command
    public void view(@BindingParam("item") Notification not){
        authenticationService.markNotificationAsRead(not);
        if(timer!=null)
            timer.cancel();
        Executions.sendRedirect("/pages/hr/viewLeaveRequests.zul");
    }

    public String getImagePath(){
        return MyProperties.getInstance().getImagePath();
    }

    @Command
    public void remove(@BindingParam("item") Notification not){
        authenticationService.markNotificationAsRead(not);
    }

    private TimerTask readNotifications(){
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    if(desktop == null) {
                        timer.cancel();
                        return;
                    }
                    Executions.activate(desktop);
                    try {
                        loadNotifications();
                    } finally {
                        Executions.deactivate(desktop);
                    }
                }catch(DesktopUnavailableException ex) {
                    System.out.println("Desktop currently unavailable");
                    timer.cancel();
                }catch(InterruptedException e) {
                    System.out.println("The server push thread interrupted");
                    timer.cancel();
                }
            }
        };
    }
}
