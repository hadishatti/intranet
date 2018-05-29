package qa.tecnositafgulf.searchcriteria;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by klajdi on 09/05/18.
 */
public class ClientsSearchCriteira implements Serializable{

    private String likeClientName;
    private String likeClientDescription;
    private String likeClientImg;
    private String likeClientLink;

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
        sb.append("SELECT client FROM Client client ");
        Query query = addConstraint(entityManager, sb,
                new StringBuilder(" ORDER BY client.").append(getOrderByFieldName()).append(" ").append(getPageOrderMode()).toString());

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
        sb.append("SELECT COUNT(*) FROM Client client ");
        Query query = addConstraint(em, sb, null);
        return query;
    }

    private Query addConstraint(EntityManager entityManager, StringBuilder sb, String orderBy){
        List<String> conditions = new ArrayList<>();
        boolean isClientName = (likeClientName != null);
        boolean isClientDescription = (likeClientDescription != null);
        boolean isClientImg = (likeClientImg != null);
        boolean isClientLink = (likeClientLink != null);

        if(isClientName)
            conditions.add("client.name like :likeClientName");
        if(isClientDescription)
            conditions.add("client.description like :likeClientDescription");
        if(isClientImg)
            conditions.add("client.img like :likeClientImg");
        if(isClientLink)
            conditions.add("client.link like :likeClientLink");

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
        if(isClientName)
            query.setParameter("likeClientName","%"+likeClientName+"%");
        if(isClientDescription)
            query.setParameter("likeClientDescription", "%"+likeClientDescription+"%");
        if(isClientImg)
            query.setParameter("likeClientImg", "%"+likeClientImg+"%");
        if(isClientLink)
            query.setParameter("likeClientLink", "%"+likeClientLink+"%");

        return query;
    }

    public void clear() {
        startIndex = DEFAULTPAGESTARTINDEX;
        pageSize = DEFAULTPAGESIZE;
        pageOrderMode = DEFAULTPAGEORDERMODE;
        orderByFieldName = DEFAULTPAGEORDER;
    }

    public void clearFilters() {
        likeClientName = null;
        likeClientDescription = null;
        likeClientImg = null;
        likeClientLink = null;
    }

    public String getLikeClientName() {
        return likeClientName;
    }

    public void setLikeClientName(String likeClientName) {
        this.likeClientName = likeClientName;
    }

    public String getLikeClientDescription() {
        return likeClientDescription;
    }

    public void setLikeClientDescription(String likeClientDescription) {
        this.likeClientDescription = likeClientDescription;
    }

    public String getLikeClientImg() {
        return likeClientImg;
    }

    public void setLikeClientImg(String likeClientImg) {
        this.likeClientImg = likeClientImg;
    }

    public String getLikeClientLink() {
        return likeClientLink;
    }

    public void setLikeClientLink(String likeClientLink) {
        this.likeClientLink = likeClientLink;
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
