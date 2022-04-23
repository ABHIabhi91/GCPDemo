package com.example.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@RestController
@RequestMapping("/gcp")
public class GcpStorageDemo {
	
	@Autowired
	private Storage storage;
	
	@GetMapping("send-data")
public String sendData() throws IOException {
		
//		GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("/Users/abhisheksingh/Downloads/gcp-image-hosting-55f85a03a0e4.json"));
//
//		 Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
		BlobId id=BlobId.of("storage-java", "demofile.txt");
		BlobInfo info=BlobInfo.newBuilder(id).build();
		
		ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("demofile");
        
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            
            File file =new File(resource.getFile());
    		byte[] array=Files.readAllBytes(Paths.get(file.toURI()));
    		storage.create(info,array);
        }
		
//		File file =new File("Users/abhisheksingh/Downloads","demofile.txt");
//		byte[] array=Files.readAllBytes(Paths.get(file.toURI()));
//		storage.create(info,array);
		return "File uploaded Successfully";
	}
}
