package pe.edu.cibertec.qhatumana.config.jasperreport;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRSaver;

import java.io.InputStream;

//@Configuration //se habilitara cuando generemos el pdf u otra cosa
public class JasperReportConfig {
    public JasperReport compileReport() throws JRException {
        InputStream employeeReportStream = getClass().getResourceAsStream("/factura.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(employeeReportStream);
        JRSaver.saveObject(jasperReport, "src/main/resources/factura.jasper");
        return jasperReport;
    }
}
