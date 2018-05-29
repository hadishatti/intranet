package qa.tecnositafgulf.service;

import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.leaves.LeaveBalance;
import qa.tecnositafgulf.model.leaves.LeaveRequest;
import qa.tecnositafgulf.searchcriteria.hr.LeaveRequestSearchCriteria;

import java.util.Date;
import java.util.List;

/**
 * Created by hadi on 12/30/17.
 */
public interface LeaveRequestService {
    public void saveLeaveRequest(LeaveRequest request);
    public void deleteSickLeave(LeaveRequest request);
    public void deleteEmergencyLeave(LeaveRequest request);
    public  List<LeaveRequest> listApprovedLeaveRequests();
    public  List<LeaveRequest> listSickLeaves();
    public  List<LeaveRequest> listEmergencyLeaves();
    List<LeaveRequest> listSickLeavesByApprover(LeaveRequestSearchCriteria sc);
    Integer listSickLeavesByApproverCount(LeaveRequestSearchCriteria sc);
    List<LeaveRequest> listEmergencyLeavesByApprover(LeaveRequestSearchCriteria sc);
    Integer listEmergencyLeavesByApproverCount(LeaveRequestSearchCriteria sc);
    public  List<LeaveRequest> listLastLeaveRequestsByApplicant(Employee applicant);
    public List<LeaveRequest> listLastLeaveRequestsByApplicant(Employee applicant, Date leaveFrom, Date leaveTo);
    public  List<LeaveRequest> listLastLeaveRequestsByApplicant(LeaveRequestSearchCriteria sc);
    public  List<LeaveRequest> listLastLeaveRequestsByApplicants(LeaveRequestSearchCriteria sc);
    public  Integer listLastLeaveRequestsByApplicantCount(LeaveRequestSearchCriteria sc);
    public  Integer listLastLeaveRequestsByApplicantsCount(LeaveRequestSearchCriteria sc);
    public List<LeaveRequest> listLastLeaveRequestsByApprover(Employee approver);
    public List<LeaveRequest> listLastLeaveRequestsByApprover(LeaveRequestSearchCriteria sc);
    public Integer listLastLeaveRequestsByApproverCount(LeaveRequestSearchCriteria sc);
    public int countPendingRequestsByApplicantAndDates(Employee applicant, Date leaveFrom, Date leaveTo);
    public List<LeaveRequest> findByNumber(String number);
    public LeaveRequest findByNumberAndStatus(String number, int status);

    public void updateLeaveBalance(LeaveBalance balance);
    public int getLeaveBalance(Employee employee, String type);

    public void notifyEmployeeForUpdate(LeaveRequest request);
    public void notifyRequestToManager(LeaveRequest request);
    public void notifyRequestToHR(LeaveRequest request);
    public void notifyRequestToFinance(LeaveRequest request);
    public void notifyRequestForTicketManager(LeaveRequest request);
    public void notifyRequestFromTicketManagerForFinance(LeaveRequest request);
    public void cancelLeaveRequest(LeaveRequest request);
    public void deleteLeaveRequestEmployeeNotification(LeaveRequest request, Employee employee);
    public void deleteLeaveRequestManagerNotification(LeaveRequest request, Employee employee);
}
