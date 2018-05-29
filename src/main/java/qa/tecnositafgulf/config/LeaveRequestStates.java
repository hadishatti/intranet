package qa.tecnositafgulf.config;

/**
 * Created by hadi on 1/2/18.
 */
public class LeaveRequestStates {
    public static final int New=1;
    public static final int CancelledByApplicant=13;
    public static final int RefusedByManagement=2;

    public static final int WaitingForHR=3;
    public static final int RefusedByHR=4;

    public static final int WaitingForFinance=5;
    public static final int RefusedByFinance=6;

    public static final int WaitingForTickets=7;
    public static final int TicketsToBePurchased=8;
    public static final int RefusedByFinanceAfterTicketSelection=9;

    public static final int Approved=10;

    public static final int SickRegistered=11;
    public static final int EmergencyRegistered=12;

    public static String toString(int type){
        String ret = "";
        switch (type){
            case New:
                ret = "New, Waiting for Approval from Management";
                break;
            case CancelledByApplicant:
                ret = "Request Cancelled by Applicant";
                break;
            case RefusedByManagement:
                ret = "Request Refused by Management";
                break;
            case WaitingForHR:
                ret = "Approved by Management, Waiting for Approval From HR";
                break;
            case RefusedByHR:
                ret = "Request Refused by HR";
                break;
            case WaitingForFinance:
                ret = "Request Approved by HR, Waiting for Approval From Finance";
                break;
            case RefusedByFinance:
                ret = "Request Refused by Finance";
                break;
            case WaitingForTickets:
                ret = "Approved by Finance, Request Sent to Ticket Manager";
                break;
            case TicketsToBePurchased:
                ret = "Tickets Selected, Waiting for Finance for Purchasing";
                break;
            case RefusedByFinanceAfterTicketSelection:
                ret = "Request Refused by Finance After Selection of Tickets";
                break;
            case Approved:
                ret = "Approved, Leave Granted";
                break;
            case SickRegistered:
                ret = "Sick Leave Registered";
                break;
            case EmergencyRegistered:
                ret = "Emergency Leave Registered";
                break;
        }
        return ret;
    }

    public static int toInt(String type) {
        switch (type) {
            case "New, Waiting for Approval from Management":
                return 1;
            case "Request Cancelled by Applicant":
                return 13;
            case "Request Refused by Management":
                return 2;
            case "Approved by Management, Waiting for Approval From HR":
                return 3;
            case "Request Refused by HR":
                return 4;
            case "Request Approved by HR, Waiting for Approval From Finance":
                return 5;
            case "Request Refused by Finance":
                return 6;
            case "Approved by Finance, Request Sent to Ticket Manager":
                return 7;
            case "Tickets Selected, Waiting for Finance for Purchasing":
                return 8;
            case "Request Refused by Finance After Selection of Tickets":
                return 9;
            case "Approved, Leave Granted":
                return 10;
            case "Sick Leave Registered":
                return 11;
            case "Emergency Leave Registered":
                return 12;
            default:
                return -1;
        }
    }
}
