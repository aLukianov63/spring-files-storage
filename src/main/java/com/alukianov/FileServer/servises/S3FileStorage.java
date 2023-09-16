package com.alukianov.FileServer.servises;

import com.alukianov.FileServer.models.FileData;
import com.alukianov.FileServer.repositories.FileDataRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3FileStorage implements FileStorageService {

    private final AmazonS3 amazonS3Client;

    @Value("${file-storage.s3.bucket-name}")
    private String bucketName;

    private final FileDataRepository fileDataRepository;

    @Override
    public void uploadFile(MultipartFile file, String path) {
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        ObjectMetadata data = new ObjectMetadata();
        PutObjectRequest putObjectRequest;
        data.setContentType(file.getContentType());
        data.setContentLength(file.getSize());
        String uuidName = UUID.randomUUID() + "." + fileExtension;

        try {
            putObjectRequest = new PutObjectRequest(bucketName, path + "/" + uuidName,
                    file.getInputStream(), data).withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3Client.putObject(putObjectRequest);

            if (!path.isEmpty()) path = path.replaceAll("/", "\\\\");

            fileDataRepository.save(FileData.builder()
                    .name(uuidName)
                    .type(file.getContentType())
                    .size(file.getSize())
                    .filePath(path + "\\" + uuidName)
                    .createdAt(LocalDateTime.now())
                    .extension(fileExtension)
                    .build()
            );

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void uploadMultipleFiles(MultipartFile[] files, String path) {
        Arrays.stream(files).forEach(file -> uploadFile(file, path));
    }

    @Override
    public Resource downloadFile(long id)  {
        Optional<FileData> fileData = fileData(id);

        if (fileData.isPresent()) {
            String fileName = fileData.get().getFilePath().replace("\\", "/");
            S3Object s3object = amazonS3Client.getObject(bucketName, fileName);
            S3ObjectInputStream inputStream = s3object.getObjectContent();
            File file = new File(fileName);

            try {
                FileUtils.copyInputStreamToFile(inputStream, file);
                Resource resource = new UrlResource(file.toURI());

                if (resource.exists() || resource.isReadable()) {
                    return resource;
                } else {
                    throw new RuntimeException("Could not read the file!");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            throw new NoSuchElementException("File with id" + id + "does not exist");
        }
    }

    @Override
    public void deleteFile(long id) {
        Optional<FileData> fileData = fileDataRepository.findById(id);

        if (fileData.isPresent()) {
            amazonS3Client.deleteObject(bucketName, fileData.get().getName());
            fileDataRepository.deleteById(id);
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
