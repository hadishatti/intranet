/*
package qa.tecnositafgulf.viewmodel.payroll;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Filedownload;
import qa.tecnositafgulf.common.Month;
import qa.tecnositafgulf.model.payroll.PayRollDetailInfo;
import qa.tecnositafgulf.model.payroll.SalaryDetails;
import qa.tecnositafgulf.model.payroll.SalaryReport;
import qa.tecnositafgulf.service.PayrollService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalarySlipViewModel extends IntranetVM {

    private String monthAndYear;
    private List<Month> monthList;
    private List<String> yearList;
    private Month selectedMonth;
    private String selectedYear;
    private PayrollService payrollService;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        init();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        payrollService = context.getBean(PayrollService.class);
        monthList = Month.getMonthList();
        yearList = GeneralUtil.getYearList();
    }

    @Command
    public void generateSalarySlip() throws JRException {
        monthAndYear = selectedMonth + "/" + selectedYear;
        SalaryReport salaryReport = payrollService.getSalaryDetailsInfo(getEmployee(), monthAndYear);
        PayRollDetailInfo payRollDetailInfo = payrollService.getEmployeePayrollDetails(getEmployee());
        List<SalaryDetails> salaryReports = null;


        if(salaryReport !=null){
            salaryReports = new ArrayList<>();
            SalaryDetails salaryDetails = new SalaryDetails();
            salaryDetails.setDepartment(getEmployee().getDepartment().getName());
            salaryDetails.setDesignation(getEmployee().getRole().getName());
            salaryDetails.setEmployee(getEmployee().getName() + "-" + getEmployee().getEmployeeNumber());
            salaryDetails.setMonthAndYear(salaryReport.getMonthOfSalary());
            salaryDetails.setOtherAllowance(String.valueOf(payRollDetailInfo.getOtherAllowance()));
            salaryDetails.setOtherDeductions("0");
            salaryDetails.setTxtBasicPay(String.valueOf(payRollDetailInfo.getBasicPay()));
            salaryDetails.setTxtHouseRentAllowance(String.valueOf(payRollDetailInfo.getHouseRentAllowance()));
            salaryDetails.setTxtTransportAllowance(String.valueOf(payRollDetailInfo.getTransportAllowance()));
            salaryDetails.setLblBasicPay("Basic Salary");
            salaryDetails.setLblHouseRentAllowance("Housing Allowance");
            salaryDetails.setLblTransportAllowance("Transport Allowance");
            salaryDetails.setLblOtherAllowance("Other Allowance");
            salaryDetails.setTotal("Total");
            salaryDetails.setTxtTotalAmount(String.valueOf(payRollDetailInfo.getBasicPay() + payRollDetailInfo.getHouseRentAllowance() +
                    payRollDetailInfo.getTransportAllowance() + payRollDetailInfo.getOtherAllowance()));
            salaryReports.add(salaryDetails);

            InputStream inputStream = SalarySlipViewModel.class.getClassLoader().getResourceAsStream("SampleReport.jrxml");
            JasperReport jreport = null;
            if(inputStream !=null){
                jreport = JasperCompileManager.compileReport(inputStream);
            }
            JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(salaryReports);
            JRBeanCollectionDataSource ds2 = new JRBeanCollectionDataSource(salaryReports);
            Map params = new HashMap();
            params.put("datasource", ds);
            params.put("datasource2", ds2);
            JasperPrint jprint = JasperFillManager.fillReport(jreport,
                    params, new JREmptyDataSource());

            //JasperExportManager.exportReportToPdfFile(jprint,
            //        "src/main/resources/table.pdf");
            //JasperPrint jasperPrint = JasperFillManager.fillReport(SalarySlipViewModel.class.getResourceAsStream("/salarySlip.jasper"), new HashMap<String, Object>(), new JREmptyDataSource());

            byte[] ba = JasperExportManager.exportReportToPdf(jprint);
            Filedownload.save(ba, "application/x-download", "SalaryReceipt.pdf");

            //JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(salaryReports);
            //JasperPrint jasperPrint = JasperFillManager.fillReport(SalarySlipViewModel.class.getResourceAsStream("/salarySlip.jasper"), new HashMap<String, Object>(), beanColDataSource);
           // byte[] ba = JasperExportManager.exportReportToPdf(jasperPrint);
            //Filedownload.save(ba, "application/x-download", "SalaryReceipt.pdf");
        }
    }

    public String getMonthAndYear() {
        return monthAndYear;
    }

    public void setMonthAndYear(String monthAndYear) {
        this.monthAndYear = monthAndYear;
    }


    public List<Month> getMonthList() {
        return monthList;
    }

    public void setMonthList(List<Month> monthList) {
        this.monthList = monthList;
    }

    public List<String> getYearList() {
        return yearList;
    }

    public void setYearList(List<String> yearList) {
        this.yearList = yearList;
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

    public PayrollService getPayrollService() {
        return payrollService;
    }

    public void setPayrollService(PayrollService payrollService) {
        this.payrollService = payrollService;
    }

}
*/
