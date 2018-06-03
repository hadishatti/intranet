package qa.tecnositafgulf.searchcriteria.tender;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TenderSearchCriteria {

    private static final int DEFAULTPAGESIZE = 5;
    private static final int DEFAULTPAGESTARTINDEX = 0;
    private static final String DEFAULTPAGEORDER = "date";
    private static final String DEFAULTPAGEORDERMODE = "desc";

    private Integer startIndex = DEFAULTPAGESTARTINDEX;
    private Integer pageSize = DEFAULTPAGESIZE;
    private String pageOrderMode = DEFAULTPAGEORDERMODE;
    private String orderByFieldName = DEFAULTPAGEORDER;

    private String likeAuthor;
    private String likeNumber;
    private String likeCategory;
    private String likeContent;
    private Date likeIssuingDateFrom;
    private Date likeIssuingDateTo;
    private Date likeClosingDateFrom;
    private Date likeClosingDateTo;
    private Date likeDateFrom;
    private Date likeDateTo;



    public Query toQuery(EntityManager entityManager){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT tender FROM Tender tender ");
        Query query = addConstraint(entityManager, sb,
                new StringBuilder(" ORDER BY tender.").append(getOrderByFieldName()).append(" ").append(getPageOrderMode()).toString());

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
        sb.append("SELECT count(*) FROM Tender tender ");
        Query query = addConstraint(em, sb, null);
        return query;
    }

    private Query addConstraint(EntityManager entityManager, StringBuilder sb, String orderBy){

        List<String> conditions = new ArrayList<>();
        boolean isAuthorId = (likeAuthor != null && !likeAuthor.isEmpty());
        boolean isNumber = (likeNumber != null && !likeNumber.isEmpty());
        boolean isCategory = (likeCategory != null && !likeCategory.isEmpty());
        boolean isContent = (likeContent != null && !likeContent.isEmpty());
        boolean isIssuingDateFrom = (likeIssuingDateFrom != null);
        boolean isIssuingDateTo = (likeIssuingDateTo != null);
        boolean isClosingDateFrom = (likeClosingDateFrom != null);
        boolean isClosingDateTo = (likeClosingDateTo != null);
        boolean isDateFrom = (likeDateFrom != null);
        boolean isDateTo = (likeDateTo != null);

        if(isAuthorId)
            conditions.add("tender.author.name like :likeAuthor");
        if(isNumber)
            conditions.add("tender.number like :likeNumber");
        if(isCategory)
            conditions.add("tender.type like :likeCategory");
        if(isContent)
            conditions.add("tender.content like :likeContent");
        if(isIssuingDateFrom)
            conditions.add("tender.issuingDate >= :likeIssuingDateFrom");
        if(isIssuingDateTo)
            conditions.add("tender.issuingDate <= :likeIssuingDateTo");
        if(isClosingDateFrom)
            conditions.add("tender.closingDate >= :likeClosingDateFrom");
        if(isClosingDateTo)
            conditions.add("tender.closingDate <= :likeClosingDateTo");
        if(isDateFrom)
            conditions.add("tender.date >= :likeDateFrom");
        if(isDateTo)
            conditions.add("tender.date <= :likeDateTo");


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
        if(isNumber)
            query.setParameter("likeNumber", "%"+likeNumber+"%");
        if(isCategory)
            query.setParameter("likeCategory", "%"+likeCategory+"%");
        if(isContent)
            query.setParameter("likeContent", "%"+likeContent+"%");
        if(isIssuingDateFrom)
            query.setParameter("likeIssuingDateFrom", likeIssuingDateFrom);
        if(isIssuingDateTo)
            query.setParameter("likeIssuingDateTo", likeIssuingDateTo);
        if(isClosingDateFrom)
            query.setParameter("likeClosingDateFrom", likeClosingDateFrom);
        if(isClosingDateTo)
            query.setParameter("likeClosingDateTo", likeClosingDateTo);
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
        likeNumber = null;
        likeCategory = null;
        likeContent = null;
        likeIssuingDateFrom = null;
        likeIssuingDateTo = null;
        likeClosingDateFrom = null;
        likeClosingDateTo = null;
        likeDateFrom = null;
        likeDateTo = null;
    }

    public String getLikeAuthor() {
        return likeAuthor;
    }

    public void setLikeAuthor(String likeAuthor) {
        this.likeAuthor = likeAuthor;
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

    public String getLikeCategory() {
        return likeCategory;
    }

    public void setLikeCategory(String likeCategory) {
        this.likeCategory = likeCategory;
    }

    public String getLikeContent() {
        return likeContent;
    }

    public void setLikeContent(String likeContent) {
        this.likeContent = likeContent;
    }

    public Date getLikeIssuingDateFrom() {
        return likeIssuingDateFrom;
    }

    public void setLikeIssuingDateFrom(Date likeIssuingDateFrom) {
        this.likeIssuingDateFrom = likeIssuingDateFrom;
    }

    public Date getLikeIssuingDateTo() {
        return likeIssuingDateTo;
    }

    public void setLikeIssuingDateTo(Date likeIssuingDateTo) {
        this.likeIssuingDateTo = likeIssuingDateTo;
    }

    public Date getLikeClosingDateFrom() {
        return likeClosingDateFrom;
    }

    public void setLikeClosingDateFrom(Date likeClosingDateFrom) {
        this.likeClosingDateFrom = likeClosingDateFrom;
    }

    public Date getLikeClosingDateTo() {
        return likeClosingDateTo;
    }

    public void setLikeClosingDateTo(Date likeClosingDateTo) {
        this.likeClosingDateTo = likeClosingDateTo;
    }

    public String getLikeNumber() {
        return likeNumber;
    }

    public void setLikeNumber(String likeNumber) {
        this.likeNumber = likeNumber;
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
