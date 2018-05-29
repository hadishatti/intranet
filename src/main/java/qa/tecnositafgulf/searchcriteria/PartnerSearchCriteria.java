package qa.tecnositafgulf.searchcriteria;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ameljo on 5/8/18.
 */
public class PartnerSearchCriteria implements Serializable {

    private String likeName;
    private String likeHref;
    private String likeImg;

    private static final int DEFAULTPAGESIZE = 8;
    private static final int DEFAULTPAGESTARTINDEX = 0;
    private static final String DEFAULTPAGEORDER = "name";
    private static final String DEFAULTPAGEORDERMODE = "desc";

    private Integer startIndex = DEFAULTPAGESTARTINDEX;
    private Integer pageSize = DEFAULTPAGESIZE;
    private String pageOrderMode = DEFAULTPAGEORDERMODE;
    private String orderByFieldName = DEFAULTPAGEORDER;


    public Query toQuery(EntityManager em){

        StringBuilder sb = new StringBuilder();

        sb.append("SELECT partner FROM Partner partner");
        Query query = addConstraint(em, sb, new StringBuilder().append(" ORDER BY partner.").append(getOrderByFieldName())
                                .append(" ").append(getPageOrderMode()).toString());

        if (this.getStartIndex() > 0)
            query.setFirstResult(this.getStartIndex() * this.getPageSize());

        if (this.getPageSize() > 0)
            query.setMaxResults(this.getPageSize());

        return query;
    }

    private Query addConstraint(EntityManager em, StringBuilder sb, String orderBy){

        List<String> conditions = new ArrayList<>();
        boolean isName = (likeName != null);
        boolean isHref = (likeHref != null);
        boolean isImg = (likeImg != null);

        if (isName) {
            conditions.add("partner.name like :likeName");
        }
        if (isHref) {
            conditions.add("partner.href like :likeHref");
        }
        if (isImg) {
            conditions.add("partner.img like :likeImg");
        }

        for(int i = 0; i < conditions.size(); i++){
            if (i == 0)
                sb.append(" WHERE " + conditions.get(i));
            else
                sb.append(" and " + conditions.get(i));
        }

        if (orderBy != null && !orderBy.isEmpty())
            sb.append(orderBy);

        Query query = em.createQuery(sb.toString());
        if (isName)
            query.setParameter("likeName", "%" + likeName + "%");
        if (isHref)
            query.setParameter("likeHref", "%" + likeHref + "%");
        if (isImg)
            query.setParameter("likeImg", "%" + likeImg + "%");

        return query;
    }

    public Query getCountQuery(EntityManager em) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT COUNT(*) FROM Partner partner");
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
        likeHref = null;
        likeImg = null;
        likeName = null;
    }

    public String getOrderByFieldName(){ return orderByFieldName; }
    public String getPageOrderMode(){ return pageOrderMode; }

    public String getLikeName() {
        return likeName;
    }

    public void setLikeName(String likeName) {
        this.likeName = likeName;
    }

    public String getLikeHref() {
        return likeHref;
    }

    public void setLikeHref(String likeHref) {
        this.likeHref = likeHref;
    }

    public String getLikeImg() {
        return likeImg;
    }

    public void setLikeImg(String likeImg) {
        this.likeImg = likeImg;
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

    public void setPageOrderMode(String pageOrderMode) {
        this.pageOrderMode = pageOrderMode;
    }

    public void setOrderByFieldName(String orderByFieldName) {
        this.orderByFieldName = orderByFieldName;
    }
}
