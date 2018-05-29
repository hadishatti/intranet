package qa.tecnositafgulf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qa.tecnositafgulf.dao.suppliers.SupplierDao;
import qa.tecnositafgulf.model.suppliers.Supplier;
import qa.tecnositafgulf.searchcriteria.SupplierSearchCriteria;

import java.util.List;

/**
 * Created by ledio on 5/5/18.
 */
@Service
public class SupplierServiceImpl implements SupplierService {
    @Autowired
    private SupplierDao supplierDao;

    @Override
    public void saveSupplier(Supplier supplier) {
        supplierDao.save(supplier);
    }

    @Override
    public void removeSupplier(Supplier supplier) {
        supplierDao.remove(supplier);
    }

    @Override
    public List<Supplier> listSuppliers() {
        return supplierDao.listAll();
    }

    @Override
    public List<Supplier> getSuppliers(SupplierSearchCriteria supplierSearchCriteria) {
        return supplierDao.getAll(supplierSearchCriteria);
    }

    @Override
    public Supplier findSupplier(Supplier supplier) {
        return supplierDao.findByName(supplier.getName());
    }

    @Override
    public Integer geSupplierCount(SupplierSearchCriteria supplierSearchCriteria) {
        return supplierDao.countAll(supplierSearchCriteria);
    }
}
