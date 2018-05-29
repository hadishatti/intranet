package qa.tecnositafgulf.searchcriteria;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ledio on 5/8/18.
 */
public class SupplierSearchCriteria implements Serializable {

    private String likeName;
    private String likeDomainURL;
    private String likeImgSrc;

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
        sb.append("SELECT s FROM Supplier s ");
        Query query = addConstraint(entityManager, sb,
                new StringBuilder(" ORDER BY s.").append(getOrderByFieldName()).append(" ").append(getPageOrderMode()).toString());

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
        sb.append("SELECT COUNT(*) FROM Supplier s ");
        Query query = addConstraint(em, sb, null);
        return query;
    }

    private Query addConstraint(EntityManager entityManager, StringBuilder sb, String orderBy){
        List<String> conditions = new ArrayList<>();
        boolean isName = (likeName != null);
        boolean isDomainURL = (likeDomainURL != null);
        boolean isImgSrc = (likeImgSrc != null);

        if (isName){
            sb.append("where s.name like :likeName");
            if(isDomainURL)
                conditions.add("s.domainURL like :likeDomainURL");
            if(isImgSrc)
                conditions.add("s.imgSrc like :likeImgSrc");
        } else if (isDomainURL){
            sb.append("where s.domainURL like :likeDomainURL");
            if(isImgSrc)
                conditions.add("s.imgSrc like :likeImgSrc");
        }else if (isImgSrc)
            sb.append("where s.imgSrc like :likeImgSrc");

        if (conditions.size() > 1) {
            for (int i = 0; i < conditions.size(); i++) {
                sb.append(" and ");
                sb.append(conditions.get(i));
            }
        } else sb.append(" ");

        //order By condition
        if (orderBy != null && !orderBy.isEmpty())
            sb.append(orderBy);

        // create query
        Query query = entityManager.createQuery(sb.toString());
        if(isName)
            query.setParameter("likeName","%"+likeName+"%");
        if(isDomainURL)
            query.setParameter("likeDomainURL", "%"+likeDomainURL+"%");
        if(isImgSrc)
            query.setParameter("likeImgSrc", "%"+likeImgSrc+"%");

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
        likeDomainURL = null;
        likeImgSrc = null;
    }

    public String getLikeName() {
        return likeName;
    }

    public void setLikeName(String likeName) {
        this.likeName = likeName;
    }

    public String getLikeDomainURL() {
        return likeDomainURL;
    }

    public void setLikeDomainURL(String likeDomainURL) {
        this.likeDomainURL = likeDomainURL;
    }

    public String getLikeImgSrc() {
        return likeImgSrc;
    }

    public void setLikeImgSrc(String likeImgSrc) {
        this.likeImgSrc = likeImgSrc;
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
