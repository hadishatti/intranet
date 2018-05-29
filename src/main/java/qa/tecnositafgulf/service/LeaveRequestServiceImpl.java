package qa.tecnositafgulf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qa.tecnositafgulf.config.LeaveRequestStates;
import qa.tecnositafgulf.config.NotificationTypes;
import qa.tecnositafgulf.dao.common.NotificationDao;
import qa.tecnositafgulf.dao.hr.LeaveBalanceDao;
import qa.tecnositafgulf.dao.hr.LeaveRequestDao;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.common.Notification;
import qa.tecnositafgulf.model.leaves.LeaveBalance;
import qa.tecnositafgulf.model.leaves.LeaveRequest;
import qa.tecnositafgulf.searchcriteria.hr.LeaveRequestSearchCriteria;

import java.util.Date;
import java.util.List;

import static qa.tecnositafgulf.config.LeaveRequestStates.*;

/**
 * Created by hadi on 12/31/17.
 */
@Service
public class LeaveRequestServiceImpl implements LeaveRequestService {

    @Autowired
    LeaveRequestDao dao;

    @Autowired
    LeaveBalanceDao balanceDao;

    @Autowired
    NotificationDao notificationDao;

    @Transactional
    public void cancelLeaveRequest(LeaveRequest requestToBeCancelled){
        int previous = requestToBeCancelled.getStatus();
        LeaveRequest request = new LeaveRequest();
        request.setApplicant(requestToBeCancelled.getApplicant());
        request.setApprover(requestToBeCancelled.getApplicant());
        request.setCreatedOn(requestToBeCancelled.getCreatedOn());
        request.setType(requestToBeCancelled.getType());
        request.setEmployeeOnBehalf(request.getEmployeeOnBehalf());
        request.setAddressOnHoliday(requestToBeCancelled.getAddressOnHoliday());
        request.setLeaveFrom(requestToBeCancelled.getLeaveFrom());
        request.setLeaveTo(requestToBeCancelled.getLeaveTo());
        request.setManager(requestToBeCancelled.getManager());
        request.setMessage(requestToBeCancelled.getMessage());
        request.setNumber(requestToBeCancelled.getNumber());
        request.setPhoneNumber(requestToBeCancelled.getPhoneNumber());
        request.setTickets(requestToBeCancelled.isTickets());
        request.setStatus(LeaveRequestStates.CancelledByApplicant);
        request.setApprovedOn(new Date());
        dao.save(request);
        notificationDao.deleteByAttachment(request.getNumber());
        Notification not = new Notification();
        switch (previous){
            case New:
                not.setType(NotificationTypes.LeaveRequestLeaveManagerNotification);
                break;
            case WaitingForHR:
                not.setType(NotificationTypes.LeaveRequestHRLeaveManagerNotification);
                break;
            case WaitingForFinance:
                not.setType(NotificationTypes.LeaveRequestFinanceLeaveManagerNotification);
                break;
            case WaitingForTickets:
                not.setType(NotificationTypes.LeaveRequestTicketLeaveManagerNotification);
                break;
            case TicketsToBePurchased:
                not.setType(NotificationTypes.LeaveRequestTicketLeaveManagerToFinanceNotification);
                break;

            default:
                break;
        }
        not.setObject(NotificationTypes.LeaveRequestCencelledByEmployee);
        not.setReceivedOn(new Date());
        not.setRecipient(requestToBeCancelled.getApprover());
        not.setAttachment(requestToBeCancelled.getNumber());
        not.setRequestCancelled(true);
        notificationDao.save(not);
    }

    @Transactional
    public void deleteLeaveRequestEmployeeNotification(LeaveRequest request, Employee employee) {
        notificationDao.deleteByAttachmentAndRecipientForEmployee(request.getNumber(), employee);
    }

    @Transactional
    public void deleteLeaveRequestManagerNotification(LeaveRequest request, Employee employee) {
        notificationDao.deleteByAttachmentAndRecipientForManager(request.getNumber(), employee);
    }

    @Transactional
    public void deleteLeaveRequestNotification(LeaveRequest request){
        notificationDao.deleteByAttachment(request.getNumber());
    }

    @Transactional
    public void saveLeaveRequest(LeaveRequest request) {
        dao.save(request);
        switch (request.getStatus()){
            case New:
                notifyRequestToManager(request);
                break;
            case RefusedByFinance:
            case RefusedByHR:
            case RefusedByManagement:
            case RefusedByFinanceAfterTicketSelection:
            case Approved:
                notifyEmployeeForUpdate(request);
                break;
            case WaitingForHR:
                notifyRequestToHR(request);
                notifyEmployeeForUpdate(request);
                break;
            case WaitingForFinance:
                notifyRequestToFinance(request);
                notifyEmployeeForUpdate(request);
                break;
            case WaitingForTickets:
                notifyRequestForTicketManager(request);
                notifyEmployeeForUpdate(request);
                break;
            case TicketsToBePurchased:
                notifyRequestFromTicketManagerForFinance(request);
                notifyEmployeeForUpdate(request);
                break;
            default:
                break;
        }
    }

    @Transactional
    public void deleteSickLeave(LeaveRequest request) {
        dao.remove(request);
    }

    @Transactional
    public void deleteEmergencyLeave(LeaveRequest request) {
        dao.remove(request);
    }

    @Transactional
    public List<LeaveRequest> listApprovedLeaveRequests() {
        return dao.listApproved();
    }

    @Transactional
    public LeaveRequest findByNumberAndStatus(String number, int status) {
        return dao.findByNumberAndStatus(number, status);
    }

    @Transactional
    public List<LeaveRequest> findByNumber(String number) {
        return dao.findByNumber(number);
    }

    @Transactional
    public void updateLeaveBalance(LeaveBalance balance) {
        balanceDao.save(balance);
    }

    @Transactional
    public int getLeaveBalance(Employee employee, String type) {
        return balanceDao.getBalance(employee, type);
    }

    @Transactional
    public List<LeaveRequest> listSickLeaves() {
        return dao.listSickLeaves();
    }

    @Transactional
    public List<LeaveRequest> listEmergencyLeaves() {
        return dao.listEmergencyLeaves();
    }

    @Transactional
    public List<LeaveRequest> listLastLeaveRequestsByApplicant(Employee applicant) {
        return dao.listLastByApplicant(applicant);
    }

    @Transactional
    public List<LeaveRequest> listLastLeaveRequestsByApplicant(Employee applicant, Date leaveFrom, Date leaveTo) {
        return dao.listLastByApplicant(applicant, leaveFrom, leaveTo);
    }

    @Transactional
    public List<LeaveRequest> listLastLeaveRequestsByApplicant(LeaveRequestSearchCriteria searchCriteria) {
        return dao.listLastByApplicant(searchCriteria);
    }

    @Transactional
    public Integer listLastLeaveRequestsByApplicantCount(LeaveRequestSearchCriteria searchCriteria) {
        return dao.listLastByApplicantCount(searchCriteria);
    }

    @Transactional
    public List<LeaveRequest> listLastLeaveRequestsByApplicants(LeaveRequestSearchCriteria searchCriteria) {
        return dao.listLastByApplicants(searchCriteria);
    }

    @Transactional
    public Integer listLastLeaveRequestsByApplicantsCount(LeaveRequestSearchCriteria searchCriteria) {
        return dao.listLastByApplicantsCount(searchCriteria);
    }

    @Transactional
    public List<LeaveRequest> listLastLeaveRequestsByApprover(LeaveRequestSearchCriteria searchCriteria) {
        return dao.listLastByApprover(searchCriteria);
    }

    @Transactional
    public Integer listLastLeaveRequestsByApproverCount(LeaveRequestSearchCriteria searchCriteria) {
        return dao.listLastByApproverCount(searchCriteria);
    }

    @Transactional
    public List<LeaveRequest> listSickLeavesByApprover(LeaveRequestSearchCriteria sc) {
        return dao.listSickLeavesByApprover(sc);
    }

    @Transactional
    public Integer listSickLeavesByApproverCount(LeaveRequestSearchCriteria sc) {
        return dao.listSickLeavesByApproverCount(sc);
    }

    @Transactional
    public List<LeaveRequest> listEmergencyLeavesByApprover(LeaveRequestSearchCriteria sc) {
        return dao.listEmergencyLeavesByApprover(sc);
    }

    @Transactional
    public Integer listEmergencyLeavesByApproverCount(LeaveRequestSearchCriteria sc) {
        return dao.listEmergencyLeavesByApproverCount(sc);
    }

    @Transactional
    public List<LeaveRequest> listLastLeaveRequestsByApprover(Employee approver) {
        return dao.listLastByApprover(approver);
    }

    @Transactional
    public List<LeaveRequest> listLastLeaveRequestsToApprove(Employee approver) {
        return dao.listLastToApproveByApprover(approver);
    }

    @Transactional
    public int countPendingRequestsByApplicantAndDates(Employee applicant, Date leaveFrom, Date leaveTo) {
        return dao.countPendingByApplicantAndDates(applicant, leaveFrom, leaveTo);
    }

    @Transactional
    public void notifyEmployeeForUpdate(LeaveRequest request) {
        Notification not = new Notification();
        not.setType(NotificationTypes.LeaveRequestEmployeeNotification);
        if(request.getStatus()==Approved)
            not.setObject(NotificationTypes.LeaveRequestStatusApproved);
        else
            not.setObject(NotificationTypes.LeaveRequestStatusUpdate);
        not.setReceivedOn(new Date());
        not.setRecipient(request.getApplicant());
        not.setSender(null);
        not.setAttachment(request.getNumber());
        not.setForEmployee(true);
        not.setRequestCancelled(false);
        notificationDao.save(not);
    }

    @Transactional
    public void notifyRequestToManager(LeaveRequest request) {
        Notification not = new Notification();
        not.setType(NotificationTypes.LeaveRequestLeaveManagerNotification);
        not.setObject(NotificationTypes.NewLeaveRequestFromEmployee);
        not.setReceivedOn(new Date());
        not.setRecipient(request.getApprover());
        not.setSender(request.getApplicant());
        not.setAttachment(request.getNumber());
        not.setForEmployee(false);
        not.setRequestCancelled(false);
        notificationDao.save(not);
    }

    @Transactional
    public void notifyRequestToHR(LeaveRequest request) {
        Notification not = new Notification();
        not.setType(NotificationTypes.LeaveRequestHRLeaveManagerNotification);
        not.setObject(NotificationTypes.NewLeaveRequestFromEmployee);
        not.setReceivedOn(new Date());
        not.setRecipient(request.getApprover());
        not.setSender(request.getApplicant().getLeaveManagerEmployee());
        not.setAttachment(request.getNumber());
        not.setForEmployee(false);
        not.setRequestCancelled(false);
        notificationDao.save(not);
    }

    @Transactional
    public void notifyRequestToFinance(LeaveRequest request) {
        Notification not = new Notification();
        not.setType(NotificationTypes.LeaveRequestFinanceLeaveManagerNotification);
        not.setObject(NotificationTypes.NewLeaveRequestFromEmployee);
        not.setReceivedOn(new Date());
        not.setRecipient(request.getApprover());
        not.setSender(request.getApplicant().getHRLeaveManagerEmployee());
        not.setAttachment(request.getNumber());
        not.setForEmployee(false);
        not.setRequestCancelled(false);
        notificationDao.save(not);
    }

    @Transactional
    public void notifyRequestForTicketManager(LeaveRequest request) {
        Notification not = new Notification();
        not.setType(NotificationTypes.LeaveRequestTicketLeaveManagerNotification);
        not.setObject(NotificationTypes.LeaveRequestStatusApprovedWaitingTicketManager);
        not.setReceivedOn(new Date());
        not.setRecipient(request.getApprover());
        not.setSender(request.getApplicant().getFinanceLeaveManagerEmployee());
        not.setAttachment(request.getNumber());
        not.setForEmployee(false);
        not.setRequestCancelled(false);
        notificationDao.save(not);
    }

    @Transactional
    public void notifyRequestFromTicketManagerForFinance(LeaveRequest request) {
        Notification not = new Notification();
        not.setType(NotificationTypes.LeaveRequestTicketLeaveManagerToFinanceNotification);
        not.setObject(NotificationTypes.LeaveRequestStatusApprovedWaitingFinance);
        not.setReceivedOn(new Date());
        not.setRecipient(request.getApprover());
        not.setSender(request.getApplicant().getTicketLeaveManagerEmployee());
        not.setAttachment(request.getNumber());
        not.setForEmployee(false);
        not.setRequestCancelled(false);
        notificationDao.save(not);
    }
}
