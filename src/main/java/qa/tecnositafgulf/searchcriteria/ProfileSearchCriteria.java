package qa.tecnositafgulf.searchcriteria;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class ProfileSearchCriteria {
    
    private String likeProfileName;
    private String likeProfileDescription;

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
        sb.append("SELECT profile FROM Profile profile ");
        Query query = addConstraint(entityManager, sb,
                new StringBuilder(" ORDER BY profile.").append(getOrderByFieldName()).append(" ").append(getPageOrderMode()).toString());

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
        sb.append("SELECT COUNT(*) FROM Profile profile ");
        Query query = addConstraint(em, sb, null);
        return query;
    }

    private Query addConstraint(EntityManager entityManager, StringBuilder sb, String orderBy){
        List<String> conditions = new ArrayList<>();
        boolean isProfileName = (likeProfileName != null);
        boolean isProfileDescription = (likeProfileDescription != null);

        if(isProfileName)
            conditions.add("profile.name like :likeProfileName");
        if(isProfileDescription)
            conditions.add("profile.description like :likeProfileDescription");

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
        if(isProfileName)
            query.setParameter("likeProfileName","%"+likeProfileName+"%");
        if(isProfileDescription)
            query.setParameter("likeProfileDescription", "%"+likeProfileDescription+"%");

        return query;
    }


    public void clear() {
        startIndex = DEFAULTPAGESTARTINDEX;
        pageSize = DEFAULTPAGESIZE;
        pageOrderMode = DEFAULTPAGEORDERMODE;
        orderByFieldName = DEFAULTPAGEORDER;
    }

    public void clearFilters() {
        likeProfileName = null;
        likeProfileDescription = null;
    }


    public String getLikeProfileName() {
        return likeProfileName;
    }

    public void setLikeProfileName(String likeProfileName) {
        this.likeProfileName = likeProfileName;
    }

    public String getLikeProfileDescription() {
        return likeProfileDescription;
    }

    public void setLikeProfileDescription(String likeProfileDescription) {
        this.likeProfileDescription = likeProfileDescription;
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
