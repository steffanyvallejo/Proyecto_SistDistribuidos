package MicroService;

import com.aspose.pdf.DocSaveOptions;
import com.aspose.pdf.Document;
import java.io.File;

public class ConvertDocuments {
	public void pdfToDocx(String filename) {
		/**
		 * Directory*/
		File dir = new File(System.getProperty("user.dir"));
		dir = new File(dir, "resources");
		
		/**
		 * File convertion*/
		Document doc = new Document(dir.toString() + "\\" + filename);
		DocSaveOptions saveOptions = new DocSaveOptions();
		saveOptions.setFormat(DocSaveOptions.DocFormat.DocX);				
		doc.save(dir.toString() + "\\" + filename.split("\\.")[0] + ".docx", saveOptions);
	}
	
	public static void main(String[] args) {
		ConvertDocuments convertDocuments = new ConvertDocuments();
		convertDocuments.pdfToDocx("input.pdf");		
	}
}
