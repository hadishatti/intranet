package qa.tecnositafgulf.dao.companyInfo.policiesProcedures;

import qa.tecnositafgulf.model.companyInfo.PolicyProcedure;

import java.util.List;

/**
 * Created by ledio on 5/16/18.
 */
public interface PolicyProcedureDao {
    void save(PolicyProcedure policyProcedure);

    void remove(PolicyProcedure policyProcedure);

    List<PolicyProcedure> listAll();

    PolicyProcedure find(PolicyProcedure policyProcedure);
}
