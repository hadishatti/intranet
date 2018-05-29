package qa.tecnositafgulf.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qa.tecnositafgulf.dao.tenders.TenderCommentDao;
import qa.tecnositafgulf.model.tenders.Tender;
import qa.tecnositafgulf.model.tenders.TenderComment;

import java.util.List;

/**
 * Created by hadi on 12/24/17.
 */
@Service
public class TenderCommentServiceImpl implements TenderCommentService {

    @Autowired
    private TenderCommentDao tenderCommentDao;

    @Transactional
    public void saveComment(TenderComment tenderComment) {
        tenderCommentDao.save(tenderComment);
    }

    @Transactional
    public void removeComment(TenderComment tenderComment) {
        tenderCommentDao.remove(tenderComment);
    }

    @Transactional
    public List<TenderComment> listCommentsByTender(Tender tender) {
        return tenderCommentDao.listByTender(tender);
    }
}
