package qa.tecnositafgulf.searchcriteria.inventory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class ProductSearchCriteria {

    private String likeProductCategoryName;
    private String likeProductName;
    private String likeProductDesc;

    private static final int DEFAULTPAGESIZE = 8;
    private static final int DEFAULTPAGESTARTINDEX = 0;
    private static final String DEFAULTPAGEORDER = "productName";
    private static final String DEFAULTPAGEORDERMODE = "desc";

    private Integer startIndex = DEFAULTPAGESTARTINDEX;
    private Integer pageSize = DEFAULTPAGESIZE;
    private String pageOrderMode = DEFAULTPAGEORDERMODE;
    private String orderByFieldName = DEFAULTPAGEORDER;

    public Query toQuery(EntityManager entityManager){
        StringBuilder sb = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        sb.append("SELECT product FROM Product product, ProductCategory productCategory where product.productCategory = productCategory.id ");
        if(getOrderByFieldName().equals("productCatgName")){
            stringBuilder.append(" ORDER BY productCategory.").append(getOrderByFieldName()).append(" ").append(getPageOrderMode()).toString();
        }else{
            stringBuilder.append(" ORDER BY product.").append(getOrderByFieldName()).append(" ").append(getPageOrderMode()).toString();
        }

        Query query = addConstraint(entityManager, sb, stringBuilder.toString());

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
        sb.append("SELECT COUNT(*) FROM Product product, ProductCategory productCategory where product.productCategory = productCategory.id ");
        Query query = addConstraint(em, sb, null);
        return query;
    }

    private Query addConstraint(EntityManager entityManager, StringBuilder sb, String orderBy){
        List<String> conditions = new ArrayList<>();
        boolean isProductCategoryName = (likeProductCategoryName != null && !likeProductCategoryName.isEmpty());
        boolean isProductName = (likeProductName != null && !likeProductName.isEmpty());
        boolean isProductDesc = (likeProductDesc != null && !likeProductDesc.isEmpty());

        if(isProductCategoryName)
            conditions.add("productCategory.productCatgName like :likeProductCategoryName");
        if(isProductName)
            conditions.add("product.productName like :likeProductName");
        if(isProductDesc)
            conditions.add("product.productDesc like :likeProductDesc");

        for (int i = 0; i < conditions.size(); i++) {
                sb.append(" and  ");
                sb.append(conditions.get(i));
        }

        //order By condition
        if (orderBy != null && !orderBy.isEmpty())
            sb.append(orderBy);

        // create query
        Query query = entityManager.createQuery(sb.toString());
        if(isProductCategoryName)
            query.setParameter("likeProductCategoryName", "%"+likeProductCategoryName+"%");
        if(isProductName)
            query.setParameter("likeProductName", "%"+likeProductName+"%");
        if(isProductDesc)
            query.setParameter("likeProductDesc", "%"+likeProductDesc+"%");

        return query;
    }

    public void clear() {
        startIndex = DEFAULTPAGESTARTINDEX;
        pageSize = DEFAULTPAGESIZE;
        pageOrderMode = DEFAULTPAGEORDERMODE;
        orderByFieldName = DEFAULTPAGEORDER;
    }

    public void clearFilters() {
        likeProductCategoryName = null;
        likeProductName = null;
        likeProductDesc = null;
    }

    public String getLikeProductCategoryName() {
        return likeProductCategoryName;
    }

    public void setLikeProductCategoryName(String likeProductCategoryName) {
        this.likeProductCategoryName = likeProductCategoryName;
    }

    public String getLikeProductName() {
        return likeProductName;
    }

    public void setLikeProductName(String likeProductName) {
        this.likeProductName = likeProductName;
    }

    public String getLikeProductDesc() {
        return likeProductDesc;
    }

    public void setLikeProductDesc(String likeProductDesc) {
        this.likeProductDesc = likeProductDesc;
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
