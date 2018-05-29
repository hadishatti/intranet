package qa.tecnositafgulf.dao.administration.buildings;

import org.hibernate.exception.ConstraintViolationException;
import qa.tecnositafgulf.model.administration.Company;
import qa.tecnositafgulf.searchcriteria.CompanySearchCriteria;

import java.util.List;

/**
 * Created by hadi on 12/17/17.
 */
public interface CompanyDao {

    void save(Company company);

    void remove(Company company) throws ConstraintViolationException;

    List<Company> listAll();

    int countAll();

    Integer getCompanyCount(CompanySearchCriteria companySearchCriteria);

    List<Company> getCompanyList(CompanySearchCriteria companySearchCriteria);
}