package qa.tecnositafgulf.dao.companyInfo;

import qa.tecnositafgulf.model.companyInfo.Partner;
import qa.tecnositafgulf.searchcriteria.PartnerSearchCriteria;

import java.util.List;

/**
 * Created by ameljo on 05/05/18.
 */
public interface  PartnerDao {

    void save (Partner partner);

    void remove (Partner partner);

    List<Partner> listAll();

    List<Partner> getPartnersList(PartnerSearchCriteria searchCriteria);

    int getPartnersCount(PartnerSearchCriteria searchCriteria);

    int countAll();
}
