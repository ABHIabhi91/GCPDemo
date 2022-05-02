package com.example.demo;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.cloud.ReadChannel;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

@RestController
@RequestMapping("/gcp")
public class GcpStorageDemo {

	

	
	@Autowired
	private Storage storage;
	

	@GetMapping("send-data")
	public String sendData() throws IOException {

		BlobId id = BlobId.of("file-storage-demo", "demofile.txt");
		BlobInfo info = BlobInfo.newBuilder(id).build();

		ClassLoader classLoader = getClass().getClassLoader();
		URL resource = classLoader.getResource("demofile.txt");
		System.out.println("resource::::" + resource);
		if (resource == null) {
			throw new IllegalArgumentException("file is not found!");
		} else {

			InputStream serviceAccount = new ClassPathResource("demofile.txt").getInputStream();
			// File file =new File(resource.getFile());
			byte[] array = IOUtils.toByteArray(serviceAccount);
			storage.create(info, array);
		}
		return "File uploaded Successfully";
	}
	
	



	

	@GetMapping("get-data")
	public String getData() throws IOException {
		StringBuffer sb = new StringBuffer();

		try (ReadChannel channel = storage.reader("file-storage-demo", "demofile.txt")) {
			ByteBuffer byteBuffer = ByteBuffer.allocate(64 * 1024);
			while (channel.read(byteBuffer) > 0) {
				byteBuffer.flip();
				String data = new String(byteBuffer.array(), 0, byteBuffer.limit());
				sb.append(data);
				byteBuffer.clear();

			}
			return sb.toString();
		}
	}
}
