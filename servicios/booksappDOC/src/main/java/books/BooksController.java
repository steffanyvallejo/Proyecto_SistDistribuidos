package books;






import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;


@RestController
public class BooksController {


    
   
    
    @PostMapping("/convertDocxPdf")
    public ResponseEntity<Resource> convertDocxPdf(@RequestParam("file") MultipartFile file, @RequestParam("type") String type, HttpServletRequest request) {

    	
        try {        	
        	String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        	int idx = fileName.lastIndexOf('.');
        	String newName = fileName.substring(0,idx)+"."+type;
        	InputStream is = file.getInputStream();
            OutputStream out = new FileOutputStream(new File(fileName.substring(0,idx)+"."+type));
            
            XWPFDocument document = new XWPFDocument(is);
            PdfOptions options = PdfOptions.create();
            PdfConverter.getInstance().convert(document, out, options);
            
            File docx = new File(newName);
            InputStream inputStream = new FileInputStream(docx);
            
            InputStreamResource resource = new InputStreamResource(inputStream);
            
            docx.delete();
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/"+type))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + newName + "\"")
                    .body(resource);
            
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(BooksController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.badRequest().build();
        }

    }
    
    
    
    
    
    

}