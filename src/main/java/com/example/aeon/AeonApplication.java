package com.example.aeon;

import com.example.aeon.controller.fileupload.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class AeonApplication {

	public static void main(String[] args) {
		SpringApplication.run(AeonApplication.class, args);
	}

}
