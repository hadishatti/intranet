/*
package qa.tecnositafgulf.viewmodel.payroll;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.payroll.PayRollDetailInfo;
import qa.tecnositafgulf.service.AdministrationService;
import qa.tecnositafgulf.service.PayrollService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import javax.persistence.PersistenceException;
import java.util.List;

public class CreateEmpPayInfoViewModel extends IntranetVM {
    private List<Employee> employeeList;
    private Employee selectedEmployee;
    private PayRollDetailInfo payRollDetailInfo;
    private PayrollService payrollService;
    private AdministrationService administrationService;


    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view,
                             @ExecutionArgParam("employeePayrollToModify") PayRollDetailInfo payRollDetailInfoToModify) {
        init();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        administrationService = context.getBean(AdministrationService.class);
        payrollService = context.getBean(PayrollService.class);
        this.employeeList = administrationService.listEmployees();
        if (payRollDetailInfoToModify == null) {
            payRollDetailInfo = new PayRollDetailInfo();
        }


    }

    @Command
    public void savePayrollDetails() {
        payRollDetailInfo.setEmployee(selectedEmployee);
        payrollService.saveEmployeePayroll(payRollDetailInfo);
        Messagebox.show("Payroll details updated successfully", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener<Event>() {
            public void onEvent(Event event) throws Exception {
                if (event.getName().equals("onOK")) {
                    Executions.sendRedirect("/pages/payroll/createEmployeePayDetails.zul");
                }
            }
        });
    }

    @Command
    @NotifyChange("payRollDetailInfo")
    public void getPayrollDetails() {
        try {
            PayRollDetailInfo payRollDetailInfo = payrollService.getEmployeePayrollDetails(selectedEmployee);
            if (payRollDetailInfo != null)
                this.payRollDetailInfo = payRollDetailInfo;
        } catch (PersistenceException persistenceException) {
            this.payRollDetailInfo = new PayRollDetailInfo();
        }
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    public Employee getSelectedEmployee() {
        return selectedEmployee;
    }

    public void setSelectedEmployee(Employee selectedEmployee) {
        this.selectedEmployee = selectedEmployee;
    }

    public PayRollDetailInfo getPayRollDetailInfo() {
        return payRollDetailInfo;
    }

    public void setPayRollDetailInfo(PayRollDetailInfo payRollDetailInfo) {
        this.payRollDetailInfo = payRollDetailInfo;
    }

    public PayrollService getPayrollService() {
        return payrollService;
    }

    public void setPayrollService(PayrollService payrollService) {
        this.payrollService = payrollService;
    }

    public AdministrationService getAdministrationService() {
        return administrationService;
    }

    public void setAdministrationService(AdministrationService administrationService) {
        this.administrationService = administrationService;
    }
}
*/
