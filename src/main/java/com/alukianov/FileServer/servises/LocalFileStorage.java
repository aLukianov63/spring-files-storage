package com.alukianov.FileServer.servises;

import com.alukianov.FileServer.models.FileData;
import com.alukianov.FileServer.repositories.FileDataRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class LocalFileStorage implements FileStorageService {

    private final FileDataRepository fileDataRepository;

    @Value(value = "${file-storage.local.directory}")
    private String STORAGE_DIRECTORY;

    private Path STORAGE_PATH;

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
    public void uploadFile(MultipartFile file, String subPath) {
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        String uuidName = UUID.randomUUID() + "." + fileExtension;
        Path path = STORAGE_PATH;

        try {
            if (!subPath.isEmpty()) {
                path = Paths.get(STORAGE_PATH.toString(), subPath);
                Files.createDirectories(path);
                subPath = subPath.replaceAll("/", "\\\\");
            }
            Files.copy(file.getInputStream(), path.resolve(uuidName));
            fileDataRepository.save(FileData.builder()
                    .name(uuidName)
                    .type(file.getContentType())
                    .size(file.getSize())
                    .filePath(subPath + "\\" + uuidName)
                    .createdAt(LocalDateTime.now())
                    .extension(fileExtension)
                    .build()
            );
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void uploadMultipleFiles(MultipartFile[] files, String subPath) {
        Arrays.stream(files).forEach(file -> uploadFile(file, subPath));
    }

    @Override
    public Resource downloadFile(long id) {
        try {
            Optional<FileData> fileData = fileDataRepository.findById(id);

            if (fileData.isPresent()) {
                Path fullPath = Paths.get(STORAGE_PATH.toString(), fileData.get().getFilePath());
                Resource resource = new UrlResource(fullPath.toUri());

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
            else {
                throw new RuntimeException("Could not delete the file!");
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
