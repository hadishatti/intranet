package qa.tecnositafgulf.dao.suppliers;

import org.hibernate.exception.ConstraintViolationException;
import qa.tecnositafgulf.model.suppliers.Supplier;
import qa.tecnositafgulf.searchcriteria.SupplierSearchCriteria;

import java.util.List;

/**
 * Created by ledio on 5/5/18.
 */
public interface SupplierDao {
    void save(Supplier supplier)throws ConstraintViolationException;

    void remove(Supplier supplier) throws ConstraintViolationException;

    List<Supplier> listAll();

    Supplier findByName(String username);

    List<Supplier> getAll(SupplierSearchCriteria supplierSearchCriteria);

    Integer countAll(SupplierSearchCriteria supplierSearchCriteria);
}
