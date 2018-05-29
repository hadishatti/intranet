package qa.tecnositafgulf.service;

import qa.tecnositafgulf.model.suppliers.Supplier;
import qa.tecnositafgulf.searchcriteria.SupplierSearchCriteria;

import java.util.List;

/**
 * Created by ledio on 5/5/18.
 */
public interface SupplierService {
    void saveSupplier(Supplier supplier);
    void removeSupplier(Supplier supplier);

    List<Supplier> listSuppliers();

    List<Supplier> getSuppliers(SupplierSearchCriteria supplierSearchCriteria);

    Supplier findSupplier(Supplier supplier);

    public Integer geSupplierCount(SupplierSearchCriteria supplierSearchCriteria);
}
