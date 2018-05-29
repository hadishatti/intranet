package qa.tecnositafgulf.searchcriteria.inventory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class ProductStockSearchCriteria {

    private String likeProductName;
    private String likeLocationName;
    private String likeCurrentStock;
    private String likeThresholdLimit;
    private String likeUnitPrice;

    private static final int DEFAULTPAGESIZE = 8;
    private static final int DEFAULTPAGESTARTINDEX = 0;
    private static final String DEFAULTPAGEORDER = "product.productName";
    private static final String DEFAULTPAGEORDERMODE = "desc";

    private Integer startIndex = DEFAULTPAGESTARTINDEX;
    private Integer pageSize = DEFAULTPAGESIZE;
    private String pageOrderMode = DEFAULTPAGEORDERMODE;
    private String orderByFieldName = DEFAULTPAGEORDER;

    public Query toQuery(EntityManager entityManager) {
        StringBuilder sb = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        sb.append("SELECT productStock FROM ProductStock productStock, Product product, Location location " +
                " where productStock.product = product.id and productStock.location = location.id ");

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
        sb.append("SELECT count(1) FROM ProductStock productStock, Product product, Location location "+
                " where productStock.product = product.id and productStock.location = location.id  ");
        Query query = addConstraint(em, sb, null);
        return query;
    }

    private Query addConstraint(EntityManager entityManager, StringBuilder sb, String orderBy) {
        List<String> conditions = new ArrayList<>();
        boolean isProductName = (likeProductName != null && !likeProductName.isEmpty());
        boolean isLocationName = (likeLocationName != null && !likeLocationName.isEmpty());
        boolean isCurrentStock = (likeCurrentStock != null && !likeCurrentStock.isEmpty());
        boolean isThresholdLimit = (likeThresholdLimit != null && !likeThresholdLimit.isEmpty());
        boolean isUnitPrice = (likeUnitPrice != null && !likeUnitPrice.isEmpty());


        if (isProductName)
            conditions.add("product.productName like :likeProductName");
        if (isLocationName)
            conditions.add("location.name like :likeLocationName");
        if (isCurrentStock)
            conditions.add("productStock.currentStock = :likeCurrentStock");
        if(isThresholdLimit)
            conditions.add("productStock.thresholdLimit = :likeThresholdLimit");
        if(isUnitPrice)
            conditions.add("productStock.unitPrice = :likeUnitPrice");

        for (int i = 0; i < conditions.size(); i++) {
            sb.append(" and  ");
            sb.append(conditions.get(i));
        }

        //order By condition
        if (orderBy != null && !orderBy.isEmpty())
            sb.append(orderBy);

        // create query
        Query query = entityManager.createQuery(sb.toString());
        if (isProductName)
            query.setParameter("likeProductName", "%" + likeProductName + "%");
        if (isLocationName)
            query.setParameter("likeLocationName", "%" + likeLocationName + "%");
        if (isCurrentStock)
            query.setParameter("likeCurrentStock", Long.parseLong(likeCurrentStock) );
        if(isThresholdLimit)
            query.setParameter("likeThresholdLimit", Long.parseLong(likeThresholdLimit) );
        if(isUnitPrice)
            query.setParameter("likeUnitPrice", Long.parseLong(likeUnitPrice));

        return query;
    }

    public void clear() {
        startIndex = DEFAULTPAGESTARTINDEX;
        pageSize = DEFAULTPAGESIZE;
        pageOrderMode = DEFAULTPAGEORDERMODE;
        orderByFieldName = DEFAULTPAGEORDER;
    }

    public void clearFilters() {
        likeProductName = null;
        likeLocationName = null;
        likeCurrentStock = null;
        likeThresholdLimit = null;
        likeUnitPrice = null;
    }


    public String getLikeUnitPrice() {
        return likeUnitPrice;
    }

    public void setLikeUnitPrice(String likeUnitPrice) {
        this.likeUnitPrice = likeUnitPrice;
    }

    public String getLikeProductName() {
        return likeProductName;
    }

    public void setLikeProductName(String likeProductName) {
        this.likeProductName = likeProductName;
    }

    public String getLikeLocationName() {
        return likeLocationName;
    }

    public void setLikeLocationName(String likeLocationName) {
        this.likeLocationName = likeLocationName;
    }

    public String getLikeCurrentStock() {
        return likeCurrentStock;
    }

    public void setLikeCurrentStock(String likeCurrentStock) {
        this.likeCurrentStock = likeCurrentStock;
    }

    public String getLikeThresholdLimit() {
        return likeThresholdLimit;
    }

    public void setLikeThresholdLimit(String likeThresholdLimit) {
        this.likeThresholdLimit = likeThresholdLimit;
    }

    public static int getDEFAULTPAGESIZE() {
        return DEFAULTPAGESIZE;
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
