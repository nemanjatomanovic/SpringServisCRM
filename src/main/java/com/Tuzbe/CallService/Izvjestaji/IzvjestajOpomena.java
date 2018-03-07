package com.Tuzbe.CallService.Izvjestaji;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;


import com.Tuzbe.CallService.Pojo.PojoIzvjestaji;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Component
public class IzvjestajOpomena {
	public void printaj(List<PojoIzvjestaji> lista, String korisnikId, String nalogId) {

		try {
			InputStream input = new FileInputStream(new File("/opt/apache-tomcat-8.0.41/webapps/KonfFile/opomene.jasper"));
			Path path2 = Paths.get("/opt/apache-tomcat-8.0.41/webapps/crm_files/" + korisnikId + "/" + nalogId);
			Files.createDirectories(path2);
			JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(lista);

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("source", itemsJRBean);

			JasperPrint jasperPrint = JasperFillManager.fillReport(input, parameters, new JREmptyDataSource());

			OutputStream resource = new FileOutputStream(new File(path2 + "/opomena.pdf"));
			JasperExportManager.exportReportToPdfStream(jasperPrint, resource);
			
			resource.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
