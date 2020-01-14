package books;

import com.aspose.pdf.DocSaveOptions;
import com.aspose.pdf.Document;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
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
import org.apache.poi.util.IOUtils;

@RestController
public class BooksController {


    private static final Book[] books = {
        new Book(1L, "Nemesis", "Isaac Asimov"),
        new Book(2L, "Great Expectations", "Charles Dickens"),
        new Book(3L, "The Chronicles of Narnia", "C.S. Lewis")
    };

    @GetMapping("/books")
    public Book[] books() {
        return books;
    }

    @GetMapping("/books/{id}")
    public Book book(@PathVariable int id) {
        for (Book book : books) {
            if (book.getId() == id) {
                return book;
            }
			
        } 
        throw new ResponseStatusException(
            HttpStatus.NOT_FOUND, "Entity not found"
        );        
    }
    
    @PostMapping("/uploadFileTest")
    public ResponseEntity<Resource> uploadFileTest(@RequestParam("file") MultipartFile file, @RequestParam("type") String type, HttpServletRequest request) {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        System.out.println("########################################3");
        System.out.println(fileName);
        System.out.println("########################################3");
        
        System.out.println("########################################3");
        System.out.println(type);
        System.out.println("########################################3");
//        String contentType = request.getServletContext().getMimeType(fileName);
        int idx = fileName.lastIndexOf('.');
        
        String newName = fileName.substring(0,idx)+"."+type;
        
        System.out.println("########################################3");
        System.out.println(newName);
        System.out.println("########################################3");
        
        InputStream resp = null;
        try {
            resp = file.getInputStream();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(BooksController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.badRequest().build();
        }
        System.out.println("########################################3");
        System.out.println(resp);
        System.out.println("########################################3");
        Document doc = new Document(resp);
        System.out.println("########################################3");
        System.out.println(doc);
        System.out.println("########################################3");
        DocSaveOptions saveOptions = new DocSaveOptions();
        saveOptions.setFormat(DocSaveOptions.DocFormat.DocX);
        System.out.println("########################################3");
        System.out.println(saveOptions);
        System.out.println("########################################3");                      
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        System.out.println("########################################3");
        System.out.println(outStream);
        System.out.println("########################################3");
        System.out.println("########################################3");
        System.out.println(doc);
        System.out.println("########################################3");
        doc.save(outStream, saveOptions);
        System.out.println("########################################3");
        System.out.println("DESPUES DE DOC.SAVE");
        System.out.println("########################################3");
        ByteArrayInputStream inStream = new ByteArrayInputStream(outStream.toByteArray());
        System.out.println("########################################3");
        System.out.println(inStream);
        System.out.println("########################################3");
        InputStreamResource resource = new InputStreamResource(inStream);
        System.out.println("########################################3");
        System.out.println(resource);
        System.out.println("########################################3");
//        InputStreamResource resource = new InputStreamResource(resp);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/"+type))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + newName + "\"")
                .body(resource);
//        return ResponseEntity.ok()
//              .contentType(MediaType.parseMediaType(contentType))
//              .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
//              .body(resource);

    }
    
    @PostMapping("/convertDocxPdf")
    public ResponseEntity<Resource> convertDocxPdf(@RequestParam("file") MultipartFile file, @RequestParam("type") String type, HttpServletRequest request) {
//    	HttpServletResponse response
    	
        try {        	
        	String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        	int idx = fileName.lastIndexOf('.');
        	String newName = fileName.substring(0,idx)+"."+type;
        	InputStream is = file.getInputStream();
            OutputStream out = new FileOutputStream(new File(fileName.substring(0,idx)+"."+type));
            
            XWPFDocument document = new XWPFDocument(is);
            PdfOptions options = PdfOptions.create();
            PdfConverter.getInstance().convert(document, out, options);
                        
            
            InputStream inputStream = new FileInputStream(new File(newName));
            
            InputStreamResource resource = new InputStreamResource(inputStream);
            
//            IOUtils.copy(inputStream, response.getOutputStream());
//            response.flushBuffer();
            
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