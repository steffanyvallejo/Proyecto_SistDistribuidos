package books;

import com.aspose.pdf.DocSaveOptions;
import com.aspose.pdf.Document;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
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

        //String contentType = request.getServletContext().getMimeType(fileName);
        int idx = fileName.lastIndexOf('.');
        
        String newName = fileName.substring(0,idx)+"."+type;
        InputStream resp = null;
        try {
            resp = file.getInputStream();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(BooksController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.badRequest().build();
        }

        Document doc = new Document(resp);
        DocSaveOptions saveOptions = new DocSaveOptions();
        saveOptions.setFormat(DocSaveOptions.DocFormat.DocX);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        doc.save(outStream, saveOptions);
        ByteArrayInputStream inStream = new ByteArrayInputStream(outStream.toByteArray());
        InputStreamResource resource = new InputStreamResource(inStream);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/"+type))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + newName + "\"")
                .body(resource);

    }

}