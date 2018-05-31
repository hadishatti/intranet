package qa.tecnositafgulf.viewmodel.hr;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.sys.PageCtrl;
import qa.tecnositafgulf.model.leaves.LeaveRequest;
import qa.tecnositafgulf.service.LeaveRequestService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.util.concurrent.TimeUnit;

/**
 * Created by hadi on 5/22/18.
 */
public class ViewLeaveRequestDetailsViewModel extends IntranetVM{

    private LeaveRequest leaveRequest;
    private LeaveRequestService service;
    private int days;
    private int leaveBalance;
    private int newBalance;

    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("request") LeaveRequest request){
        init();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        service = context.getBean(LeaveRequestService.class);
        leaveRequest = request;
        leaveBalance = service.getLeaveBalance(leaveRequest.getApplicant(), leaveRequest.getType());
        days = (int) TimeUnit.DAYS.convert(leaveRequest.getLeaveTo().getTime()-leaveRequest.getLeaveFrom().getTime(),TimeUnit.MILLISECONDS)+1;
        newBalance = leaveBalance-days;
        addCommonTags((PageCtrl) view.getPage());
    }

    public LeaveRequest getLeaveRequest() {
        return leaveRequest;
    }

    public void setLeaveRequest(LeaveRequest leaveRequest) {
        this.leaveRequest = leaveRequest;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getLeaveBalance() {
        return leaveBalance;
    }

    public void setLeaveBalance(int leaveBalance) {
        this.leaveBalance = leaveBalance;
    }

    public int getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(int newBalance) {
        this.newBalance = newBalance;
    }
}
