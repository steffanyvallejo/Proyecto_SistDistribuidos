package books;



import ws.schild.jave.AudioAttributes;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncodingAttributes;
import ws.schild.jave.MultimediaObject;


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
import java.nio.file.Files;



@RestController
public class BooksController {


    
    
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