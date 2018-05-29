package qa.tecnositafgulf.dao.tenders;

import org.springframework.stereotype.Repository;
import qa.tecnositafgulf.model.tenders.Tender;
import qa.tecnositafgulf.model.tenders.TenderComment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by hadi on 12/24/17.
 */
@Repository
public class TenderCommentDaoImpl implements TenderCommentDao {

    @PersistenceContext
    private EntityManager em;

    public void save(TenderComment tenderComment) {
        em.merge(tenderComment);
    }

    public void remove(TenderComment tenderComment) {
        em.remove(em.contains(tenderComment) ? tenderComment : em.merge(tenderComment));
    }

    public List<TenderComment> listByTender(Tender tender) {
        TypedQuery<TenderComment> query = em.createNamedQuery("TenderComment.listByTender", TenderComment.class);
        query.setParameter("tender", tender);
        return query.getResultList();
    }
}
