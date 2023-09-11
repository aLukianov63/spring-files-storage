package com.alukianov.FileServer.servises;

import com.alukianov.FileServer.models.FileData;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface FileStorageService {
    void uploadFile(MultipartFile file);
    void uploadMultipleFiles(MultipartFile[] files);
    Resource downloadFile(long id);
    void deleteFile(long id);
    List<FileData> filesList();
    Optional<FileData> fileData(long id);
}
