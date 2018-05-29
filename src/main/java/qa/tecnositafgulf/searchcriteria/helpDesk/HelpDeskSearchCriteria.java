package qa.tecnositafgulf.searchcriteria.helpDesk;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by klajdi on 20/05/18.
 */
public class HelpDeskSearchCriteria implements Serializable{

    private static final String DEFAULTPAGEORDER = "date";
    private static final String DEFAULTPAGEORDERMODE = "desc";

    private String pageOrderMode = DEFAULTPAGEORDERMODE;
    private String orderByFieldName = DEFAULTPAGEORDER;

    private String likeContent;
    private Date likeDateFrom;
    private Date likeDateTo;

    public Query toQuery(EntityManager em){

        StringBuilder sb = new StringBuilder();

        sb.append("SELECT helpdesk FROM HelpDesk helpdesk");
        Query query = addConstraint(em, sb, new StringBuilder().append(" ORDER BY helpdesk.").append(getOrderByFieldName())
                .append(" ").append(getPageOrderMode()).toString());

        return query;
    }

    private Query addConstraint(EntityManager em, StringBuilder sb, String orderBy){

        List<String> conditions = new ArrayList<>();
        boolean isContent = (likeContent != null && !likeContent.isEmpty());
        boolean isDate = (likeDateFrom != null && likeDateTo != null);

        if (isContent)
            conditions.add("CONCAT_WS(' ', helpdesk.author.name, helpdesk.title, helpdesk.content) like :likeContent");
        if(isDate)
            conditions.add("helpdesk.date BETWEEN :likeDateFrom AND :likeDateTo");

        for (int i = 0; i < conditions.size(); i++) {
            if(i == 0){
                sb.append(" where  ");
                sb.append(conditions.get(i));
            }else {
                sb.append(" and  ");
                sb.append(conditions.get(i));
            }
        }

        if (orderBy != null && !orderBy.isEmpty())
            sb.append(orderBy);

        Query query = em.createQuery(sb.toString());
        if (isContent)
            query.setParameter("likeContent", "%" + likeContent + "%");
        if(isDate) {
            query.setParameter("likeDateFrom", likeDateFrom);
            query.setParameter("likeDateTo", likeDateTo);
        }

        return query;
    }

    public Query getCountQuery(EntityManager em) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT COUNT(*) FROM HelpDesk helpdesk");
        Query query = addConstraint(em, sb, null);
        return query;
    }

    public void clear() {
        pageOrderMode = DEFAULTPAGEORDERMODE;
        orderByFieldName = DEFAULTPAGEORDER;
    }

    public void clearFilters() {
        likeContent = null;
        likeDateFrom = null;
        likeDateTo = null;
    }

    public String getOrderByFieldName() {
        return orderByFieldName;
    }

    public void setOrderByFieldName(String orderByFieldName) {
        this.orderByFieldName = orderByFieldName;
    }

    public String getPageOrderMode() {
        return pageOrderMode;
    }

    public void setPageOrderMode(String pageOrderMode) {
        this.pageOrderMode = pageOrderMode;
    }

    public String getLikeContent() {
        return likeContent;
    }

    public void setLikeContent(String likeContent) {
        this.likeContent = likeContent;
    }

    public Date getLikeDateFrom() {
        return likeDateFrom;
    }

    public void setLikeDateFrom(Date likeDateFrom) {
        this.likeDateFrom = likeDateFrom;
    }

    public Date getLikeDateTo() {
        return likeDateTo;
    }

    public void setLikeDateTo(Date likeDateTo) {
        this.likeDateTo = likeDateTo;
    }
}
