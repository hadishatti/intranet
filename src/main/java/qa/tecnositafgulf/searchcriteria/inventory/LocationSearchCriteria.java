package qa.tecnositafgulf.searchcriteria.inventory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class LocationSearchCriteria {

    private String likeCompanyName;
    private String likeLocationName;
    private String likeLocationDesc;
    private String likeLocationAddress;

    private static final int DEFAULTPAGESIZE = 8;
    private static final int DEFAULTPAGESTARTINDEX = 0;
    private static final String DEFAULTPAGEORDER = "location.name";
    private static final String DEFAULTPAGEORDERMODE = "desc";

    private Integer startIndex = DEFAULTPAGESTARTINDEX;
    private Integer pageSize = DEFAULTPAGESIZE;
    private String pageOrderMode = DEFAULTPAGEORDERMODE;
    private String orderByFieldName = DEFAULTPAGEORDER;

    public Query toQuery(EntityManager entityManager) {
        StringBuilder sb = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        sb.append("SELECT location FROM Location location, Company company where location.company = company.id ");

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
        sb.append("SELECT COUNT(*) FROM Location location, Company company where location.company = company.id ");
        Query query = addConstraint(em, sb, null);
        return query;
    }

    private Query addConstraint(EntityManager entityManager, StringBuilder sb, String orderBy) {
        List<String> conditions = new ArrayList<>();
        boolean isCompanyName = (likeCompanyName != null && !likeCompanyName.isEmpty());
        boolean isLocationName = (likeLocationName != null && !likeLocationName.isEmpty());
        boolean isLocationDesc = (likeLocationDesc != null && !likeLocationDesc.isEmpty());
        boolean isLocationAddress = (likeLocationAddress != null && !likeLocationAddress.isEmpty());

        if (isCompanyName)
            conditions.add("company.name like :likeCompanyName");
        if (isLocationName)
            conditions.add("location.name like :likeLocationName");
        if (isLocationDesc)
            conditions.add("location.description like :likeLocationDesc");
        if(isLocationAddress)
            conditions.add("location.address like :likeLocationAddress");

        for (int i = 0; i < conditions.size(); i++) {
            sb.append(" and  ");
            sb.append(conditions.get(i));
        }

        //order By condition
        if (orderBy != null && !orderBy.isEmpty())
            sb.append(orderBy);

        // create query
        Query query = entityManager.createQuery(sb.toString());
        if (isCompanyName)
            query.setParameter("likeCompanyName", "%" + likeCompanyName + "%");
        if (isLocationName)
            query.setParameter("likeLocationName", "%" + likeLocationName + "%");
        if (isLocationDesc)
            query.setParameter("likeLocationDesc", "%" + likeLocationDesc + "%");
        if(isLocationAddress)
            query.setParameter("likeLocationAddress", "%" + likeLocationAddress + "%");

        return query;
    }

    public void clear() {
        startIndex = DEFAULTPAGESTARTINDEX;
        pageSize = DEFAULTPAGESIZE;
        pageOrderMode = DEFAULTPAGEORDERMODE;
        orderByFieldName = DEFAULTPAGEORDER;
    }

    public void clearFilters() {
        likeCompanyName = null;
        likeLocationName = null;
        likeLocationDesc = null;
        likeLocationAddress = null;
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

    public String getLikeCompanyName() {
        return likeCompanyName;
    }

    public void setLikeCompanyName(String likeCompanyName) {
        this.likeCompanyName = likeCompanyName;
    }

    public String getLikeLocationName() {
        return likeLocationName;
    }

    public void setLikeLocationName(String likeLocationName) {
        this.likeLocationName = likeLocationName;
    }

    public String getLikeLocationDesc() {
        return likeLocationDesc;
    }

    public void setLikeLocationDesc(String likeLocationDesc) {
        this.likeLocationDesc = likeLocationDesc;
    }

    public String getLikeLocationAddress() {
        return likeLocationAddress;
    }

    public void setLikeLocationAddress(String likeLocationAddress) {
        this.likeLocationAddress = likeLocationAddress;
    }
}
