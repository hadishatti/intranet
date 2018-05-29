package qa.tecnositafgulf.viewmodel.companyInfo.partners;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.ListModelList;
import qa.tecnositafgulf.config.ReportConfig;
import qa.tecnositafgulf.config.ReportType;
import qa.tecnositafgulf.model.reports.PartnerReportDataSource;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.util.Arrays;

/**
 * Created by ameljo on 5/8/18.
 */
public class ReportPartnerViewModel  extends IntranetVM {
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
    public void showReport(@BindingParam("name") String name) {
        reportConfig = new ReportConfig();
        reportConfig.setType(reportType);
        reportConfig.buildSource(name);
        reportConfig.setDataSource(new PartnerReportDataSource());
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
