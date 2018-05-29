package qa.tecnositafgulf.config;

/**
 * Created by hadi on 1/2/18.
 */
public class NotificationTypes {
    //objects (recipient)
    public final static int LeaveRequestEmployeeNotification = 0;
    public final static int LeaveRequestLeaveManagerNotification = 1;
    public final static int LeaveRequestHRLeaveManagerNotification = 2;
    public final static int LeaveRequestFinanceLeaveManagerNotification = 3;
    public final static int LeaveRequestTicketLeaveManagerNotification = 4;
    public final static int LeaveRequestTicketLeaveManagerToFinanceNotification = 5;
    //messages :
    public final static String LeaveRequestStatusUpdate = "Your Leave Request Status has been updated";
    public final static String NewLeaveRequestFromEmployee = "Your Received a Leave Request To Approve";
    public final static String LeaveRequestCencelledByEmployee = "A Pending Leave Request To Approve has been Cancelled by the Applicant";
    public final static String LeaveRequestStatusApprovedWaitingTicketManager = "Your Leave Request Status has been approved by Finance, Waiting For Ticket Manager";
    public final static String LeaveRequestStatusApprovedWaitingFinance = "Your Leave Request Status has been approved: Waiting For Finance to Purchase Tickets";
    public final static String LeaveRequestStatusApproved = "Your Leave Request has been approved";
}
