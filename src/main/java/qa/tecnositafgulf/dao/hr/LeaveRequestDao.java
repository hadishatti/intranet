package qa.tecnositafgulf.dao.hr;

import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.leaves.LeaveRequest;
import qa.tecnositafgulf.searchcriteria.hr.LeaveRequestSearchCriteria;

import java.util.Date;
import java.util.List;

/**
 * Created by hadi on 12/31/17.
 */
public interface LeaveRequestDao {
    void save(LeaveRequest request);
    void remove(LeaveRequest request);
    List<LeaveRequest> listApproved();
    List<LeaveRequest> listSickLeaves();
    List<LeaveRequest> listEmergencyLeaves();
    List<LeaveRequest> listSickLeavesByApprover(LeaveRequestSearchCriteria sc);
    Integer listSickLeavesByApproverCount(LeaveRequestSearchCriteria sc);
    List<LeaveRequest> listEmergencyLeavesByApprover(LeaveRequestSearchCriteria sc);
    Integer listEmergencyLeavesByApproverCount(LeaveRequestSearchCriteria sc);
    List<LeaveRequest> listLastByApplicant(Employee applicant);
    public List<LeaveRequest> listLastByApplicant(Employee applicant, Date leaveFrom, Date leaveTo);
    public List<LeaveRequest> listLastByApplicant(LeaveRequestSearchCriteria sc);
    public Integer listLastByApplicantCount(LeaveRequestSearchCriteria sc) ;
    public List<LeaveRequest> listLastByApplicants(LeaveRequestSearchCriteria sc);
    public Integer listLastByApplicantsCount(LeaveRequestSearchCriteria sc) ;
    List<LeaveRequest> listLastByApprover(Employee approver);
    List<LeaveRequest> listLastByApprover(LeaveRequestSearchCriteria sc);
    Integer listLastByApproverCount(LeaveRequestSearchCriteria sc);
    int countPendingByApplicantAndDates(Employee applicant, Date leaveFrom, Date leaveTo);
    List<LeaveRequest> listLastToApproveByApprover(Employee approver);
    public List<LeaveRequest> findByNumber(String number);
    public LeaveRequest findByNumberAndStatus(String number, int status);
}
