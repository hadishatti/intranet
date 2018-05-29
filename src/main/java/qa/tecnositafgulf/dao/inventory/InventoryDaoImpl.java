package qa.tecnositafgulf.dao.inventory;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;
import qa.tecnositafgulf.model.calendar.QueueMessage;
import qa.tecnositafgulf.model.inventory.location.Location;
import qa.tecnositafgulf.model.inventory.product.Product;
import qa.tecnositafgulf.model.inventory.productCategory.ProductCategory;
import qa.tecnositafgulf.model.inventory.stocks.ProductStock;
import qa.tecnositafgulf.model.inventory.stocks.TransferStock;
import qa.tecnositafgulf.model.inventory.supplier.ProductSupplier;
import qa.tecnositafgulf.searchcriteria.inventory.*;

import javax.persistence.*;
import java.util.List;

@Repository
public class InventoryDaoImpl implements InventoryDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(ProductCategory productCategory) {
        entityManager.merge(productCategory);
    }

    @Override
    public Integer getProductCategoryCount(ProductCategorySearchCriteria productCategorySearchCriteria) {
        Query query = productCategorySearchCriteria.getCountQuery(entityManager);
        Number cResults = (Number) query.getSingleResult();
        return cResults.intValue();
    }

    @Override
    public List<ProductCategory> getAllProductCategories() {
        return entityManager.createNamedQuery("ProductCategory.getAllProductCategory", ProductCategory.class).getResultList();
    }

    @Override
    public List<ProductCategory> getAllProductCategories(ProductCategorySearchCriteria productCategorySearchCriteria) {
        Query query = productCategorySearchCriteria.toQuery(entityManager);
        return query.getResultList();
    }

    @Override
    public List<Product> getAllProducts(ProductSearchCriteria productSearchCriteria) {
        Query query = productSearchCriteria.toQuery(entityManager);
        return query.getResultList();
    }

    @Override
    public Integer getAllProductCount(ProductSearchCriteria productSearchCriteria) {
        Query query = productSearchCriteria.getCountQuery(entityManager);
        Number cResults = (Number) query.getSingleResult();
        return cResults.intValue();
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(Product product) {
        entityManager.merge(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return entityManager.createNamedQuery("Product.getAllProducts", Product.class).getResultList();
    }

    @Override
    public void remove(Product product) throws ConstraintViolationException {
        entityManager.remove(entityManager.contains(product) ? product : entityManager.merge(product));
    }

    @Override
    public void remove(ProductCategory productCategory) {
        entityManager.remove(entityManager.contains(productCategory) ? productCategory : entityManager.merge(productCategory));
    }

    @Override
    public void save(ProductStock productStock) {
        entityManager.merge(productStock);
    }

    @Override
    public List<ProductStock> getAllProductsProductStocks() {
        return entityManager.createNamedQuery("ProductStock.getAllProductStock", ProductStock.class).getResultList();
    }

    @Override
    public ProductStock getProductStockLocationWise(Product product, Location location) {
        TypedQuery<ProductStock> query = entityManager.createQuery("select productStock from ProductStock productStock where productStock.product.id=:product and productStock.location=:location", ProductStock.class);
        query.setParameter("product", product);
        query.setParameter("location", location);
        ProductStock productStock = null;
        try {
            productStock = query.getSingleResult();
        } catch (NoResultException noResultException) {
            System.out.println("No result found");
        }
        return productStock;
    }

    @Override
    public void remove(Location location) throws ConstraintViolationException {
        location = entityManager.find(Location.class, location.getId());
        entityManager.remove(location);
    }

    @Override
    public void remove(ProductStock productStock) throws ConstraintViolationException {
        entityManager.remove(entityManager.contains(productStock) ? productStock : entityManager.merge(productStock));
    }

    @Override
    public void saveProductSupplier(ProductSupplier productSupplier) {
        entityManager.merge(productSupplier);
    }

    @Override
    public void removeProductSupplier(ProductSupplier productSupplier) {
        entityManager.remove(entityManager.contains(productSupplier) ? productSupplier : entityManager.merge(productSupplier));
    }

    @Override
    public List<ProductSupplier> listProductSuppliers() {
        TypedQuery<ProductSupplier> query = entityManager.createNamedQuery("ProductSupplier.getAllSuppliers", ProductSupplier.class);

        return query.getResultList();
    }

    @Override
    public List<ProductSupplier> getAllProductSuppliers(ProductSupplierSearchCriteria productSupplierSearchCriteria) {
        Query query = productSupplierSearchCriteria.toQuery(entityManager);
        return query.getResultList();
    }

    @Override
    public int getAllProductSuppliersCount(ProductSupplierSearchCriteria productSupplierSearchCriteria) {
        Query query = productSupplierSearchCriteria.getCountQuery(entityManager);
        Number cResults = (Number) query.getSingleResult();
        return cResults.intValue();
    }

    @Override
    public List<ProductStock> getAllProductsProductStocks(ProductStockSearchCriteria productStockSearchCriteria) {
        Query query = productStockSearchCriteria.toQuery(entityManager);
        return query.getResultList();
    }

    @Override
    public Integer getAllProductsProductStockCount(ProductStockSearchCriteria productStockSearchCriteria) {
        Query query = productStockSearchCriteria.getCountQuery(entityManager);
        Number cResults = (Number) query.getSingleResult();
        return cResults.intValue();
    }

    @Override
    public List<Product> getProductStockOnLocation(Location location) {
        String queryString = "select product from Product product, ProductStock productStock where productStock.product=product.id and productStock.location=:location";
        TypedQuery<Product> productStockTypedQuery = entityManager.createQuery(queryString, Product.class);
        productStockTypedQuery.setParameter("location", location);
        return productStockTypedQuery.getResultList();
    }

    @Override
    public void saveLocation(Location location) {
        entityManager.merge(location);
    }

    @Override
    public List<Location> getAllLocations(LocationSearchCriteria locationSearchCriteria) {
        Query query = locationSearchCriteria.toQuery(entityManager);
        return query.getResultList();
    }

    @Override
    public Integer getAllLocationsCount(LocationSearchCriteria locationSearchCriteria) {
        Query query = locationSearchCriteria.getCountQuery(entityManager);
        Number cResults = (Number) query.getSingleResult();
        return cResults.intValue();
    }

    @Override
    public List<Location> getAllLocation() {
        return entityManager.createNamedQuery("Location.getAllLocations", Location.class).getResultList();
    }

    @Override
    public ProductStock getProductStockOnProductLocation(Location location, Product product) {
        TypedQuery<ProductStock> query = entityManager.createQuery("select productStock from ProductStock productStock where productStock.product=:product and productStock.location=:location", ProductStock.class);
        query.setParameter("product", product);
        query.setParameter("location", location);
        ProductStock productStock = null;
        try {
            productStock = query.getSingleResult();
        } catch (NoResultException noResultException) {
            System.out.println("No result found");
        }
        return productStock;
    }

    @Override
    public Location getLocationByName(String location) {
        TypedQuery<Location> locationTypedQuery = entityManager.createNamedQuery("Location.getLocationByName", Location.class);
        locationTypedQuery.setParameter("name", location);
        return locationTypedQuery.getSingleResult();
    }

    @Override
    public void updateProductStocks(ProductStock fromProductStock, ProductStock toProductStock) {
        entityManager.merge(fromProductStock);
        entityManager.merge(toProductStock);
    }

    @Override
    public void addStockTransferDetails(TransferStock transferStock) {
        entityManager.merge(transferStock);
    }
}
