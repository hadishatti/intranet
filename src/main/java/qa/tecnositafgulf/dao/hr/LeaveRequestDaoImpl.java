package qa.tecnositafgulf.dao.hr;

import org.springframework.stereotype.Repository;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.leaves.LeaveRequest;
import qa.tecnositafgulf.searchcriteria.hr.LeaveRequestSearchCriteria;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by hadi on 12/31/17.
 */
@Repository
public class LeaveRequestDaoImpl implements LeaveRequestDao{

    @PersistenceContext
    private EntityManager em;

    public void save(LeaveRequest request) {
        em.merge(request);
    }

    public void remove(LeaveRequest request) {
        em.remove(em.contains(request)?request:em.merge(request));
    }

    public List<LeaveRequest> listApproved() {
        TypedQuery<LeaveRequest> query = em.createNamedQuery("LeaveRequest.listApproved", LeaveRequest.class);
        return query.getResultList();
    }

    public List<LeaveRequest> listSickLeaves() {
        TypedQuery<LeaveRequest> query = em.createNamedQuery("LeaveRequest.listSickLeaves", LeaveRequest.class);
        return query.getResultList();
    }

    public List<LeaveRequest> listEmergencyLeaves() {
        TypedQuery<LeaveRequest> query = em.createNamedQuery("LeaveRequest.listEmergencyLeaves", LeaveRequest.class);
        return query.getResultList();
    }

    public List<LeaveRequest> findByNumber(String number) {
        TypedQuery<LeaveRequest> query = em.createNamedQuery("LeaveRequest.findByNumber", LeaveRequest.class);
        query.setParameter("number", number);
        return query.getResultList();
    }

    public LeaveRequest findByNumberAndStatus(String number, int status) {
        TypedQuery<LeaveRequest> query = em.createNamedQuery("LeaveRequest.findByNumberAndStatus", LeaveRequest.class);
        query.setParameter("number", number);
        query.setParameter("status",status);
        LeaveRequest r;
        try {
            r = query.getSingleResult();
        }catch (NoResultException e){
            r=null;
        }
        return  r;
    }

    public List<LeaveRequest> listLastByApplicant(Employee applicant) {
        TypedQuery<LeaveRequest> query = em.createNamedQuery("LeaveRequest.listLastByApplicant", LeaveRequest.class);
        query.setParameter("applicant", applicant);
        return query.getResultList();
    }

    public List<LeaveRequest> listLastByApplicant(Employee applicant, Date leaveFrom, Date leaveTo) {
        TypedQuery<LeaveRequest> query = em.createNamedQuery("LeaveRequest.listLastByApplicantAndDates", LeaveRequest.class);
        query.setParameter("leaveFrom", leaveFrom);
        query.setParameter("leaveTo", leaveTo);
        query.setParameter("applicant", applicant);
        return query.getResultList();
    }

    public List<LeaveRequest> listLastByApplicant(LeaveRequestSearchCriteria sc) {
        Query query = sc.toQuery(em);
        return query.getResultList();
    }

    public Integer listLastByApplicantCount(LeaveRequestSearchCriteria sc) {
        Query query = sc.getCountQuery(em);
        Number cResults = (Number) query.getSingleResult();
        return cResults.intValue();
    }

    public List<LeaveRequest> listLastByApplicants(LeaveRequestSearchCriteria sc) {
        Query query = sc.toQuery1(em);
        return query.getResultList();
    }

    public Integer listLastByApplicantsCount(LeaveRequestSearchCriteria sc) {
        Query query = sc.getCountQuery1(em);
        Number cResults = (Number) query.getSingleResult();
        return cResults.intValue();
    }

    public List<LeaveRequest> listLastByApprover(LeaveRequestSearchCriteria sc) {
        Query query = sc.toQuery2(em);
        return query.getResultList();
    }

    public Integer listLastByApproverCount(LeaveRequestSearchCriteria sc) {
        Query query = sc.getCountQuery2(em);
        Number cResults = (Number) query.getSingleResult();
        return cResults.intValue();
    }

    public List<LeaveRequest> listSickLeavesByApprover(LeaveRequestSearchCriteria sc) {
        Query query = sc.toQuery3(em);
        return query.getResultList();
    }

    public Integer listSickLeavesByApproverCount(LeaveRequestSearchCriteria sc) {
        Query query = sc.getCountQuery3(em);
        Number cResults = (Number) query.getSingleResult();
        return cResults.intValue();
    }

    public List<LeaveRequest> listEmergencyLeavesByApprover(LeaveRequestSearchCriteria sc) {
        Query query = sc.toQuery4(em);
        return query.getResultList();
    }

    public Integer listEmergencyLeavesByApproverCount(LeaveRequestSearchCriteria sc) {
        Query query = sc.getCountQuery4(em);
        Number cResults = (Number) query.getSingleResult();
        return cResults.intValue();
    }

    public List<LeaveRequest> listLastByApprover(Employee approver) {
        TypedQuery<LeaveRequest> query = em.createNamedQuery("LeaveRequest.listLastByApprover", LeaveRequest.class);
        query.setParameter("approver", approver);
        return query.getResultList();
    }

    public int countPendingByApplicantAndDates(Employee applicant, Date leaveFrom, Date leaveTo) {
        Query query = em.createNamedQuery("LeaveRequest.countPendingByApplicantAndDates");
        query.setParameter("applicant",applicant);
        query.setParameter("leaveFrom",leaveFrom);
        query.setParameter("leaveTo",leaveTo);
        return ((Long)query.getSingleResult()).intValue();
    }

    public List<LeaveRequest> listLastToApproveByApprover(Employee approver) {
        TypedQuery<LeaveRequest> query = em.createNamedQuery("LeaveRequest.listLastToApproveByApprover", LeaveRequest.class);
        query.setParameter("approver", approver);
        return query.getResultList();
    }

}
