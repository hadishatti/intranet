package qa.tecnositafgulf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qa.tecnositafgulf.dao.tenders.TenderDao;
import qa.tecnositafgulf.model.tenders.Tender;
import qa.tecnositafgulf.searchcriteria.tender.TenderSearchCriteria;

import java.util.List;

/**
 * Created by hadi on 12/24/17.
 */
@Service
public class TenderServiceImpl implements TenderService{

    @Autowired
    private TenderDao tenderDao;

    @Transactional
    public void saveTender(Tender tender) {
        tenderDao.save(tender);
    }

    @Transactional
    public void removeTender(Tender tender) {
        tenderDao.remove(tender);
    }

    @Transactional
    public List<Tender> listTenders() {
        return tenderDao.listAll();
    }

    @Transactional
    public Tender findTender(Tender tender){
        return tenderDao.find(tender);
    }

    @Override
    @Transactional
    public List<Tender> listTenders(TenderSearchCriteria tenderSearchCriteria) {
        return tenderDao.listTenders(tenderSearchCriteria);
    }

    @Override
    @Transactional
    public Integer countTenders(TenderSearchCriteria tenderSearchCriteria) {
        return tenderDao.countTender(tenderSearchCriteria);
    }
}
