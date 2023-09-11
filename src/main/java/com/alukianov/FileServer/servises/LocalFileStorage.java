package com.alukianov.FileServer.servises;

import com.alukianov.FileServer.models.FileData;
import com.alukianov.FileServer.repositories.FileDataRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class LocalFileStorage implements FileStorageService {

    private final FileDataRepository fileDataRepository;

    @Value(value = "${application.storage.directory}")
    private String STORAGE_DIRECTORY;

    private Path STORAGE_PATH;

    public Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    public void init() {
        STORAGE_PATH = Paths.get(STORAGE_DIRECTORY);
        try {
            if (!new File(STORAGE_DIRECTORY).exists()) {
                Files.createDirectory(STORAGE_PATH);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder files!");
        }
    }

    @Override
    public void uploadFile(MultipartFile file) {
        Optional<String> fileExtension = getExtensionByStringHandling(file.getOriginalFilename());

        if (fileExtension.isPresent()) {
            String uuidName = UUID.randomUUID() + "." + fileExtension.get();
            try {
                Files.copy(file.getInputStream(), this.STORAGE_PATH.resolve(uuidName));
            } catch (Exception e) {
                if (e instanceof FileAlreadyExistsException) {
                    throw new RuntimeException("A file of that name already exists.");
                }
                throw new RuntimeException(e.getMessage());
            }
            fileDataRepository.save(FileData.builder()
                    .name(uuidName)
                    .type(file.getContentType())
                    .size(file.getSize())
                    .filePath("\\" + uuidName)
                    .createdAt(LocalDateTime.now())
                    .build()
            );
        }
    }

    @Override
    public void uploadMultipleFiles(MultipartFile[] files) {
        Arrays.stream(files).forEach(this::uploadFile);
    }

    @Override
    public Resource downloadFile(long id) {
        try {
            Optional<FileData> fileData = fileDataRepository.findById(id);

            if (fileData.isPresent()) {
                Path file = STORAGE_PATH.resolve(fileData.get().getName());
                Resource resource = new UrlResource(file.toUri());

                if (resource.exists() || resource.isReadable()) {
                    return  resource;
                }
                else {
                    throw new RuntimeException("Could not read the file!");
                }
            }
            else {
                throw new NoSuchElementException("File with id" + id + "does not exist");
            }

        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }


    }

    @Override
    public void deleteFile(long id) {
        Optional<FileData> fileData = fileDataRepository.findById(id);
        if (fileData.isPresent()) {
            File file = new File(STORAGE_PATH + fileData.get().getFilePath());
            if (file.delete()) {
                fileDataRepository.delete(fileData.get());
            }
        }
        else {
            throw new NoSuchElementException("File with id" + id + "does not exist");
        }
    }

    @Override
    public List<FileData> filesList() {
        return fileDataRepository.findAll();
    }

    @Override
    public Optional<FileData> fileData(long id) {
        return fileDataRepository.findById(id);
    }
}
