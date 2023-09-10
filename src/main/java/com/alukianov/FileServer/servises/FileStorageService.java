package com.alukianov.FileServer.servises;

import com.alukianov.FileServer.models.FileData;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    void uploadFile(MultipartFile file);
    void uploadMultipleFiles(MultipartFile[] files);
    void downloadFile(long id);
    void deleteFile(long id);
    void filesList();
    FileData fileData(long id);
}
