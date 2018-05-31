package qa.tecnositafgulf.service;

import qa.tecnositafgulf.model.inventory.location.Location;
import qa.tecnositafgulf.model.inventory.product.Product;
import qa.tecnositafgulf.model.inventory.productCategory.ProductCategory;
import qa.tecnositafgulf.model.inventory.stocks.ProductStock;
import qa.tecnositafgulf.model.inventory.stocks.TransferStock;
import qa.tecnositafgulf.model.inventory.supplier.ProductSupplier;
import qa.tecnositafgulf.searchcriteria.inventory.*;

import java.util.List;

public interface InventoryService {

    public void save(ProductCategory productCategory);

    public Integer getProductCategoryCount(ProductCategorySearchCriteria productCategorySearchCriteria);

    public List<ProductCategory> getAllProductCategories();

    public List<ProductCategory> getAllProductCategories(ProductCategorySearchCriteria productCategorySearchCriteria);

    public void remove(ProductCategory productCategory);

    public void save(Product product);

    public List<Product> getAllProducts();

    public List<Product> getAllProducts(ProductSearchCriteria productSearchCriteria);

    public Integer getAllProductCount(ProductSearchCriteria productSearchCriteria);

    public void remove(Product product);

    public void save(ProductStock productStock);

    public List<ProductStock> getAllProductsProductStocks();

    public List<ProductStock> getAllProductsProductStocks(ProductStockSearchCriteria productStockSearchCriteria);

    public Integer getAllProductsProductStockCount(ProductStockSearchCriteria productStockSearchCriteria);

    public List<Product> getProductStockOnLocation(Location location);

    public void remove(ProductStock productStock);

    public ProductStock getProductStockLocationWise(Product product, Location location);

    public void saveProductSupplier(ProductSupplier productSupplier);

    public void removeProductSupplier(ProductSupplier productSupplier);

    public List<ProductSupplier> listAllProductSuppliers();

    public List<ProductSupplier> getProductSuppliers(ProductSupplierSearchCriteria productStockSearchCriteria);

    public int getProductSuppliersCount(ProductSupplierSearchCriteria productStockSearchCriteria);

    public void saveLocation(Location location);

    public ProductStock getProductStockOnProductLocation(Location location, Product product);

    public void remove(Location location);

    public List<Location> getAllLocations();

    public List<Location> getAllLocations(LocationSearchCriteria locationSearchCriteria);

    public Integer getAllLocationsCount(LocationSearchCriteria locationSearchCriteria);

    public Location getLocationByName(String location);

    public void updateProductStocks(ProductStock fromProductStock, ProductStock toProductStock);

    public void addStockTransferDetails(TransferStock transferStock);

}
