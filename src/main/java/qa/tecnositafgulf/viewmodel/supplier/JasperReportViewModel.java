package qa.tecnositafgulf.viewmodel.supplier;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.ListModelList;
import qa.tecnositafgulf.model.suppliers.CustomDataSource;
import qa.tecnositafgulf.model.suppliers.ReportConfig;
import qa.tecnositafgulf.model.suppliers.ReportType;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.util.Arrays;

public class JasperReportViewModel  extends IntranetVM {
 
    ReportType reportType = null;
    private ReportConfig reportConfig = null;
     
    private ListModelList<ReportType> reportTypesModel = new ListModelList<ReportType>(
            Arrays.asList(
                    new ReportType("PDF", "pdf"), 
                    new ReportType("HTML", "html"), 
                    new ReportType("Word (RTF)", "rtf"), 
                    new ReportType("Excel", "xls"), 
                    new ReportType("Excel (JXL)", "jxl"), 
                    new ReportType("CSV", "csv"), 
                    new ReportType("OpenOffice (ODT)", "odt")));
 
 
    @Command("showReport")
    @NotifyChange("reportConfig")
    public void showReport() {
        reportConfig = new ReportConfig();
        reportConfig.setType(reportType);
        reportConfig.setDataSource(new CustomDataSource());
    }
     
    public ListModelList<ReportType> getReportTypesModel() {
        return reportTypesModel;
    }
 
    public ReportConfig getReportConfig() {
        return reportConfig;
    }
     
    public ReportType getReportType() {
        return reportType;
    }
 
    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }
}