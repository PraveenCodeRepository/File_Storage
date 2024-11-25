package com.praveen.Spring_FileStorage_Directory.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.io.File;
import org.springframework.stereotype.Service;
import com.praveen.Spring_FileStorage_Directory.dto.FileDto;
import com.praveen.Spring_FileStorage_Directory.entity.Filee;
import com.praveen.Spring_FileStorage_Directory.repository.FileRepository;
import com.praveen.Spring_FileStorage_Directory.util.FileUtil;
import com.praveen.Spring_FileStorage_Directory.util.FileUtil1;

@Service
public class FileService {

	private static final String IMAGE_STORAGE_DIRECTORY = "C:\\Users\\PRAVEEN\\Desktop\\Store_Files\\image";

	private static final String PDF_STORAGE_DIRECTORY = "C:\\Users\\PRAVEEN\\Desktop\\Store_Files\\pdf";

	String filePath;

	private final FileRepository fileRepository;

	public FileService(FileRepository fileRepository) {
		this.fileRepository = fileRepository;
	}

	public FileDto saveFile(FileDto fileDto) throws IOException {
		 
		if (fileDto != null) {
			
			if (fileDto.getType().startsWith("image/"))
				filePath = IMAGE_STORAGE_DIRECTORY + File.separator +fileDto.getName();

			else if (fileDto.getType().startsWith("application/"))
				filePath = PDF_STORAGE_DIRECTORY + File.separator+ fileDto.getName();

			Path path = Paths.get(filePath);
			Path parentPath = path.getParent();

			Files.createDirectories(parentPath);
			byte[] compressedData = FileUtil.compressData(fileDto.getData());
			Files.write(path, compressedData);


			Filee filee = Filee.builder()
					            .name(fileDto.getName())
				            	.type(fileDto.getType())
					            .path(filePath).build();

			Filee fileSaved = fileRepository.save(filee);

			FileDto fileDtoResponse = FileDto.builder()
				                        	 .id(fileSaved.getId())
					                         .name(fileSaved.getName())
					                         .type(fileSaved.getType())
					                         .path(fileSaved.getPath())
					                         .createdAt(fileSaved.getCreatedAt())
					                         .updatedAt(fileSaved.getUpdatedAt())
					                         .data(compressedData)
					                         .build();
			return fileDtoResponse;

		}

		else
			throw new RuntimeException("File not Saved");

	}

	public FileDto getFileById(Integer id) throws IOException {

		Optional<Filee> file = fileRepository.findById(id);

		if (file.isPresent()) {

			Filee fileFromDb = file.get();
			String filePath = fileFromDb.getPath();
			
			Path path = Paths.get(filePath);
			byte[] data = Files.readAllBytes(path);
			
		    byte[] data1 = FileUtil.decompressData(data);

			FileDto fileDtoResponse = FileDto.builder()
					                         .id(fileFromDb.getId())
					                         .name(fileFromDb.getName())
					                         .type(fileFromDb.getType())
					                         .data(data1)
					                         .createdAt(fileFromDb.getCreatedAt())
					                         .updatedAt(fileFromDb.getUpdatedAt())
					                         .build();

			return fileDtoResponse;

		} else
			throw new RuntimeException("File not found");
	}

}
