package qa.tecnositafgulf.model.reports;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import org.zkoss.zul.Messagebox;
import qa.tecnositafgulf.config.LeaveRequestStates;
import qa.tecnositafgulf.model.leaves.LeaveRequest;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by ledio on 5/29/18.
 */
public class LeaveRequestReportDataSource implements JRDataSource {
    private LeaveRequest leaveRequest;
    private Integer leaveBalance;
    private int index = -1;

    public LeaveRequestReportDataSource(LeaveRequest leaveRequest, Integer leaveBalance){
        this.leaveRequest = leaveRequest;
        this.leaveBalance = leaveBalance;
    }

    @Override
    public boolean next() throws JRException {
        index++;
        if (index < 1){
            return true;
        }
        return false;
    }

    @Override
    public Object getFieldValue(JRField jrField) throws JRException {
        Object value = null;
        String fieldName = jrField.getName();

        if ("number".equals(fieldName))
            value = leaveRequest.getNumber();
        if ("employeeName".equals(fieldName))
            value = leaveRequest.getApplicant().getName();
        if ("employeeNumber".equals(fieldName))
            value = leaveRequest.getApplicant().getEmployeeNumber();
        if ("employeeDepartment".equals(fieldName))
            value = leaveRequest.getApplicant().getDepartment().getName();
        if ("employeeRole".equals(fieldName))
            value = leaveRequest.getApplicant().getRole().getName();
        if ("holidayType".equals(fieldName))
            value = leaveRequest.getType();
        if ("daysHoliday".equals(fieldName))
            value = daysBetween(leaveRequest.getLeaveFrom(), leaveRequest.getLeaveTo());
        if ("leaveFrom".equals(fieldName))
            value = leaveRequest.getLeaveFrom().toString().substring(0, 16);
        if ("leaveTo".equals(fieldName))
            value = leaveRequest.getLeaveTo().toString().substring(0, 16);
        if ("addressOnHoliday".equals(fieldName))
            value = leaveRequest.getAddressOnHoliday() == null? "Not Specified": leaveRequest.getAddressOnHoliday();
        if ("phoneNumber".equals(fieldName))
            value = leaveRequest.getPhoneNumber() == null? "Not Specified": leaveRequest.getPhoneNumber();
        if ("approvedOn".equals(fieldName))
            value = leaveRequest.getApprovedOn().toString().substring(0, 16);
        if ("employeeOnBehalfName".equals(fieldName))
            value = leaveRequest.getEmployeeOnBehalf() == null? "Not Specified": leaveRequest.getEmployeeOnBehalf().getName();
        if ("employeeOnBehalfRole".equals(fieldName))
            value = leaveRequest.getEmployeeOnBehalf() == null? "Not Specified": leaveRequest.getEmployeeOnBehalf().getRole().getName();
        if ("employeeLeaveManager".equals(fieldName))
            value = leaveRequest.getApplicant().getLeaveManagerEmployee().getName();
        if ("employeeLeaveBalance".equals(fieldName))
            value = leaveBalance;
        if ("casualLeaveReason".equals(fieldName))
            value = "TODO"; //TODO add reason for casual leave
        if ("refusal".equals(fieldName))
            value = leaveRequest.getReason() == null? "Not specified":leaveRequest.getReason();
        if ("status".equals(fieldName)) {
            if (leaveRequest.getStatus() == LeaveRequestStates.Approved)
                value = "Approved";
            if (leaveRequest.getStatus() == LeaveRequestStates.RefusedByFinance)
                value = "Refused by Finance";
            if (leaveRequest.getStatus() == LeaveRequestStates.RefusedByFinanceAfterTicketSelection)
                value = "Refused by Finance after Ticket selection";
            if (leaveRequest.getStatus() == LeaveRequestStates.RefusedByHR)
                value = "Refused by HR";
            if (leaveRequest.getStatus() == LeaveRequestStates.RefusedByManagement)
                value = "Refused by Management";
            if (leaveRequest.getStatus() == LeaveRequestStates.EmergencyRegistered)
                value = "Approved";
            if (leaveRequest.getStatus() == LeaveRequestStates.SickRegistered)
                value = "Approved";
        }

        return value;
    }

    private Long daysBetween(Date from, Date to) {
        Long diff = -1L;
        try {
            Long diffInMillies = Math.abs(to.getTime() - from.getTime());
            diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        }catch (Exception p){
            Messagebox.show("Parse Error!", "Error", Messagebox.OK, Messagebox.EXCLAMATION);
        }

        return diff;
    }

}

