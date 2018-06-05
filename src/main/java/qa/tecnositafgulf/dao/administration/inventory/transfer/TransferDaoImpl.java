package qa.tecnositafgulf.dao.administration.inventory.transfer;

import org.springframework.stereotype.Repository;
import qa.tecnositafgulf.model.administration.inventory.Transfer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by ledio on 6/5/18.
 */

@Repository
public class TransferDaoImpl implements TransferDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(Transfer transfer) {
        em.merge(transfer);
    }

    @Override
    public void remove(Transfer transfer) {
        em.remove(em.contains(transfer) ? transfer : em.merge(transfer));
    }

    @Override
    public List<Transfer> getAllTransfers() {
        TypedQuery<Transfer> query = em.createNamedQuery("Transfer.getAllTransfers", Transfer.class);
        return query.getResultList();
    }

    @Override
    public Integer countAllTransfers() {
        Query query = em.createNamedQuery("Transfer.countAllTransfers");
        return ((Long)query.getSingleResult()).intValue();
    }
}
