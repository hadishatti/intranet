package qa.tecnositafgulf.searchcriteria;

import qa.tecnositafgulf.config.ProjectsStatusEnum;
import qa.tecnositafgulf.model.administration.Employee;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ameljo on 5/19/18.
 */
public class ProjectSearchCriteria implements Serializable {

    private String likeName;
    private String likeCode;
    private String likeDesc;
    private Employee likeEmployee;
    private Date likeStartingFrom;
    private Date likeStartingTo;
    private ProjectsStatusEnum likeStatus;
    private Date likeEndingFrom;
    private Date likeEndingTo;

    private static final int DEFAULTPAGESIZE = 10;
    private static final int DEFAULTPAGESTARTINDEX = 0;
    private static final String DEFAULTPAGEORDER = "name";
    private static final String DEFAULTPAGEORDERMODE = "desc";

    private Integer startIndex = DEFAULTPAGESTARTINDEX;
    private Integer pageSize = DEFAULTPAGESIZE;
    private String pageOrderMode = DEFAULTPAGEORDERMODE;
    private String orderByFieldName = DEFAULTPAGEORDER;

    public Query toQuery(EntityManager em){

        StringBuilder sb = new StringBuilder();

        sb.append("SELECT project FROM Project project");
        Query query = addConstraint(em, sb, new StringBuilder().append(" ORDER BY project.").append(getOrderByFieldName())
                .append(" ").append(getPageOrderMode()).toString());

        if (this.getStartIndex() > 0)
            query.setFirstResult(this.getStartIndex() * this.getPageSize());

        if (this.getPageSize() > 0)
            query.setMaxResults(this.getPageSize());

        return query;
    }

    private Query addConstraint(EntityManager em, StringBuilder sb, String orderBy){


        List<String> conditions = new ArrayList<>();

        boolean isName = (likeName != null && !likeName.isEmpty());
        boolean isDesc = (likeDesc != null && !likeDesc.isEmpty());
        boolean isCode = (likeCode != null && !likeCode.isEmpty());
        boolean isEmployee = likeEmployee != null;
        boolean isStartingDateFrom = likeStartingFrom != null;
        boolean isStartingDateTo = likeStartingTo != null;
        boolean isStatus = likeStatus != null;
        boolean isEndingDateFrom = likeEndingFrom != null;
        boolean isEndingDateTo = likeEndingTo != null;


        if (isName)
            conditions.add("project.name like :likeName");
        if (isDesc)
            conditions.add("project.content like :likeDesc");
        if (isCode)
            conditions.add("project.projectCode like :likeCode");
        if (isEmployee)
            conditions.add("project.manager = :likeEmployee");
        if (isStartingDateFrom)
            conditions.add("project.startingDate >= :likeStartingFrom");
        if (isStartingDateTo)
            conditions.add("project.startingDate <= :likeStartingTo");
        if (isStatus)
            conditions.add("project.status = :likeStatus");
        if (isEndingDateFrom)
            conditions.add("project.endingDate >= :likeEndingFrom");
        if(isEndingDateTo)
            conditions.add("project.endingDate <= :likeEndingTo");

        for (int i = 0; i < conditions.size(); i++) {
            if (i == 0)
                sb.append(" where ");
            else
                sb.append(" and  ");
            sb.append(conditions.get(i));
        }

        if (orderBy != null && !orderBy.isEmpty())
            sb.append(orderBy);

        Query query = em.createQuery(sb.toString());
        if (isName)
            query.setParameter("likeName", "%" + likeName + "%");
        if (isDesc)
            query.setParameter("likeDesc", "%" + likeDesc + "%");
        if (isCode)
            query.setParameter("likeCode", "%" + likeCode + "%");
        if (isEmployee)
            query.setParameter("likeEmployee", likeEmployee);
        if (isStartingDateFrom)
            query.setParameter("likeStartingFrom", likeStartingFrom);
        if (isStartingDateTo)
            query.setParameter("likeStartingTo", likeStartingTo);
        if (isEndingDateFrom)
            query.setParameter("likeEndingFrom", likeEndingFrom);
        if (isEndingDateTo)
            query.setParameter("likeEndingTo", likeEndingTo);
        if (isStatus)
            query.setParameter("likeStatus", likeStatus);

        return query;
    }

    public Query getCountQuery(EntityManager em) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT COUNT(*) FROM Project project");
        Query query = addConstraint(em, sb, null);
        return query;
    }

    public void clear() {
        startIndex = DEFAULTPAGESTARTINDEX;
        pageSize = DEFAULTPAGESIZE;
        pageOrderMode = DEFAULTPAGEORDERMODE;
        orderByFieldName = DEFAULTPAGEORDER;
    }

    public void clearFilters() {

        likeName = null;
        likeCode = null;
        likeDesc = null;
        likeEmployee = null;
        likeStatus = null;
        likeStartingFrom = null;
        likeStartingTo = null;
        likeEndingFrom = null;
        likeEndingTo = null;
    }

    public String getLikeName() {
        return likeName;
    }

    public void setLikeName(String likeName) {
        this.likeName = likeName;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public static String getDEFAULTPAGEORDERMODE() {
        return DEFAULTPAGEORDERMODE;
    }

    public static String getDEFAULTPAGEORDER() {
        return DEFAULTPAGEORDER;
    }

    public static int getDEFAULTPAGESTARTINDEX() {
        return DEFAULTPAGESTARTINDEX;
    }

    public static int getDEFAULTPAGESIZE() {
        return DEFAULTPAGESIZE;
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

    public String getLikeCode() {
        return likeCode;
    }

    public void setLikeCode(String likeCode) {
        this.likeCode = likeCode;
    }

    public String getLikeDesc() {
        return likeDesc;
    }

    public void setLikeDesc(String likeDesc) {
        this.likeDesc = likeDesc;
    }

    public Employee getLikeEmployee() {
        return likeEmployee;
    }

    public void setLikeEmployee(Employee likeEmployee) {
        this.likeEmployee = likeEmployee;
    }

    public ProjectsStatusEnum getLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(ProjectsStatusEnum likeStatus) {
        this.likeStatus = likeStatus;
    }

    public Date getLikeStartingFrom() {
        return likeStartingFrom;
    }

    public void setLikeStartingFrom(Date likeStartingFrom) {
        this.likeStartingFrom = likeStartingFrom;
    }

    public Date getLikeStartingTo() {
        return likeStartingTo;
    }

    public void setLikeStartingTo(Date likeStartingTo) {
        this.likeStartingTo = likeStartingTo;
    }

    public Date getLikeEndingFrom() {
        return likeEndingFrom;
    }

    public void setLikeEndingFrom(Date likeEndingFrom) {
        this.likeEndingFrom = likeEndingFrom;
    }

    public Date getLikeEndingTo() {
        return likeEndingTo;
    }

    public void setLikeEndingTo(Date likeEndingTo) {
        this.likeEndingTo = likeEndingTo;
    }
}
