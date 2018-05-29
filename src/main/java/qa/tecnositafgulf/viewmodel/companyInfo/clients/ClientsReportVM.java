package qa.tecnositafgulf.viewmodel.companyInfo.clients;

/**
 * Created by klajdi on 08/05/18.
 */

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.ListModelList;
import qa.tecnositafgulf.config.ReportConfig;
import qa.tecnositafgulf.config.ReportType;
import qa.tecnositafgulf.model.reports.ClientReportDataSource;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.util.Arrays;


public class ClientsReportVM  extends IntranetVM {

    ReportType reportType = null;
    private ReportConfig reportConfig = null;

    private ListModelList<ReportType> reportTypesModel = new ListModelList<ReportType>(
            Arrays.asList(
                    new ReportType("PDF", "pdf"),
                    new ReportType("HTML", "html"),
                    new ReportType("Word (RTF)", "rtf"),
                    new ReportType("CSV", "csv"),
                    new ReportType("OpenOffice (ODT)", "odt")));


    @Command("showReport")
    @NotifyChange("reportConfig")
    public void showReport() {
        reportConfig = new ReportConfig();
        reportConfig.setType(reportType);
        reportConfig.setDataSource(new ClientReportDataSource());
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
