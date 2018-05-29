package qa.tecnositafgulf.searchcriteria.hr;

import qa.tecnositafgulf.config.LeaveRequestStates;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.service.LeaveRequestService;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by hadi on 5/19/18.
 */
public class LeaveRequestSearchCriteria {

    private String likeLeaveId;
    private Date createdOnFrom;
    private Date createdOnTo;
    private Date leaveFromFrom;
    private Date leaveFromTo;
    private Date leaveToFrom;
    private Date leaveToTo;
    private String type;
    private String tickets;
    private String status;
    private Date approvedOnFrom;
    private Date approvedOnTo;
    private Employee applicant;
    private List<Employee> applicants;
    private Employee approver;

    private static final int DEFAULTPAGESIZE = 3;
    private static final int DEFAULTPAGESTARTINDEX = 0;
    private static final String DEFAULTPAGEORDER = "createdOn";
    private static final String DEFAULTPAGEORDERMODE = "desc";

    private Integer startIndex = DEFAULTPAGESTARTINDEX;
    private Integer pageSize = DEFAULTPAGESIZE;
    private String pageOrderMode = DEFAULTPAGEORDERMODE;
    private String orderByFieldName = DEFAULTPAGEORDER;

    private LeaveRequestService service;

    public LeaveRequestSearchCriteria(){
        applicant = new Employee();
        applicant.setName("All");
        applicants = new ArrayList<>();
        type = "All";
        tickets = "Both";
        status = "All";


    }

    public Query toQuery(EntityManager entityManager){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT leaveRequest FROM LeaveRequest leaveRequest " +
                "WHERE leaveRequest.status IN (SELECT MAX(lr.status) FROM LeaveRequest lr WHERE leaveRequest.number = lr.number GROUP BY lr.number) " +
                "AND leaveRequest.applicant = :applicant ");
        Query query = addConstraint(entityManager, sb,
                new StringBuilder(" ORDER BY leaveRequest.").append(getOrderByFieldName()).append(" ").append(getPageOrderMode()).toString());

        if(this.getStartIndex() > 0){
            query.setFirstResult(this.getStartIndex() * this.getPageSize());
        }
        if (this.getPageSize() > 0) {
            query.setMaxResults(this.getPageSize());
        }

        return query;
    }

    public Query getCountQuery(EntityManager em) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT COUNT(leaveRequest) FROM LeaveRequest leaveRequest " +
                "WHERE leaveRequest.status IN (SELECT MAX(lr.status) FROM LeaveRequest lr WHERE leaveRequest.number = lr.number GROUP BY lr.number) " +
                "AND leaveRequest.applicant = :applicant " +
                "AND leaveRequest.status <> 11 AND leaveRequest.status <> 12 AND leaveRequest.status <> 1");
        Query query = addConstraint(em, sb, null);
        return query;
    }

    public Query toQuery1(EntityManager entityManager){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT leaveRequest FROM LeaveRequest leaveRequest " +
                "WHERE leaveRequest.status IN (SELECT MAX(lr.status) FROM LeaveRequest lr WHERE leaveRequest.number = lr.number GROUP BY lr.number) " +
                "AND leaveRequest.status <> 11 AND leaveRequest.status <> 12 AND leaveRequest.status <> 1");
        Query query = addConstraint(entityManager, sb,
                new StringBuilder(" ORDER BY leaveRequest.").append(getOrderByFieldName()).append(" ").append(getPageOrderMode()).toString());

        if(this.getStartIndex() > 0){
            query.setFirstResult(this.getStartIndex() * this.getPageSize());
        }
        if (this.getPageSize() > 0) {
            query.setMaxResults(this.getPageSize());
        }

        return query;
    }

    public Query getCountQuery1(EntityManager em) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT COUNT(leaveRequest) FROM LeaveRequest leaveRequest " +
                "WHERE leaveRequest.status IN (SELECT MAX(lr.status) FROM LeaveRequest lr WHERE leaveRequest.number = lr.number GROUP BY lr.number) " +
                "AND leaveRequest.status <> 11 AND leaveRequest.status <> 12 AND leaveRequest.status <> 1");
        Query query = addConstraint(em, sb, null);
        return query;
    }

    public Query toQuery2(EntityManager entityManager){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT leaveRequest FROM LeaveRequest leaveRequest WHERE leaveRequest.status IN (SELECT MAX(lr.status) FROM LeaveRequest lr WHERE leaveRequest.number = lr.number GROUP BY lr.number) AND leaveRequest.status <> 11 AND leaveRequest.status <> 12 AND leaveRequest.status <> 13 AND leaveRequest.status <> 2 AND leaveRequest.status <> 4 AND leaveRequest.status <> 6 AND leaveRequest.status <> 9 AND leaveRequest.status <> 10");
        Query query = addConstraint(entityManager, sb,
                new StringBuilder(" ORDER BY leaveRequest.").append(getOrderByFieldName()).append(" ").append(getPageOrderMode()).toString());

        if(this.getStartIndex() > 0){
            query.setFirstResult(this.getStartIndex() * this.getPageSize());
        }
        if (this.getPageSize() > 0) {
            query.setMaxResults(this.getPageSize());
        }

        return query;
    }

    public Query getCountQuery2(EntityManager em) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT count(leaveRequest) FROM LeaveRequest leaveRequest WHERE leaveRequest.status IN (SELECT MAX(lr.status) FROM LeaveRequest lr WHERE leaveRequest.number = lr.number GROUP BY lr.number) AND leaveRequest.status <> 11 AND leaveRequest.status <> 12 AND leaveRequest.status <> 13 AND leaveRequest.status <> 2 AND leaveRequest.status <> 4 AND leaveRequest.status <> 6 AND leaveRequest.status <> 9 AND leaveRequest.status <> 10");
        Query query = addConstraint(em, sb, null);
        return query;
    }

    public Query toQuery3(EntityManager entityManager){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT leaveRequest FROM LeaveRequest leaveRequest WHERE leaveRequest.status IN (SELECT MAX(lr.status) FROM LeaveRequest lr WHERE leaveRequest.number = lr.number GROUP BY lr.number) AND leaveRequest.status = 11");
        Query query = addConstraint(entityManager, sb,
                new StringBuilder(" ORDER BY leaveRequest.").append(getOrderByFieldName()).append(" ").append(getPageOrderMode()).toString());

        if(this.getStartIndex() > 0){
            query.setFirstResult(this.getStartIndex() * this.getPageSize());
        }
        if (this.getPageSize() > 0) {
            query.setMaxResults(this.getPageSize());
        }

        return query;
    }

    public Query getCountQuery3(EntityManager em) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT count(leaveRequest) FROM LeaveRequest leaveRequest WHERE leaveRequest.status IN (SELECT MAX(lr.status) FROM LeaveRequest lr WHERE leaveRequest.number = lr.number GROUP BY lr.number) AND leaveRequest.status = 11");
        Query query = addConstraint(em, sb, null);
        return query;
    }

    public Query toQuery4(EntityManager entityManager){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT leaveRequest FROM LeaveRequest leaveRequest WHERE leaveRequest.status IN (SELECT MAX(lr.status) FROM LeaveRequest lr WHERE leaveRequest.number = lr.number GROUP BY lr.number) AND leaveRequest.status = 12");
        Query query = addConstraint(entityManager, sb,
                new StringBuilder(" ORDER BY leaveRequest.").append(getOrderByFieldName()).append(" ").append(getPageOrderMode()).toString());

        if(this.getStartIndex() > 0){
            query.setFirstResult(this.getStartIndex() * this.getPageSize());
        }
        if (this.getPageSize() > 0) {
            query.setMaxResults(this.getPageSize());
        }

        return query;
    }

    public Query getCountQuery4(EntityManager em) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT count(leaveRequest) FROM LeaveRequest leaveRequest WHERE leaveRequest.status IN (SELECT MAX(lr.status) FROM LeaveRequest lr WHERE leaveRequest.number = lr.number GROUP BY lr.number) AND leaveRequest.status = 12");
        Query query = addConstraint(em, sb, null);
        return query;
    }

    private Query addConstraint(EntityManager entityManager, StringBuilder sb, String orderBy){

        List<String> conditions = new ArrayList<>();

        boolean isApplicant = (applicant.getId()!=null);
        boolean isApplicants = (!applicants.isEmpty());
        boolean isApprover = (approver !=null);
        boolean isLikeLeaveId = (likeLeaveId != null);
        boolean isCreatedOnFrom = (createdOnFrom != null);
        boolean isCreatedOnTo = (createdOnTo != null);
        boolean isLeaveFromFrom = (leaveFromFrom != null);
        boolean isLeaveFromTo = (leaveFromTo != null);
        boolean isLeaveToFrom = (leaveToFrom != null);
        boolean isLeaveToTo = (leaveToTo != null);
        boolean isType = (!type.equals("All"));
        boolean isTickets = (!tickets.equals("Both"));
        boolean isStatus = (!status.equals("All"));
        boolean isApprovedOnFrom = (approvedOnFrom != null);
        boolean isApprovedOnTo = (approvedOnTo != null);

        if(isApplicant)
            conditions.add("leaveRequest.applicant = :applicant");
        else if(isApplicants)
            conditions.add("leaveRequest.applicant IN (:applicants)");
        if(isApprover)
            conditions.add("leaveRequest.approver = :approver");
        if(isLikeLeaveId)
            conditions.add("leaveRequest.number like :likeLeaveId");
        if(isCreatedOnFrom)
            conditions.add("leaveRequest.createdOn >= :createdOnFrom");
        if(isCreatedOnTo)
            conditions.add("leaveRequest.createdOn <= :createdOnTo");
        if(isLeaveFromFrom)
            conditions.add("leaveRequest.leaveFrom >= :leaveFromFrom");
        if(isLeaveFromTo)
            conditions.add("leaveRequest.leaveFrom <= :leaveFromTo");
        if(isLeaveToFrom)
            conditions.add("leaveRequest.leaveTo >= :leaveToFrom");
        if(isLeaveToTo)
            conditions.add("leaveRequest.leaveTo <= :leaveToTo");
        if(isType)
            conditions.add("leaveRequest.type = :type");
        if(isTickets)
            conditions.add("leaveRequest.tickets = :ticket");
        if(isStatus)
            conditions.add("leaveRequest.status = :status");
        if(isApprovedOnFrom)
            conditions.add("leaveRequest.approvedOn >= :approvedOnFrom");
        if(isApprovedOnTo)
            conditions.add("leaveRequest.approvedOn <= :approvedOnTo");

        for (int i = 0; i < conditions.size(); i++) {
            sb.append(" and  ");
            sb.append(conditions.get(i));
        }

        //order By condition
        if (orderBy != null && !orderBy.isEmpty())
            sb.append(orderBy);
        // create query
        Query query = entityManager.createQuery(sb.toString());
        if(isApplicant)
            query.setParameter("applicant",applicant);
        else if(isApplicants)
            query.setParameter("applicants",applicants);
        if(isApprover)
            query.setParameter("approver",approver);
        if(isLikeLeaveId)
            query.setParameter("likeLeaveId","%"+likeLeaveId+"%");
        if(isCreatedOnFrom) {
            Calendar c = Calendar.getInstance();
            c.setTime(createdOnFrom);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            createdOnFrom = c.getTime();
            query.setParameter("createdOnFrom", createdOnFrom);
        }
        if(isCreatedOnTo) {
            Calendar c = Calendar.getInstance();
            c.setTime(createdOnTo);
            c.set(Calendar.HOUR_OF_DAY, 23);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.SECOND, 59);
            c.set(Calendar.MILLISECOND, 59);
            createdOnTo = c.getTime();
            query.setParameter("createdOnTo", createdOnTo);
        }
        if(isLeaveFromFrom) {
            Calendar c = Calendar.getInstance();
            c.setTime(leaveFromFrom);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            leaveFromFrom = c.getTime();
            query.setParameter("leaveFromFrom", leaveFromFrom);
        }
        if(isLeaveFromTo) {
            Calendar c = Calendar.getInstance();
            c.setTime(leaveFromTo);
            c.set(Calendar.HOUR_OF_DAY, 23);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.SECOND, 59);
            c.set(Calendar.MILLISECOND, 59);
            leaveFromTo = c.getTime();
            query.setParameter("leaveFromTo", leaveFromTo);
        }
        if(isLeaveToFrom) {
            Calendar c = Calendar.getInstance();
            c.setTime(leaveToFrom);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            leaveToFrom = c.getTime();
            query.setParameter("leaveToFrom", leaveToFrom);
        }
        if(isLeaveToTo) {
            Calendar c = Calendar.getInstance();
            c.setTime(leaveToTo);
            c.set(Calendar.HOUR_OF_DAY, 23);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.SECOND, 59);
            c.set(Calendar.MILLISECOND, 59);
            leaveToTo = c.getTime();
            query.setParameter("leaveToTo", leaveToTo);
        }
        if(isType)
            query.setParameter("type",type);
        if(isTickets)
            query.setParameter("ticket",tickets.equals("Yes"));
        if(isStatus)
            query.setParameter("status", LeaveRequestStates.toInt(status));
        if(isApprovedOnFrom) {
            Calendar c = Calendar.getInstance();
            c.setTime(approvedOnFrom);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            approvedOnFrom = c.getTime();
            query.setParameter("approvedOnFrom", approvedOnFrom);
        }
        if(isApprovedOnTo) {
            Calendar c = Calendar.getInstance();
            c.setTime(approvedOnTo);
            c.set(Calendar.HOUR_OF_DAY, 23);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.SECOND, 59);
            c.set(Calendar.MILLISECOND, 59);
            approvedOnTo = c.getTime();
            query.setParameter("approvedOnTo", approvedOnTo);
        }
        return query;
    }

    public void clear() {
        startIndex = DEFAULTPAGESTARTINDEX;
        pageSize = DEFAULTPAGESIZE;
        pageOrderMode = DEFAULTPAGEORDERMODE;
        orderByFieldName = DEFAULTPAGEORDER;
    }

    public void clearFilters() {
        applicant = new Employee();
        applicant.setName("All");
        applicants = new ArrayList<>();
        approver=null;
        likeLeaveId = null;
        createdOnFrom = null;
        createdOnTo = null;
        leaveFromFrom = null;
        leaveFromTo = null;
        leaveToFrom = null;
        leaveToTo = null;
        type = "All";
        tickets = "Both";
        status = "All";
        approvedOnFrom = null;
        approvedOnTo = null;
    }

    public Employee getApplicant() {
        return applicant;
    }

    public void setApplicant(Employee applicant) {
        this.applicant = applicant;
    }

    public List<Employee> getApplicants() {
        return applicants;
    }

    public void setApplicants(List<Employee> applicants) {
        this.applicants = applicants;
    }

    public Employee getApprover() {
        return approver;
    }

    public void setApprover(Employee approver) {
        this.approver = approver;
    }

    public String getLikeLeaveId() {
        return likeLeaveId;
    }

    public void setLikeLeaveId(String likeLeaveId) {
        this.likeLeaveId = likeLeaveId;
    }

    public Date getCreatedOnFrom() {
        return createdOnFrom;
    }

    public void setCreatedOnFrom(Date createdOnFrom) {
        this.createdOnFrom = createdOnFrom;
    }

    public Date getCreatedOnTo() {
        return createdOnTo;
    }

    public void setCreatedOnTo(Date createdOnTo) {
        this.createdOnTo = createdOnTo;
    }

    public Date getLeaveFromFrom() {
        return leaveFromFrom;
    }

    public void setLeaveFromFrom(Date leaveFromFrom) {
        this.leaveFromFrom = leaveFromFrom;
    }

    public Date getLeaveFromTo() {
        return leaveFromTo;
    }

    public void setLeaveFromTo(Date leaveFromTo) {
        this.leaveFromTo = leaveFromTo;
    }

    public Date getLeaveToFrom() {
        return leaveToFrom;
    }

    public void setLeaveToFrom(Date leaveToFrom) {
        this.leaveToFrom = leaveToFrom;
    }

    public Date getLeaveToTo() {
        return leaveToTo;
    }

    public void setLeaveToTo(Date leaveToTo) {
        this.leaveToTo = leaveToTo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTickets() {
        return tickets;
    }

    public void setTickets(String tickets) {
        this.tickets = tickets;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getApprovedOnFrom() {
        return approvedOnFrom;
    }

    public void setApprovedOnFrom(Date approvedOnFrom) {
        this.approvedOnFrom = approvedOnFrom;
    }

    public Date getApprovedOnTo() {
        return approvedOnTo;
    }

    public void setApprovedOnTo(Date approvedOnTo) {
        this.approvedOnTo = approvedOnTo;
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

    public LeaveRequestService getService() {
        return service;
    }

    public void setService(LeaveRequestService service) {
        this.service = service;
    }
}
