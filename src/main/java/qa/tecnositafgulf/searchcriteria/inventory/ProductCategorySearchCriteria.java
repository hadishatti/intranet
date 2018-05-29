package qa.tecnositafgulf.searchcriteria.inventory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class ProductCategorySearchCriteria {

    private String likeProductCategoryName;
    private String likeProductCategoryDesc;

    private static final int DEFAULTPAGESIZE = 8;
    private static final int DEFAULTPAGESTARTINDEX = 0;
    private static final String DEFAULTPAGEORDER = "productCatgName";
    private static final String DEFAULTPAGEORDERMODE = "desc";

    private Integer startIndex = DEFAULTPAGESTARTINDEX;
    private Integer pageSize = DEFAULTPAGESIZE;
    private String pageOrderMode = DEFAULTPAGEORDERMODE;
    private String orderByFieldName = DEFAULTPAGEORDER;

    public Query toQuery(EntityManager entityManager){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT productCategory FROM ProductCategory productCategory ");
        Query query = addConstraint(entityManager, sb,
                new StringBuilder(" ORDER BY productCategory.").append(getOrderByFieldName()).append(" ").append(getPageOrderMode()).toString());

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
        sb.append("SELECT COUNT(*) FROM ProductCategory productCategory ");
        Query query = addConstraint(em, sb, null);
        return query;
    }

    private Query addConstraint(EntityManager entityManager, StringBuilder sb, String orderBy){

        List<String> conditions = new ArrayList<>();
        boolean isProductCategoryName = (likeProductCategoryName != null && !likeProductCategoryName.isEmpty());
        boolean isProductCategoryDesc = (likeProductCategoryDesc != null && !likeProductCategoryDesc.isEmpty());

        if(isProductCategoryName)
            conditions.add("productCategory.productCatgName like :likeProductCategoryName");
        if(isProductCategoryDesc)
            conditions.add("productCategory.productCatgDesc like :likeProductCategoryDesc");

        for (int i = 0; i < conditions.size(); i++) {
            if(i == 0){
                sb.append(" where  ");
                sb.append(conditions.get(i));
            }else{
                sb.append(" and  ");
                sb.append(conditions.get(i));
            }
        }

        //order By condition
        if (orderBy != null && !orderBy.isEmpty())
            sb.append(orderBy);

        // create query
        Query query = entityManager.createQuery(sb.toString());
        if(isProductCategoryName)
            query.setParameter("likeProductCategoryName", "%"+likeProductCategoryName+"%");
        if(isProductCategoryDesc)
            query.setParameter("likeProductCategoryDesc", "%"+likeProductCategoryDesc+"%");

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
        likeProductCategoryDesc = null;
    }


    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
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

    public String getLikeProductCategoryName() {
        return likeProductCategoryName;
    }

    public void setLikeProductCategoryName(String likeProductCategoryName) {
        this.likeProductCategoryName = likeProductCategoryName;
    }

    public String getLikeProductCategoryDesc() {
        return likeProductCategoryDesc;
    }

    public void setLikeProductCategoryDesc(String likeProductCategoryDesc) {
        this.likeProductCategoryDesc = likeProductCategoryDesc;
    }
}
