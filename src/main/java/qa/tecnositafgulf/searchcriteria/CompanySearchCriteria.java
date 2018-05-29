package qa.tecnositafgulf.searchcriteria;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class CompanySearchCriteria {

    private String likeCompName;
    private String likeCompAddress;

    private static final int DEFAULTPAGESIZE = 10;
    private static final int DEFAULTPAGESTARTINDEX = 0;
    private static final String DEFAULTPAGEORDER = "name";
    private static final String DEFAULTPAGEORDERMODE = "desc";

    private Integer startIndex = DEFAULTPAGESTARTINDEX;
    private Integer pageSize = DEFAULTPAGESIZE;
    private String pageOrderMode = DEFAULTPAGEORDERMODE;
    private String orderByFieldName = DEFAULTPAGEORDER;

    public Query toQuery(EntityManager entityManager){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT company FROM Company company ");
        Query query = addConstraint(entityManager, sb,
                new StringBuilder(" ORDER BY company.").append(getOrderByFieldName()).append(" ").append(getPageOrderMode()).toString());

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
        sb.append("SELECT COUNT(*) FROM Company company ");
        Query query = addConstraint(em, sb, null);
        return query;
    }

    private Query addConstraint(EntityManager entityManager, StringBuilder sb, String orderBy){
        List<String> conditions = new ArrayList<>();
        boolean isCompanyName = (likeCompName != null);
        boolean isCompanyAddress = (likeCompAddress != null);

        if(isCompanyName)
            conditions.add("company.name like :likeCompName");
        if(isCompanyAddress)
            conditions.add("company.address like :likeCompAddress");

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
        if(isCompanyName)
            query.setParameter("likeCompName","%"+likeCompName+"%");
        if(isCompanyAddress)
            query.setParameter("likeCompAddress", "%"+likeCompAddress+"%");

        return query;
    }


    public void clear() {
        startIndex = DEFAULTPAGESTARTINDEX;
        pageSize = DEFAULTPAGESIZE;
        pageOrderMode = DEFAULTPAGEORDERMODE;
        orderByFieldName = DEFAULTPAGEORDER;
    }

    public void clearFilters() {
        likeCompName = null;
        likeCompAddress = null;
    }

    public String getLikeCompName() {
        return likeCompName;
    }

    public void setLikeCompName(String likeCompName) {
        this.likeCompName = likeCompName;
    }

    public String getLikeCompAddress() {
        return likeCompAddress;
    }

    public void setLikeCompAddress(String likeCompAddress) {
        this.likeCompAddress = likeCompAddress;
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
