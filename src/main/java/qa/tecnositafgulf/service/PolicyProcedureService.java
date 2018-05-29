package qa.tecnositafgulf.service;

import qa.tecnositafgulf.model.companyInfo.PolicyProcedure;

import java.util.List;

/**
 * Created by ledio on 5/16/18.
 */
public interface PolicyProcedureService {
    void savePolicyProcedure(PolicyProcedure policyProcedure);

    void removePolicyProcedure(PolicyProcedure policyProcedure);

    List<PolicyProcedure> listPolicyProcedures();

    PolicyProcedure findPolicyProcedure(PolicyProcedure policyProcedure);
}
