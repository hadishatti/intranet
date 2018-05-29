package qa.tecnositafgulf.searchcriteria.inventory;

public class StockTransferSearchCriteria {

    private String likeProductName;
    private String likeFromLocationName;
    private String likeToLocationName;
    private String likeUpdatedStock;


    private static final int DEFAULTPAGESIZE = 10;
    private static final int DEFAULTPAGESTARTINDEX = 0;
    private static final String DEFAULTPAGEORDER = "transferStock.fromLocationName";
    private static final String DEFAULTPAGEORDERMODE = "desc";

    private Integer startIndex = DEFAULTPAGESTARTINDEX;
    private Integer pageSize = DEFAULTPAGESIZE;
    private String pageOrderMode = DEFAULTPAGEORDERMODE;
    private String orderByFieldName = DEFAULTPAGEORDER;

    /*public Query toQuery(EntityManager entityManager) {
        StringBuilder sb = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        sb.append("SELECT transferStock FROM TransferStock transferStock, Product product, Location location " +
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
    }*/

    public String getLikeProductName() {
        return likeProductName;
    }

    public void setLikeProductName(String likeProductName) {
        this.likeProductName = likeProductName;
    }

    public String getLikeFromLocationName() {
        return likeFromLocationName;
    }

    public void setLikeFromLocationName(String likeFromLocationName) {
        this.likeFromLocationName = likeFromLocationName;
    }

    public String getLikeToLocationName() {
        return likeToLocationName;
    }

    public void setLikeToLocationName(String likeToLocationName) {
        this.likeToLocationName = likeToLocationName;
    }

    public String getLikeUpdatedStock() {
        return likeUpdatedStock;
    }

    public void setLikeUpdatedStock(String likeUpdatedStock) {
        this.likeUpdatedStock = likeUpdatedStock;
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
