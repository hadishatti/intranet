/*
package qa.tecnositafgulf.viewmodel.payroll;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;
import qa.tecnositafgulf.common.Month;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.payroll.PayRollDetailInfo;
import qa.tecnositafgulf.model.payroll.SalaryDetailInfo;
import qa.tecnositafgulf.service.AdministrationService;
import qa.tecnositafgulf.service.PayrollService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.util.GeneralUtil;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import javax.persistence.PersistenceException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateEmployeeSalaryViewModel extends IntranetVM{

    private SalaryDetailInfo salaryDetailInfo;
    private List<Employee> employeeList;
    private List<String> modeOfPaymentList;
    private List<Month> monthList;
    private List<String> yearList;
    private Month selectedMonth;
    private String selectedYear;
    private Employee selectedEmployee;
    private PayRollDetailInfo payRollDetailInfo;
    private PayrollService payrollService;
    private AdministrationService administrationService;


    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view,
                             @ExecutionArgParam("employeeSalaryToModify") SalaryDetailInfo salaryDetailInfo){
        init();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        administrationService = context.getBean(AdministrationService.class);
        payrollService = context.getBean(PayrollService.class);
        this.employeeList = administrationService.listEmployees();
        if(salaryDetailInfo == null){
            this.salaryDetailInfo = new SalaryDetailInfo();
        }
        modeOfPaymentList = new ArrayList<>();
        modeOfPaymentList.add("CASH");
        modeOfPaymentList.add("EFT");
        monthList = Month.getMonthList();
        yearList = GeneralUtil.getYearList();
    }


    @Command
    @NotifyChange("payRollDetailInfo")
    public void getEmployeePayrollDetails(){
        try{
        payRollDetailInfo = payrollService.getEmployeePayrollDetails(selectedEmployee);
        }catch (PersistenceException persistenceException){
            Messagebox.show("No payroll record found for selected Employee", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION);
        }

    }

    public List<String> getYearList() {
        return yearList;
    }

    public void setYearList(List<String> yearList) {
        this.yearList = yearList;
    }

    public List<String> getModeOfPaymentList() {
        return modeOfPaymentList;
    }

    public void setModeOfPaymentList(List<String> modeOfPaymentList) {
        this.modeOfPaymentList = modeOfPaymentList;
    }

    @Command
    public void saveEmpSalaryDetails(){
        salaryDetailInfo.setPayRollDetailInfo(payRollDetailInfo);
        salaryDetailInfo.setEmployeeID(payRollDetailInfo.getEmployee().getId());
        salaryDetailInfo.setCreatingDate(new Timestamp(new Date().getTime()));
        salaryDetailInfo.setMonthYearOfSalary(selectedMonth.name() + "/" + selectedYear);
        salaryDetailInfo.setTotalAmountPay(computeTotalAmountSalary());
        try {
            payrollService.saveEmployeeSalary(salaryDetailInfo);
            Messagebox.show("Record inserted successfully", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener<Event>() {
                public void onEvent(Event event) throws Exception {
                    if (event.getName().equals("onOK")) {
                        Executions.sendRedirect("/pages/payroll/createEmployeeSalary.zul");
                    }
                }
            });
        }catch (PersistenceException persistenceException){
            Messagebox.show("Salary for selected month already generated", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION);
        }
    }

    private Integer computeTotalAmountSalary(){
        return 1234;//put formula to calculate the salary.
    }

    public Month getSelectedMonth() {
        return selectedMonth;
    }

    public void setSelectedMonth(Month selectedMonth) {
        this.selectedMonth = selectedMonth;
    }

    public String getSelectedYear() {
        return selectedYear;
    }

    public void setSelectedYear(String selectedYear) {
        this.selectedYear = selectedYear;
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

    public SalaryDetailInfo getSalaryDetailInfo() {
        return salaryDetailInfo;
    }

    public void setSalaryDetailInfo(SalaryDetailInfo salaryDetailInfo) {
        this.salaryDetailInfo = salaryDetailInfo;
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
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

    public List<Month> getMonthList() {
        return monthList;
    }

    public void setMonthList(List<Month> monthList) {
        this.monthList = monthList;
    }
}
*/
