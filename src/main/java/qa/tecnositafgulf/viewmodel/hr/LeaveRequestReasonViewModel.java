package qa.tecnositafgulf.viewmodel.hr;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.sys.PageCtrl;
import qa.tecnositafgulf.config.LeaveRequestStates;
import qa.tecnositafgulf.model.leaves.LeaveBalance;
import qa.tecnositafgulf.model.leaves.LeaveRequest;
import qa.tecnositafgulf.service.LeaveRequestService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.util.concurrent.TimeUnit;

/**
 * Created by hadi on 5/25/18.
 */
public class LeaveRequestReasonViewModel extends IntranetVM{

    private String title;
    private String message;
    private String reason1;
    private String reason2;
    private String reason3;
    private String reason4;
    private String reason5;
    private String reason;
    private String constraint;
    private boolean viewTextbox;
    private LeaveRequest leaveRequest;
    private boolean approved;
    private LeaveRequestService service;
    private LeaveBalance leaveBalance;


    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("approved") boolean approved, @ExecutionArgParam("request") LeaveRequest request) {
        init();
        this.approved = approved;
        leaveRequest = request;
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        service = context.getBean(LeaveRequestService.class);
        int days=0;
        if(!approved) {
            if (leaveRequest.getStatus() == LeaveRequestStates.RefusedByManagement) {
                reason1 = "An other employee is in leave in the same requested period.";
                reason2 = "Employee needed in a task to be performed in the same period.";
                reason3 = "Non reasonable period of notice for the leave request.";
                reason4 = "Non reasonable period of the requested leave.";
                reason5 = "Other";
            } else if (leaveRequest.getStatus() == LeaveRequestStates.RefusedByHR) {
                reason1 = "Leave request not compliant to the Leave Policy.";
                reason2 = "Employee leave balance is negative.";
                reason3 = "Non reasonable period of notice for the leave request.";
                reason4 = "Non reasonable period of the requested leave.";
                reason5 = "Other";
            } else if (leaveRequest.getStatus() == LeaveRequestStates.RefusedByFinance) {
                reason1 = "Annual Budget does not allow the leave.";
                reason2 = "Non reasonable period of notice for the leave request.";
                reason3 = "Non reasonable period of the requested leave.";
                reason5 = "Other";
            } else if (leaveRequest.getStatus() == LeaveRequestStates.RefusedByFinanceAfterTicketSelection) {
                reason1 = "Cost of the tickets is not compliant to the allocated Ticket Budget.";
                reason2 = "Non reasonable period of notice for the leave request.";
                reason3 = "Non reasonable period of the requested leave.";
                reason5 = "Other";
            }
            viewTextbox=false;
            constraint ="";
            title="Leave Request Refused";
            message="Please specify one of the following reasons:";
        }else{
            reason1="";
            reason2="";
            reason3="";
            reason4="";
            if(leaveRequest.getStatus()!=LeaveRequestStates.Approved)
                viewTextbox=true;
            else {
                viewTextbox = false;
                leaveBalance = new LeaveBalance();
                leaveBalance.setEmployee(leaveRequest.getApplicant());
                leaveBalance.setType(leaveRequest.getType());
                long daysInMillis = leaveRequest.getLeaveTo().getTime() - leaveRequest.getLeaveFrom().getTime();
                days = (int) TimeUnit.DAYS.convert(daysInMillis, TimeUnit.MILLISECONDS)+1;
                days = service.getLeaveBalance(leaveRequest.getApplicant(), leaveRequest.getType())-days;
                leaveBalance.setValue(days);
            }
            constraint ="";
            title="Leave Request Approved";
            if (leaveRequest.getStatus() == LeaveRequestStates.WaitingForHR)
                message="Send a message to the Human Resource Manager "+leaveRequest.getApprover().getName()+" "+leaveRequest.getApprover().getFamilyName()+" (Optional):";
            else if (leaveRequest.getStatus() == LeaveRequestStates.WaitingForFinance)
                message="Send a message to the Finance Manager "+leaveRequest.getApprover().getName()+" "+leaveRequest.getApprover().getFamilyName()+" (Optional):";
            else if (leaveRequest.getStatus() == LeaveRequestStates.WaitingForTickets)
                message="Send a message to the Ticket Manager "+leaveRequest.getApprover().getName()+" "+leaveRequest.getApprover().getFamilyName()+" (Optional):";
            else if (leaveRequest.getStatus() == LeaveRequestStates.TicketsToBePurchased)
                message="Send a message to the Finance Manager "+leaveRequest.getApprover().getName()+" "+leaveRequest.getApprover().getFamilyName()+" (Optional):";
            else if (leaveRequest.getStatus() == LeaveRequestStates.Approved)
                message="Submit to approve the Leave Request. New Employee's "+leaveRequest.getType()+" Leave Balance will be "+days+".";
        }
        reason="";
        addCommonTags((PageCtrl) view.getPage());
    }

    @Command
    @NotifyChange({"constraint","viewTextbox","reason"})
    public void option1Selected(){
        constraint ="";
        viewTextbox=false;
        reason = reason1;
    }

    @Command
    @NotifyChange({"constraint","viewTextbox","reason"})
    public void option2Selected(){
        constraint ="";
        viewTextbox=false;
        reason = reason2;
    }

    @Command
    @NotifyChange({"constraint","viewTextbox","reason"})
    public void option3Selected(){
        constraint ="";
        viewTextbox=false;
        reason = reason3;
    }

    @Command
    @NotifyChange({"constraint","viewTextbox","reason"})
    public void option4Selected(){
        constraint ="";
        viewTextbox=false;
        reason = reason4;
    }

    @Command
    @NotifyChange({"constraint","viewTextbox","reason"})
    public void option5Selected(){
        constraint ="no empty";
        viewTextbox=true;
        reason="";
    }

    @Command
    public void submit(){
        if(!approved)
            leaveRequest.setReason(reason.isEmpty()?"Not Specified":reason);
        else {
            leaveRequest.setMessage(reason.isEmpty() ? "Not Specified" : reason);
            if(leaveRequest.getStatus()==LeaveRequestStates.Approved)
                service.updateLeaveBalance(leaveBalance);
        }
        service.saveLeaveRequest(leaveRequest);
        Executions.sendRedirect("/pages/hr/viewLeaveRequestsToManage.zul");
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReason1() {
        return reason1;
    }

    public void setReason1(String reason1) {
        this.reason1 = reason1;
    }

    public String getReason2() {
        return reason2;
    }

    public void setReason2(String reason2) {
        this.reason2 = reason2;
    }

    public String getReason3() {
        return reason3;
    }

    public void setReason3(String reason3) {
        this.reason3 = reason3;
    }

    public String getReason4() {
        return reason4;
    }

    public void setReason4(String reason4) {
        this.reason4 = reason4;
    }

    public String getReason5() {
        return reason5;
    }

    public void setReason5(String reason5) {
        this.reason5 = reason5;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getConstraint() {
        return constraint;
    }

    public void setConstraint(String constraint) {
        this.constraint = constraint;
    }

    public boolean isViewTextbox() {
        return viewTextbox;
    }

    public void setViewTextbox(boolean viewTextbox) {
        this.viewTextbox = viewTextbox;
    }

    public LeaveRequest getLeaveRequest() {
        return leaveRequest;
    }

    public void setLeaveRequest(LeaveRequest leaveRequest) {
        this.leaveRequest = leaveRequest;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
