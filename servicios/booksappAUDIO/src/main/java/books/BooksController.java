package books;



import ws.schild.jave.AudioAttributes;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncodingAttributes;
import ws.schild.jave.MultimediaObject;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import javax.imageio.ImageIO;
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
import java.nio.file.Files;

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
            
            File docx = new File(newName);
            InputStream inputStream = new FileInputStream(docx);
            
            InputStreamResource resource = new InputStreamResource(inputStream);
            
//            IOUtils.copy(inputStream, response.getOutputStream());
//            response.flushBuffer();
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
    
    
    @PostMapping("/convertPngTOJpg")
    public ResponseEntity<Resource> convertImage(@RequestParam("file") MultipartFile file, @RequestParam("type") String type, HttpServletRequest request){
    	
    	try {
    		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        	int idx = fileName.lastIndexOf('.');
        	String newName = fileName.substring(0,idx)+"."+type;
        	InputStream is = file.getInputStream();
        	BufferedImage image = ImageIO.read(is);
        	
            BufferedImage result = new BufferedImage(
                    image.getWidth(),
                    image.getHeight(),
                    BufferedImage.TYPE_INT_RGB);
            result.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);
            OutputStream out = new FileOutputStream(new File(newName));
            ImageIO.write(result, "jpg", out);
            File img = new File(newName);
            InputStream inputStream = new FileInputStream(img);
            
            
            InputStreamResource resource = new InputStreamResource(inputStream);
            img.delete();
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/"+type))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + newName + "\"")
                    .body(resource);
    		
    	}catch(Exception ex) {
    		java.util.logging.Logger.getLogger(BooksController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.badRequest().build();
    	}
    	
    	
    	
    }
    
    @PostMapping("/convertAudio")
    public ResponseEntity<Resource> convertAudio(@RequestParam("file") MultipartFile file, @RequestParam("type") String type, HttpServletRequest request){
    	
    	 try {    
    		 
    		 String fileName = StringUtils.cleanPath(file.getOriginalFilename());
         	int idx = fileName.lastIndexOf('.');
         	String newName = fileName.substring(0,idx)+"."+type;
         	InputStream is = file.getInputStream();
         	
  		   File source = new File(fileName);		                 
  		   File target = new File(newName);                                            
  		       //Audio Attributes            
  		   Files.copy(is, source.toPath()); 
  		   AudioAttributes audio = new AudioAttributes();              
  		   audio.setCodec("libmp3lame");                               
  		   audio.setBitRate(128000);                                   
  		   audio.setChannels(2);                                       
  		   audio.setSamplingRate(44100);                               
  		                                                               
  		   //Encoding attributes                                       
  		   EncodingAttributes attrs = new EncodingAttributes();        
  		   attrs.setFormat("mp3");                                     
  		   attrs.setAudioAttributes(audio);                            
  		                                                               
  		   //Encode                                                    
  		   Encoder encoder = new Encoder();                            
  		   encoder.encode(new MultimediaObject(source), target, attrs);
  		 InputStream inputStream = new FileInputStream(new File(newName));
         
         InputStreamResource resource = new InputStreamResource(inputStream);
         source.delete();
         target.delete();
         return ResponseEntity.ok()
                 .contentType(MediaType.parseMediaType("application/"+type))
                 .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + newName + "\"")
                 .body(resource);
  		   
  		                                                                
  		  } catch (Exception ex) {                                      
  			java.util.logging.Logger.getLogger(BooksController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.badRequest().build();                 
  		  }  
    	
    	
    	
    }
    
    

}