package qa.tecnositafgulf.dao.tenders;

import qa.tecnositafgulf.model.tenders.Tender;
import qa.tecnositafgulf.model.tenders.TenderComment;

import java.util.List;

/**
 * Created by hadi on 12/24/17.
 */
public interface TenderCommentDao {
    void save(TenderComment tenderComment);

    void remove(TenderComment tenderComment);

    List<TenderComment> listByTender(Tender tender);
}
