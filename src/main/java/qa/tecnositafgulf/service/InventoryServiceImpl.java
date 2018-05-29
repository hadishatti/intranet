package qa.tecnositafgulf.service;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qa.tecnositafgulf.dao.inventory.InventoryDao;
import qa.tecnositafgulf.model.inventory.location.Location;
import qa.tecnositafgulf.model.inventory.product.Product;
import qa.tecnositafgulf.model.inventory.productCategory.ProductCategory;
import qa.tecnositafgulf.model.inventory.stocks.ProductStock;
import qa.tecnositafgulf.model.inventory.stocks.TransferStock;
import qa.tecnositafgulf.model.inventory.supplier.ProductSupplier;
import qa.tecnositafgulf.searchcriteria.inventory.LocationSearchCriteria;
import qa.tecnositafgulf.searchcriteria.inventory.ProductCategorySearchCriteria;
import qa.tecnositafgulf.searchcriteria.inventory.ProductSearchCriteria;
import qa.tecnositafgulf.searchcriteria.inventory.ProductStockSearchCriteria;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryDao inventoryDao;

    @Override
    @Transactional
    public void save(ProductCategory productCategory) {
        inventoryDao.save(productCategory);
    }

    @Override
    @Transactional
    public Integer getProductCategoryCount(ProductCategorySearchCriteria productCategorySearchCriteria) {
        return inventoryDao.getProductCategoryCount(productCategorySearchCriteria);
    }

    @Override
    @Transactional
    public List<ProductCategory> getAllProductCategories(ProductCategorySearchCriteria productCategorySearchCriteria) {
        return inventoryDao.getAllProductCategories(productCategorySearchCriteria);
    }

    @Override
    @Transactional
    public List<ProductCategory> getAllProductCategories() {
        return inventoryDao.getAllProductCategories();
    }

    @Override
    @Transactional
    public Integer getAllProductCount(ProductSearchCriteria productSearchCriteria) {
        return inventoryDao.getAllProductCount(productSearchCriteria);
    }

    @Override
    @Transactional
    public void save(Product product) {
        inventoryDao.save(product);
    }

    @Override
    @Transactional
    public List<Product> getAllProducts() {
        return inventoryDao.getAllProducts();
    }

    @Override
    @Transactional
    public List<Product> getAllProducts(ProductSearchCriteria productSearchCriteria){
        return inventoryDao.getAllProducts(productSearchCriteria);
    }

    @Override
    @Transactional
    public void remove(Product product) throws ConstraintViolationException {
        inventoryDao.remove(product);
    }

    @Override
    @Transactional
    public void remove(ProductCategory productCategory) {
        inventoryDao.remove(productCategory);
    }

    @Override
    @Transactional
    public void save(ProductStock productStock) {
        inventoryDao.save(productStock);
    }

    @Override
    @Transactional
    public List<ProductStock> getAllProductsProductStocks() {
        return inventoryDao.getAllProductsProductStocks();
    }

    @Override
    @Transactional
    public List<ProductStock> getAllProductsProductStocks(ProductStockSearchCriteria productStockSearchCriteria) {
        return inventoryDao.getAllProductsProductStocks(productStockSearchCriteria);
    }

    @Override
    @Transactional
    public Integer getAllProductsProductStockCount(ProductStockSearchCriteria productStockSearchCriteria) {
        return inventoryDao.getAllProductsProductStockCount(productStockSearchCriteria);
    }


    @Override
    @Transactional
    public List<Product> getProductStockOnLocation(Location location) {
        return inventoryDao.getProductStockOnLocation(location);
    }

    @Override
    @Transactional
    public void remove(ProductStock productStock) throws ConstraintViolationException{
        inventoryDao.remove(productStock);
    }

    @Override
    @Transactional
    public void saveProductSupplier(ProductSupplier productSupplier) {
        inventoryDao.saveProductSupplier(productSupplier);
    }

    @Override
    @Transactional
    public void saveLocation(Location location) {
        inventoryDao.saveLocation(location);
    }

    @Override
    @Transactional
    public void remove(Location location) throws ConstraintViolationException{
        inventoryDao.remove(location);
    }

    @Override
    @Transactional
    public List<Location> getAllLocations() {
        return inventoryDao.getAllLocation();
    }

    @Override
    @Transactional
    public ProductStock getProductStockLocationWise(Product product, Location location) {
        return inventoryDao.getProductStockLocationWise(product, location);
    }

    @Override
    @Transactional
    public ProductStock getProductStockOnProductLocation(Location location, Product product) {
        return inventoryDao.getProductStockOnProductLocation(location, product);
    }

    @Override
    @Transactional
    public List<Location> getAllLocations(LocationSearchCriteria locationSearchCriteria) {
        return inventoryDao.getAllLocations(locationSearchCriteria);
    }

    @Override
    @Transactional
    public Integer getAllLocationsCount(LocationSearchCriteria locationSearchCriteria) {
        return inventoryDao.getAllLocationsCount(locationSearchCriteria);
    }

    @Override
    @Transactional
    public Location getLocationByName(String location) {
        return inventoryDao.getLocationByName(location);
    }

    @Override
    @Transactional
    public void updateProductStocks(ProductStock fromProductStock, ProductStock toProductStock) {
        inventoryDao.updateProductStocks(fromProductStock, toProductStock);
    }

    @Override
    @Transactional
    public void addStockTransferDetails(TransferStock transferStock) {
        inventoryDao.addStockTransferDetails(transferStock);
    }

    public InventoryDao getInventoryDao() {
        return inventoryDao;
    }

    public void setInventoryDao(InventoryDao inventoryDao) {
        this.inventoryDao = inventoryDao;
    }


}
