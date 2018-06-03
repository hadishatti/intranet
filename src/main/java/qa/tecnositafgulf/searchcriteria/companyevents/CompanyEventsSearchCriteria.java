package qa.tecnositafgulf.searchcriteria.companyevents;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CompanyEventsSearchCriteria {

    private static final int DEFAULTPAGESIZE = 5;
    private static final int DEFAULTPAGESTARTINDEX = 0;
    private static final String DEFAULTPAGEORDER = "date";
    private static final String DEFAULTPAGEORDERMODE = "desc";

    private Integer startIndex = DEFAULTPAGESTARTINDEX;
    private Integer pageSize = DEFAULTPAGESIZE;
    private String pageOrderMode = DEFAULTPAGEORDERMODE;
    private String orderByFieldName = DEFAULTPAGEORDER;

    private String likeAuthor;
    private String likeTitle;
    private String likeContent;
    private Date likeDateFrom;
    private Date likeDateTo;

    public Query toQuery(EntityManager entityManager){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT companyEvent FROM CompanyEvent companyEvent ");
        Query query = addConstraint(entityManager, sb,
                new StringBuilder(" ORDER BY companyEvent.").append(getOrderByFieldName()).append(" ").append(getPageOrderMode()).toString());

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
        sb.append("SELECT count(*) FROM CompanyEvent companyEvent ");
        Query query = addConstraint(em, sb, null);
        return query;
    }


    private Query addConstraint(EntityManager entityManager, StringBuilder sb, String orderBy){

        List<String> conditions = new ArrayList<>();
        boolean isAuthorId = (likeAuthor != null && !likeAuthor.isEmpty());
        boolean isTitle = (likeTitle != null && !likeTitle.isEmpty());
        boolean isContent = (likeContent != null && !likeContent.isEmpty());
        boolean isDateFrom = likeDateFrom != null;
        boolean isDateTo = likeDateTo != null;
        if(isAuthorId)
            conditions.add("companyEvent.author.name like :likeAuthor");
        if(isTitle)
            conditions.add("companyEvent.title like :likeTitle");
        if(isContent)
            conditions.add("companyEvent.content like :likeContent");
        if(isDateFrom)
            conditions.add("companyEvent.date >= :likeDateFrom");
        if(isDateTo)
            conditions.add("companyEvent.date <= :likeDateTo");

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
        if(isAuthorId)
            query.setParameter("likeAuthor", "%"+likeAuthor+"%");
        if(isTitle)
            query.setParameter("likeTitle", "%"+likeTitle+"%");
        if(isContent)
            query.setParameter("likeContent","%"+likeContent+"%");
        if(isDateFrom)
            query.setParameter("likeDateFrom", likeDateFrom);
        if(isDateTo)
            query.setParameter("likeDateTo", likeDateTo);



        return query;
    }

    public void clear() {
        startIndex = DEFAULTPAGESTARTINDEX;
        pageSize = DEFAULTPAGESIZE;
        pageOrderMode = DEFAULTPAGEORDERMODE;
        orderByFieldName = DEFAULTPAGEORDER;
    }

    public void clearFilters() {
        likeAuthor = null;
        likeTitle = null;
        likeContent = null;
        likeDateFrom = null;
        likeDateTo = null;
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

    public String getLikeAuthor() {
        return likeAuthor;
    }

    public void setLikeAuthor(String likeAuthor) {
        this.likeAuthor = likeAuthor;
    }

    public String getLikeTitle() {
        return likeTitle;
    }

    public void setLikeTitle(String likeTitle) {
        this.likeTitle = likeTitle;
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
