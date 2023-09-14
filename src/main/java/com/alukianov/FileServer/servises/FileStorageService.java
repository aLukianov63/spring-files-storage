package com.alukianov.FileServer.servises;

import com.alukianov.FileServer.models.FileData;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

public interface FileStorageService {
    void uploadFile(MultipartFile file, String path);
    void uploadMultipleFiles(MultipartFile[] files, String folder);
    Resource downloadFile(long id) throws FileNotFoundException, MalformedURLException;
    void deleteFile(long id);
    List<FileData> filesList();
    Optional<FileData> fileData(long id);
}
