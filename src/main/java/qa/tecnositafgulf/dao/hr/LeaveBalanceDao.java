package qa.tecnositafgulf.dao.hr;

import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.leaves.LeaveBalance;

/**
 * Created by hadi on 5/22/18.
 */
public interface LeaveBalanceDao {
    public void save(LeaveBalance balance);
    public int getBalance(Employee employee, String type);
}
