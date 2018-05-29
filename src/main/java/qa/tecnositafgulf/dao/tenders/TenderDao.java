package qa.tecnositafgulf.dao.tenders;

import qa.tecnositafgulf.model.tenders.Tender;
import qa.tecnositafgulf.searchcriteria.tender.TenderSearchCriteria;

import java.util.List;

/**
 * Created by hadi on 12/24/17.
 */
public interface TenderDao {
    void save(Tender tender);

    void remove(Tender tender);

    List<Tender> listAll();

    List<Tender> listTenders(TenderSearchCriteria tenderSearchCriteria);

    Integer countTender(TenderSearchCriteria tenderSearchCriteria);

    Tender find(Tender tender);
}
