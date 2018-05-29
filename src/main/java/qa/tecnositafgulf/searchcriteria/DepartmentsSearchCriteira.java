package qa.tecnositafgulf.searchcriteria;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class DepartmentsSearchCriteira {

    private String likeDepartmentName;
    private String likeDepartmentBuildingName;

    private static final int DEFAULTPAGESIZE = 8;
    private static final int DEFAULTPAGESTARTINDEX = 0;
    private static final String DEFAULTPAGEORDER = "name";
    private static final String DEFAULTPAGEORDERMODE = "desc";

    private Integer startIndex = DEFAULTPAGESTARTINDEX;
    private Integer pageSize = DEFAULTPAGESIZE;
    private String pageOrderMode = DEFAULTPAGEORDERMODE;
    private String orderByFieldName = DEFAULTPAGEORDER;

    public Query toQuery(EntityManager entityManager){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT department FROM Department department ");
        Query query = addConstraint(entityManager, sb,
                new StringBuilder(" ORDER BY department.").append(getOrderByFieldName()).append(" ").append(getPageOrderMode()).toString());

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
        sb.append("SELECT COUNT(*) FROM Department department ");
        Query query = addConstraint(em, sb, null);
        return query;
    }

    private Query addConstraint(EntityManager entityManager, StringBuilder sb, String orderBy){
        List<String> conditions = new ArrayList<>();
        boolean isDepartmentName = (likeDepartmentName != null);
        boolean isDeprtmentBuildingName = (likeDepartmentBuildingName != null);

        if(isDepartmentName)
            conditions.add("department.name like :likeDepartmentName");
        if(isDeprtmentBuildingName)
            conditions.add("department.company.name like :likeDepartmentBuildingName");

        for (int i = 0; i < conditions.size(); i++) {
            if(i == 0){
                sb.append(" where  ");
                sb.append(conditions.get(i));
            }else{
                sb.append(" and  ");
                sb.append(conditions.get(i));
            }

        }

        //order By condition
        if (orderBy != null && !orderBy.isEmpty())
            sb.append(orderBy);

        // create query
        Query query = entityManager.createQuery(sb.toString());
        if(isDepartmentName)
            query.setParameter("likeDepartmentName","%"+likeDepartmentName+"%");
        if(isDeprtmentBuildingName)
            query.setParameter("likeDepartmentBuildingName", "%"+likeDepartmentBuildingName+"%");

        return query;
    }


    public void clear() {
        startIndex = DEFAULTPAGESTARTINDEX;
        pageSize = DEFAULTPAGESIZE;
        pageOrderMode = DEFAULTPAGEORDERMODE;
        orderByFieldName = DEFAULTPAGEORDER;
    }

    public void clearFilters() {
        likeDepartmentName = null;
        likeDepartmentBuildingName = null;
    }


    public String getLikeDepartmentName() {
        return likeDepartmentName;
    }

    public void setLikeDepartmentName(String likeDepartmentName) {
        this.likeDepartmentName = likeDepartmentName;
    }

    public String getLikeDepartmentBuildingName() {
        return likeDepartmentBuildingName;
    }

    public void setLikeDepartmentBuildingName(String likeDepartmentBuildingName) {
        this.likeDepartmentBuildingName = likeDepartmentBuildingName;
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
