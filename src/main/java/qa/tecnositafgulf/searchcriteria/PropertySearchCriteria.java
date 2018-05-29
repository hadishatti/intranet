package qa.tecnositafgulf.searchcriteria;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by klajdi on 24/05/18.
 */
public class PropertySearchCriteria implements Serializable{

    private static final int DEFAULTPAGESIZE = 10;
    private static final int DEFAULTPAGESTARTINDEX = 0;
    private static final String DEFAULTPAGEORDER = "key";
    private static final String DEFAULTPAGEORDERMODE = "desc";

    private Integer startIndex = DEFAULTPAGESTARTINDEX;
    private Integer pageSize = DEFAULTPAGESIZE;
    private String pageOrderMode = DEFAULTPAGEORDERMODE;
    private String orderByFieldName = DEFAULTPAGEORDER;

    private String likeKey;
    private String likeDescription;
    private String likeValue;

    public Query toQuery(EntityManager entityManager){

        StringBuilder sb = new StringBuilder();

        sb.append("SELECT property FROM Property property ");
        Query query = addConstraint(entityManager, sb,
                new StringBuilder(" ORDER BY property.").append(getOrderByFieldName()).append(" ").append(getPageOrderMode()).toString());

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
        sb.append("SELECT count(*) FROM Property property ");
        Query query = addConstraint(em, sb, null);
        return query;
    }

    private Query addConstraint(EntityManager entityManager, StringBuilder sb, String orderBy){

        List<String> conditions = new ArrayList<>();
        boolean isKey = (likeKey != null && !likeKey.isEmpty());
        boolean isDescription = (likeDescription != null && !likeDescription.isEmpty());
        boolean isValue = (likeValue != null && !likeValue.isEmpty());

        if(isKey)
            conditions.add("property.key like :likeKey");
        if(isDescription)
            conditions.add("property.description like :likeDescription");
        if(isValue)
            conditions.add("property.value like :likeValue");

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
        if (orderBy != null && !orderBy.isEmpty())
            sb.append(orderBy);

        // create query
        Query query = entityManager.createQuery(sb.toString());
        if(isKey)
            query.setParameter("likeKey", "%"+likeKey+"%");
        if(isDescription)
            query.setParameter("likeDescription", "%"+likeDescription+"%");
        if(isValue)
            query.setParameter("likeValue","%"+likeValue+"%");

        return query;
    }

    public void clear() {
        startIndex = DEFAULTPAGESTARTINDEX;
        pageSize = DEFAULTPAGESIZE;
        pageOrderMode = DEFAULTPAGEORDERMODE;
        orderByFieldName = DEFAULTPAGEORDER;
    }

    public void clearFilters() {
        likeKey = null;
        likeDescription = null;
        likeValue = null;
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

    public String getLikeKey() {
        return likeKey;
    }

    public void setLikeKey(String likeKey) {
        this.likeKey = likeKey;
    }

    public String getLikeDescription() {
        return likeDescription;
    }

    public void setLikeDescription(String likeDescription) {
        this.likeDescription = likeDescription;
    }

    public String getLikeValue() {
        return likeValue;
    }

    public void setLikeValue(String likeValue) {
        this.likeValue = likeValue;
    }
}
