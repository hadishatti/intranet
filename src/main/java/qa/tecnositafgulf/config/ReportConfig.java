package qa.tecnositafgulf.config;

import net.sf.jasperreports.engine.JRDataSource;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ameljo on 5/8/18.
 */
public class ReportConfig {
    private String source;
    private Map<String, Object> parameters;
    private JRDataSource dataSource;
    private ReportType type;

    public ReportConfig() {
        parameters = new HashMap<>();
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

//    //public void buildSource(String name){
//        source = "/jasper/" + name + ".jasper";
//    }

    public void buildSource(String name){
        source = name;
    }

    public void setDataSource(JRDataSource reportDataSource) {
        this.dataSource = reportDataSource;
    }
}
