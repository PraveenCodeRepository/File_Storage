package com.praveen.Spring_FileStorage_Directory.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.praveen.Spring_FileStorage_Directory.dto.FileDto;
import com.praveen.Spring_FileStorage_Directory.service.FileService;

@RestController
@RequestMapping("/file")
public class FileController {

	private final FileService fileService;

	public FileController(FileService fileService) {
		super();
		this.fileService = fileService;
	}
	
	@PostMapping("/upload")
	public ResponseEntity<?> uploadFile(@RequestParam MultipartFile file){
		
		   try {
			       String fileName = file.getOriginalFilename();
			        String fileType = file.getContentType();
			          byte[] data =   file.getBytes();
			          
			        FileDto fileDto =  FileDto.builder()
			                           .name(fileName)
			                            .type(fileType)
			                            .data(data)
			                            .build();
			   
			  FileDto fileUploaded = fileService.saveFile(fileDto);
			  
			  return new ResponseEntity<>(fileUploaded , HttpStatus.CREATED);
			   
		   }catch(Exception e) {
			   
			   return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			   
		   }
		
	}
	
	@GetMapping("/download/{id}")
	public ResponseEntity<byte[]> downloadFile(@PathVariable Integer id){
		 
		     try {
		    	FileDto fileDto = fileService.getFileById(id);
		    	
		    	HttpHeaders headers = new HttpHeaders();
		    	
		    	headers.setContentType(MediaType.parseMediaType(fileDto.getType()));
		    	headers.setContentDispositionFormData("attachment", fileDto.getName());
		    	
		    	return new ResponseEntity<byte[]>(fileDto.getData(),headers,HttpStatus.OK);
		    	
		     }
		     catch(Exception e) {
		    	 
		    	 return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
		     }
	}
	
	
	
}
