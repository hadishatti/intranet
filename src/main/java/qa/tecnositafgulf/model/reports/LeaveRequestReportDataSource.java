package qa.tecnositafgulf.model.reports;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import org.zkoss.zul.Messagebox;
import qa.tecnositafgulf.model.leaves.LeaveBalance;
import qa.tecnositafgulf.model.leaves.LeaveRequest;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by ledio on 5/29/18.
 */
public class LeaveRequestReportDataSource implements JRDataSource {
    private LeaveRequest leaveRequest;
    private LeaveBalance leaveBalance;

    public LeaveRequestReportDataSource(LeaveRequest leaveRequest, LeaveBalance leaveBalance){
        this.leaveRequest = leaveRequest;
        this.leaveBalance = leaveBalance;
    }

    @Override
    public boolean next() throws JRException {
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
        if ("daysHoliday".equals(fieldName))
            value = daysBetween(leaveRequest.getLeaveFrom(), leaveRequest.getLeaveTo());
        if ("leaveFrom".equals(fieldName))
            value = leaveRequest.getLeaveFrom().toString();
        if ("leaveTo".equals(fieldName))
            value = leaveRequest.getLeaveTo().toString();
        if ("addressOnHoliday".equals(fieldName))
            value = leaveRequest.getAddressOnHoliday();
        if ("phoneNumber".equals(fieldName))
            value = leaveRequest.getPhoneNumber();
        if ("approvedOn".equals(fieldName))
            value = leaveRequest.getApprovedOn().toString();
        if ("employeeOnBehalfName".equals(fieldName))
            value = leaveRequest.getEmployeeOnBehalf().getName();
        if ("employeeOnBehalfRole".equals(fieldName))
            value = leaveRequest.getEmployeeOnBehalf().getRole().getName();
        if ("employeeLeaveManager".equals(fieldName))
            value = leaveRequest.getApplicant().getLeaveManagerEmployee().getName();
        if ("employeeLeaveBalance".equals(fieldName))
            value = leaveBalance.getValue();
        if ("refusal".equals(fieldName))
            value = leaveRequest.getReason();

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

