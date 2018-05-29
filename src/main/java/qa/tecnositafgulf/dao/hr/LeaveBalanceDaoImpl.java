package qa.tecnositafgulf.dao.hr;

import org.springframework.stereotype.Repository;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.leaves.LeaveBalance;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Created by hadi on 5/22/18.
 */
@Repository
public class LeaveBalanceDaoImpl implements LeaveBalanceDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(LeaveBalance balance) {
        LeaveBalance b = getBal(balance.getEmployee(),balance.getType());
        if(b==null)
            em.merge(balance);
        else{
            b.setValue(balance.getValue());
            em.merge(b);
        }
    }

    @Override
    public int getBalance(Employee employee, String type) {
        int balance = 0;
        TypedQuery<LeaveBalance> query = em.createNamedQuery("LeaveBalance.getBalance", LeaveBalance.class);
        query.setParameter("employee", employee);
        query.setParameter("type", type);
        try {
            LeaveBalance bal = query.getSingleResult();
            balance = bal.getValue();
        }catch (NoResultException e){
            balance = 21;
        }
        return balance;
    }

    private LeaveBalance getBal(Employee employee, String type) {
        LeaveBalance bal;
        TypedQuery<LeaveBalance> query = em.createNamedQuery("LeaveBalance.getBalance", LeaveBalance.class);
        query.setParameter("employee", employee);
        query.setParameter("type", type);
        try {
            bal = query.getSingleResult();
        }catch (NoResultException e){
            bal = null;
        }
        return bal;
    }
}
