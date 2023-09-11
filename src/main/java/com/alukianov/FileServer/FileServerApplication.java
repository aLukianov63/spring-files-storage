package com.alukianov.FileServer;

import com.alukianov.FileServer.servises.LocalFileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class FileServerApplication implements CommandLineRunner {

	private final LocalFileStorage fileStorage;

	public static void main(String[] args) {
		SpringApplication.run(FileServerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		fileStorage.init();
	}

}
