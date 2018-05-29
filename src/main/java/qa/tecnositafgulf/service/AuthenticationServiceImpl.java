package qa.tecnositafgulf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import qa.tecnositafgulf.dao.administration.employees.EmployeeDao;
import qa.tecnositafgulf.dao.common.NotificationDao;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.administration.Profile;
import qa.tecnositafgulf.model.common.Notification;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private NotificationDao notificationDao;

    Employee employee = (Employee) Sessions.getCurrent().getAttribute("employee");

    public boolean login(String username, String password) {
        Employee employee = employeeDao.findByUsername(username);
        if(employee==null || !employee.getPassword().equals(employeeDao.MD5(password)))
            return false;
        Session session = Sessions.getCurrent();
        session.setAttribute("employee",employee);
        return true;
    }

    public void logout() {
        Session session = Sessions.getCurrent();
        session.removeAttribute("employee");
        session.removeAttribute("path");
    }

    public List<Notification> listNotificationsForEmployee(Employee recipient) {
        return notificationDao.listForEmployeeByRecipient(recipient);
    }

    public List<Notification> listNotificationsForManager(Employee recipient) {
        return notificationDao.listForManagerByRecipient(recipient);
    }

    public boolean isProfilePresent(Employee employee, String profile){
        boolean present = false;
        for (Profile p : employee.getProfiles()){
            if (p.getName().equals(profile))
                present = true;
        }

        return present;
    }

//    public boolean isLeaveManager(){ return isProfilePresent(employee, "LM"); }

    public boolean isAdministrator() {
        return isProfilePresent(employee, "Admin");
    }

//    public boolean isHRApprover() {
//        return isProfilePresent(employee, "HRLM");
//    }
//
//    public boolean isFinanceApprover() {
//        return isProfilePresent(employee, "FLM");
//    }

    public boolean isPublisher() {
        if(isProfilePresent(employee, "Admin") || isProfilePresent(employee, "Publisher"))
            return true;
        return false;
    }

//    public boolean isTicketManager() {
//        return isProfilePresent(employee, "TLM");
//    }

    public void markNotificationAsRead(Notification n){
        n.setReadOn(new Date());
        notificationDao.save(n);
    }


}
