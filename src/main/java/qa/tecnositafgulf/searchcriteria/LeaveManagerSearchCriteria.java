package qa.tecnositafgulf.searchcriteria;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import qa.tecnositafgulf.model.administration.Profile;
import qa.tecnositafgulf.service.AdministrationService;
import qa.tecnositafgulf.spring.config.AppConfig;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class LeaveManagerSearchCriteria {

    private String likeEmployee;
    private String likeIsLeaveManager;
    private String likeIsHrManager;
    private String likeIsFinanceLeaveManager;
    private String likeIsTicketLeaveManager;
    private String likeLeaveManager;
    private String likeHrLeaveManager;
    private String likeFinanceLeaveManager;
    private String likeTicketLeaveManager;

    private Profile leaveManager;
    private Profile hrLeaveManager;
    private Profile financeLeaveManager;
    private Profile ticketLeaveManager;

    private static final int DEFAULTPAGESIZE = 10;
    private static final int DEFAULTPAGESTARTINDEX = 0;
    private static final String DEFAULTPAGEORDER = "employeeNumber";
    private static final String DEFAULTPAGEORDERMODE = "desc";

    private Integer startIndex = DEFAULTPAGESTARTINDEX;
    private Integer pageSize = DEFAULTPAGESIZE;
    private String pageOrderMode = DEFAULTPAGEORDERMODE;
    private String orderByFieldName = DEFAULTPAGEORDER;

    private AdministrationService administrationService;

    public Query toQuery(EntityManager entityManager){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT employee FROM Employee employee " +
                " ");
        Query query = addConstraint(entityManager, sb,
                new StringBuilder(" ORDER BY employee.").append(getOrderByFieldName()).append(" ").append(getPageOrderMode()).toString());

        if (this.getStartIndex() > 0) {
            query.setFirstResult(this.getStartIndex() * this.getPageSize());
        }
        if (this.getPageSize() > 0) {
            query.setMaxResults(this.getPageSize());
        }

        return query;
    }


    public Query getCountQuery(EntityManager em) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT COUNT(*) FROM Employee employee");
        Query query = addConstraint(em, sb, null);
        return query;
    }

    private Query addConstraint(EntityManager entityManager, StringBuilder sb, String orderBy){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        administrationService = context.getBean(AdministrationService.class);
        List<Profile> profiles = administrationService.listProfiles();

        List<String> conditions = new ArrayList<>();
        boolean isEmployee = (likeEmployee != null && !likeEmployee.isEmpty());
        boolean isLikeIsLeaveManager = (likeIsLeaveManager != null && !likeIsLeaveManager.isEmpty());
        boolean isLikeIsHrManager = (likeIsHrManager != null && !likeIsHrManager.isEmpty());
        boolean isLikeIsFinanceLeaveManager = (likeIsFinanceLeaveManager != null && !likeIsFinanceLeaveManager.isEmpty());
        boolean isLikeIsTicketLeaveManager = (likeIsTicketLeaveManager != null && !likeIsTicketLeaveManager.isEmpty());
        boolean isLeaveManager = (likeLeaveManager != null && !likeLeaveManager.isEmpty());
        boolean isHrLeaveManager = (likeHrLeaveManager != null && !likeHrLeaveManager.isEmpty()) ;
        boolean isFinanceLeaveManager = (likeFinanceLeaveManager != null && !likeFinanceLeaveManager.isEmpty());
        boolean isTicketLeaveManager = (likeTicketLeaveManager != null && !likeTicketLeaveManager.isEmpty());

        if(isEmployee)
            conditions.add("CONCAT(employee.employeeNumber, ' - ',  employee.name) like :likeEmployee");
        if(isLikeIsLeaveManager) {
            leaveManager = getProfile(profiles, "LM");
            conditions.add(":likeIsLeaveManager IN elements(employee.profiles)");
        }
        if(isLikeIsHrManager) {
            hrLeaveManager = getProfile(profiles, "HRLM");
            conditions.add(":likeIsHrManager IN elements(employee.profiles)");
        }
        if(isLikeIsFinanceLeaveManager) {
            financeLeaveManager = getProfile(profiles, "FLM");
            conditions.add(":likeIsFinanceLeaveManager IN elements(employee.profiles)");
        }
        if(isLikeIsTicketLeaveManager) {
            ticketLeaveManager = getProfile(profiles, "TLM");
            conditions.add(":likeIsTicketLeaveManager IN elements(employee.profiles)");
        }
        if(isLeaveManager) {
            if(likeLeaveManager.contains("NOT"))
                conditions.add("employee.leaveManagerEmployee is null ");
            else
                conditions.add("CONCAT(employee.leaveManagerEmployee.employeeNumber, ' - ', employee.leaveManagerEmployee.name) like :likeLeaveManager");
        }
        if(isHrLeaveManager) {
            if(likeHrLeaveManager.contains("NOT"))
                conditions.add("employee.HRLeaveManagerEmployee is null ");
            else
                conditions.add("CONCAT(employee.HRLeaveManagerEmployee.employeeNumber, ' - ', employee.HRLeaveManagerEmployee.name) like :likeHrLeaveManager");
        }
        if(isFinanceLeaveManager) {
            if(likeFinanceLeaveManager.contains("NOT"))
                conditions.add("employee.financeLeaveManagerEmployee is null ");
            else
                conditions.add("CONCAT(employee.financeLeaveManagerEmployee.employeeNumber, ' - ', employee.financeLeaveManagerEmployee.name) like :likeFinanceLeaveManager");
        }
        if(isTicketLeaveManager) {
            if(likeTicketLeaveManager.contains("NOT"))
                conditions.add("employee.ticketLeaveManagerEmployee is null ");
            else
                conditions.add("CONCAT(employee.ticketLeaveManagerEmployee.employeeNumber, ' - ', employee.ticketLeaveManagerEmployee.name) like :likeTicketLeaveManager");
        }

        for (int i = 0; i < conditions.size(); i++) {
            if(i == 0){
                sb.append(" where  ");
                sb.append(conditions.get(i));
            }else {
                sb.append(" and  ");
                sb.append(conditions.get(i));
            }
        }

        //order By condition
        if (orderBy != null && !orderBy.isEmpty()) {
            sb.append(orderBy);
        }

        System.out.println("Leave manager Query= "+sb.toString());

        // create query
        Query query = entityManager.createQuery(sb.toString());
        if(isEmployee)
            query.setParameter("likeEmployee", "%"+likeEmployee+"%");
        if(isLikeIsLeaveManager)
            query.setParameter("likeIsLeaveManager", leaveManager);
        if(isLikeIsHrManager)
            query.setParameter("likeIsHrManager", hrLeaveManager);
        if(isLikeIsFinanceLeaveManager)
            query.setParameter("likeIsFinanceLeaveManager", financeLeaveManager);
        if(isLikeIsTicketLeaveManager)
            query.setParameter("likeIsTicketLeaveManager", ticketLeaveManager);


        if(isLeaveManager)
            if(!likeFinanceLeaveManager.contains("NOT"))
            query.setParameter("likeLeaveManager", "%"+likeLeaveManager+"%");
        if(isHrLeaveManager)
            if(!likeHrLeaveManager.contains("NOT"))
            query.setParameter("likeHrLeaveManager","%"+likeHrLeaveManager+"%");
        if(isFinanceLeaveManager)
            if(!likeFinanceLeaveManager.contains("NOT"))
            query.setParameter("likeFinanceLeaveManager", "%"+likeFinanceLeaveManager+"%");
        if(isTicketLeaveManager)
            if(!likeTicketLeaveManager.contains("NOT"))
            query.setParameter("likeTicketLeaveManager","%"+likeTicketLeaveManager+"%");

        return query;
    }

    private Profile getProfile(List<Profile> profiles, String profileName){
        for (Profile p : profiles){
            if (p.getName().equals(profileName))
                return p;
        }
        return null;
    }

    public void clear() {
        startIndex = DEFAULTPAGESTARTINDEX;
        pageSize = DEFAULTPAGESIZE;
        pageOrderMode = DEFAULTPAGEORDERMODE;
        orderByFieldName = DEFAULTPAGEORDER;
    }

    public void clearFilters() {
        likeEmployee = null;
        likeIsLeaveManager = null;
        likeIsHrManager = null;
        likeIsFinanceLeaveManager = null;
        likeIsTicketLeaveManager = null;
        likeLeaveManager = null;
        likeHrLeaveManager = null;
        likeFinanceLeaveManager = null;
        likeTicketLeaveManager = null;
    }

    public String getLikeEmployee() {
        return likeEmployee;
    }

    public void setLikeEmployee(String likeEmployee) {
        this.likeEmployee = likeEmployee;
    }

    public String getLikeIsLeaveManager() {
        return likeIsLeaveManager;
    }

    public void setLikeIsLeaveManager(String likeIsLeaveManager) {
        this.likeIsLeaveManager = likeIsLeaveManager;
    }

    public String getLikeIsHrManager() {
        return likeIsHrManager;
    }

    public void setLikeIsHrManager(String likeIsHrManager) {
        this.likeIsHrManager = likeIsHrManager;
    }

    public String getLikeIsFinanceLeaveManager() {
        return likeIsFinanceLeaveManager;
    }

    public void setLikeIsFinanceLeaveManager(String likeIsFinanceLeaveManager) {
        this.likeIsFinanceLeaveManager = likeIsFinanceLeaveManager;
    }

    public String getLikeIsTicketLeaveManager() {
        return likeIsTicketLeaveManager;
    }

    public void setLikeIsTicketLeaveManager(String likeIsTicketLeaveManager) {
        this.likeIsTicketLeaveManager = likeIsTicketLeaveManager;
    }

    public String getLikeLeaveManager() {
        return likeLeaveManager;
    }

    public void setLikeLeaveManager(String likeLeaveManager) {
        this.likeLeaveManager = likeLeaveManager;
    }

    public String getLikeHrLeaveManager() {
        return likeHrLeaveManager;
    }

    public void setLikeHrLeaveManager(String likeHrLeaveManager) {
        this.likeHrLeaveManager = likeHrLeaveManager;
    }

    public String getLikeFinanceLeaveManager() {
        return likeFinanceLeaveManager;
    }

    public void setLikeFinanceLeaveManager(String likeFinanceLeaveManager) {
        this.likeFinanceLeaveManager = likeFinanceLeaveManager;
    }

    public String getLikeTicketLeaveManager() {
        return likeTicketLeaveManager;
    }

    public void setLikeTicketLeaveManager(String likeTicketLeaveManager) {
        this.likeTicketLeaveManager = likeTicketLeaveManager;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getPageOrderMode() {
        return pageOrderMode;
    }

    public void setPageOrderMode(String pageOrderMode) {
        this.pageOrderMode = pageOrderMode;
    }

    public String getOrderByFieldName() {
        return orderByFieldName;
    }

    public void setOrderByFieldName(String orderByFieldName) {
        this.orderByFieldName = orderByFieldName;
    }
}
