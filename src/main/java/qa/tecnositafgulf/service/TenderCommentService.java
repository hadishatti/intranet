package qa.tecnositafgulf.service;

import qa.tecnositafgulf.model.tenders.Tender;
import qa.tecnositafgulf.model.tenders.TenderComment;

import java.util.List;

/**
 * Created by hadi on 12/24/17.
 */
public interface TenderCommentService {
    void saveComment(TenderComment tenderComment);

    void removeComment(TenderComment tenderComment);

    List<TenderComment> listCommentsByTender(Tender tender);
}
