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
    
    
    
    

}