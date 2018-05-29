package qa.tecnositafgulf.model.suppliers;

import net.sf.jasperreports.engine.JRDataSource;

import java.util.HashMap;
import java.util.Map;
 
public class ReportConfig {
 
    private String source = "/home/ledio/JaspersoftWorkspace/MyReports/Supplier_Report.jasper";
    private Map<String, Object> parameters;
    private JRDataSource dataSource;
    private ReportType type;
 
    public ReportConfig() {
        parameters = new HashMap<String, Object>();
        parameters.put("ReportTitle", "Address Report");
        parameters.put("DataFile", "CustomDataSource from java");
    }
     
    public ReportType getType() {
        return type;
    }
 
    public void setType(ReportType selectedReportType) {
        this.type = selectedReportType;
    }
 
    public String getSource() {
        return source;
    }
 
    public Map<String, Object> getParameters() {
        return parameters;
    }
 
    public JRDataSource getDataSource() {
        return dataSource;
    }
 
    public void setDataSource(JRDataSource reportDataSource) {
        this.dataSource = reportDataSource;
    }
}