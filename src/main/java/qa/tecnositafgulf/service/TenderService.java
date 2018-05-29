package qa.tecnositafgulf.service;

import qa.tecnositafgulf.model.tenders.Tender;
import qa.tecnositafgulf.searchcriteria.tender.TenderSearchCriteria;

import java.util.List;

/**
 * Created by hadi on 12/24/17.
 */
public interface TenderService {
    void saveTender(Tender tender);

    void removeTender(Tender tender);

    List<Tender> listTenders();

    List<Tender> listTenders(TenderSearchCriteria tenderSearchCriteria);

    Integer countTenders(TenderSearchCriteria tenderSearchCriteria);

    Tender findTender(Tender tender);
}
