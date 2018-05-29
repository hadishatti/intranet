package qa.tecnositafgulf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qa.tecnositafgulf.dao.companyInfo.policiesProcedures.PolicyProcedureDao;
import qa.tecnositafgulf.model.companyInfo.PolicyProcedure;

import java.util.List;

/**
 * Created by ledio on 5/16/18.
 */

@Service
public class PolicyProcedureServiceImpl implements PolicyProcedureService {

    @Autowired
    private PolicyProcedureDao policyProcedureDao;

    @Override
    @Transactional
    public void savePolicyProcedure(PolicyProcedure policyProcedure) {
        policyProcedureDao.save(policyProcedure);
    }

    @Override
    @Transactional
    public void removePolicyProcedure(PolicyProcedure policyProcedure) {
        policyProcedureDao.remove(policyProcedure);
    }

    @Override
    @Transactional
    public List<PolicyProcedure> listPolicyProcedures() {
        return policyProcedureDao.listAll();
    }

    @Override
    @Transactional
    public PolicyProcedure findPolicyProcedure(PolicyProcedure policyProcedure) {
        return policyProcedureDao.find(policyProcedure);
    }
}
