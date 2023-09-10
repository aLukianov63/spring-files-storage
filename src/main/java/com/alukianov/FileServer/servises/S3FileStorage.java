package com.alukianov.FileServer.servises;

import com.alukianov.FileServer.models.FileData;
import org.springframework.web.multipart.MultipartFile;

public class S3FileStorage implements FileStorageService{
    @Override
    public void uploadFile(MultipartFile file) {

    }

    @Override
    public void uploadMultipleFiles(MultipartFile[] files) {

    }

    @Override
    public void downloadFile(long id) {

    }

    @Override
    public void deleteFile(long id) {

    }

    @Override
    public void filesList() {

    }

    @Override
    public FileData fileData(long id) {
        return null;
    }
}
