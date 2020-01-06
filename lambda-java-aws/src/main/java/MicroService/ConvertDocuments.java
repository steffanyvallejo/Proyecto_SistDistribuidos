package MicroService;

import com.aspose.pdf.DocSaveOptions;
import com.aspose.pdf.Document;
import com.aspose.pdf.Page;
import com.aspose.pdf.PdfSaveOptions;
import com.aspose.pdf.PptxSaveOptions;
import com.aspose.pdf.SaveFormat;
import com.aspose.pdf.TextAbsorber;
import com.aspose.pdf.TextExtractionOptions;
import com.aspose.pdf.devices.TextDevice;
import com.aspose.slides.PdfOptions;
import com.aspose.slides.Presentation;

import java.io.File;
import java.io.IOException;

public class ConvertDocuments {

    public void pdfToDocx(String filename) {
        /**
         * Directory
         */
        File dir = new File(System.getProperty("user.dir"));
        dir = new File(dir, "resources");

        /**
         * File convertion
         */ 
        Document doc = new Document(dir.toString() + "\\" + filename);
        DocSaveOptions saveOptions = new DocSaveOptions();
        saveOptions.setFormat(DocSaveOptions.DocFormat.DocX);
        doc.save(dir.toString() + "\\" + filename.split("\\.")[0] + ".docx", saveOptions);
        
 
        
    }

    public void pdfToPptx(String filename) {
       
    	File dir = new File(System.getProperty("user.dir"));
        dir = new File(dir, "resources");
        Document doc = new Document(dir.toString() + "\\" + filename);

  
        PptxSaveOptions pptx_save = new PptxSaveOptions();
     // Save the output in PPTX format
        doc.save("output.pptx", pptx_save);
    }
    
    
    
   public void pptToPdf(String filename) {

	   File dir = new File(System.getProperty("user.dir"));
       dir = new File(dir, "resources");
      
       Presentation pres = new Presentation(dir.toString() + "\\" + filename);

       try {
    		// Instantiate the PdfOptions class
    		PdfOptions pdfOptions = new PdfOptions();

    		// Specify that the generated document should include hidden slides
    		pdfOptions.setShowHiddenSlides(true);

    		// Save the presentation to PDF with specified options
    		pres.save("output.pdf", SaveFormat.Pdf, pdfOptions);
    	} finally {
    		if (pres != null)
    			pres.dispose();
    	}

  }

    public static void main(String[] args) throws IOException {
        ConvertDocuments convertDocuments = new ConvertDocuments();
       // convertDocuments.pdfToDocx("input.pdf");
       // convertDocuments.pdfToPptx("input.pdf");
        convertDocuments.pptToPdf("input.pptx");
    }
}
