package qa.tecnositafgulf.searchcriteria.inventory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductSupplierSearchCriteria {

    private String likeName;
    private String likeAddress;
    private String likeEmail;
    private String likeMobileNumber;
    private String likeLandlineNumber;
    private Date likeDateFrom;
    private Date likeDateTo;


    private static final int DEFAULTPAGESIZE = 8;
    private static final int DEFAULTPAGESTARTINDEX = 0;
    private static final String DEFAULTPAGEORDER = "productSupplier.name";
    private static final String DEFAULTPAGEORDERMODE = "desc";

    private Integer startIndex = DEFAULTPAGESTARTINDEX;
    private Integer pageSize = DEFAULTPAGESIZE;
    private String pageOrderMode = DEFAULTPAGEORDERMODE;
    private String orderByFieldName = DEFAULTPAGEORDER;

    public Query toQuery(EntityManager entityManager) {
        StringBuilder sb = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        sb.append("SELECT productSupplier FROM ProductSupplier productSupplier " );

        Query query = addConstraint(entityManager, sb, stringBuilder.append(" ORDER BY ").append(getOrderByFieldName()).append(" ").append(getPageOrderMode()).toString());

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
        sb.append("SELECT count(1) FROM ProductSupplier productSupplier");
        Query query = addConstraint(em, sb, null);
        return query;
    }

    private Query addConstraint(EntityManager entityManager, StringBuilder sb, String orderBy) {
        List<String> conditions = new ArrayList<>();
        boolean isName = (likeName != null && !likeName.isEmpty());
        boolean isEmail = (likeEmail != null && !likeEmail.isEmpty());
        boolean isAddress = (likeAddress != null && !likeAddress.isEmpty());
        boolean isMobileNumber = (likeMobileNumber != null && !likeMobileNumber.isEmpty());
        boolean isLandlineNumber = (likeLandlineNumber != null && !likeLandlineNumber.isEmpty());
        boolean isDateFrom = likeDateFrom != null;
        boolean isDateTo = likeDateTo != null;

        if(isName)
            conditions.add("productSupplier.name like :likeName");
        if(isEmail)
            conditions.add("productSupplier.emailId like :likeEmail");
        if(isAddress)
            conditions.add("productSupplier.address like :likeAddress");
        if(isMobileNumber)
            conditions.add("productSupplier.mobileNumber like :likeMobileNumber");
        if(isLandlineNumber)
            conditions.add("productSupplier.landLineNumber like :likeLandlineNumber");
        if(isDateFrom)
            conditions.add("productSupplier.createDate >= :likeDateFrom");
        if(isDateTo)
            conditions.add("productSupplier.createDate <= :likeDateTo");

        for (int i = 0; i < conditions.size(); i++) {
            if (i == 0)
                sb.append(" where ");
            else
                sb.append(" and  ");
            sb.append(conditions.get(i));
        }

        //order By condition
        if (orderBy != null && !orderBy.isEmpty())
            sb.append(orderBy);

        // create query
        Query query = entityManager.createQuery(sb.toString());
        if(isName)
            query.setParameter("likeName", "%" + likeName + "%");
        if(isAddress)
            query.setParameter("likeAddress", "%" + likeAddress + "%");
        if(isEmail)
            query.setParameter("likeEmail", "%" + likeEmail + "%");
        if(isMobileNumber)
            query.setParameter("likeMobileNumber", "%" + likeMobileNumber + "%");
        if(isLandlineNumber)
            query.setParameter("likeLandlineNumber", "%" + likeLandlineNumber + "%");
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
        likeName = null;
        likeEmail = null;
        likeAddress = null;
        likeMobileNumber = null;
        likeLandlineNumber = null;
        likeDateFrom = null;
        likeDateTo = null;
    }

    public String getLikeName() {
        return likeName;
    }

    public void setLikeName(String likeName) {
        this.likeName = likeName;
    }

    public String getLikeAddress() {
        return likeAddress;
    }

    public void setLikeAddress(String likeAddress) {
        this.likeAddress = likeAddress;
    }

    public String getLikeMobileNumber() {
        return likeMobileNumber;
    }

    public void setLikeMobileNumber(String likeMobileNumber) {
        this.likeMobileNumber = likeMobileNumber;
    }

    public String getLikeEmail() {
        return likeEmail;
    }

    public void setLikeEmail(String likeEmail) {
        this.likeEmail = likeEmail;
    }

    public String getLikeLandlineNumber() {
        return likeLandlineNumber;
    }

    public void setLikeLandlineNumber(String likeLandlineNumber) {
        this.likeLandlineNumber = likeLandlineNumber;
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
