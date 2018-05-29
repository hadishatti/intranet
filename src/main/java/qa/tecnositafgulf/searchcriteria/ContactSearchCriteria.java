package qa.tecnositafgulf.searchcriteria;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;

/**
 * Created by ameljo on 5/14/18.
 */
public class ContactSearchCriteria implements Serializable {

    private  String likeContent;

    private static final int DEFAULTPAGESIZE = 2;
    private static final int DEFAULTPAGESTARTINDEX = 0;
    private static final String DEFAULTPAGEORDER = "name";
    private static final String DEFAULTPAGEORDERMODE = "desc";

    private Integer startIndex = DEFAULTPAGESTARTINDEX;
    private Integer pageSize = DEFAULTPAGESIZE;
    private String pageOrderMode = DEFAULTPAGEORDERMODE;
    private String orderByFieldName = DEFAULTPAGEORDER;

    public Query toQuery(EntityManager em){

        StringBuilder sb = new StringBuilder();

        sb.append("SELECT contact FROM Contact contact");
        Query query = addConstraint(em, sb, new StringBuilder().append(" ORDER BY contact.").append(getOrderByFieldName())
                .append(" ").append(getPageOrderMode()).toString());

        if (this.getStartIndex() > 0)
            query.setFirstResult(this.getStartIndex() * this.getPageSize());

        if (this.getPageSize() > 0)
            query.setMaxResults(this.getPageSize());

        return query;
    }

    private Query addConstraint(EntityManager em, StringBuilder sb, String orderBy){

        boolean isContent = (likeContent != null);


        if (isContent) {
            sb.append(" WHERE CONCAT_WS(' ', contact.name, contact.surname, contact.position) like :likeContent");
        }

        if (orderBy != null && !orderBy.isEmpty())
            sb.append(orderBy);

        Query query = em.createQuery(sb.toString());
        if (isContent)
            query.setParameter("likeContent", "%" + likeContent + "%");

        return query;
    }

    public Query getCountQuery(EntityManager em) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT COUNT(*) FROM Contact contact");
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
        likeContent = null;
    }

    public String getLikeContent() {
        return likeContent;
    }

    public void setLikeContent(String likeContent) {
        this.likeContent = likeContent;
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
}
