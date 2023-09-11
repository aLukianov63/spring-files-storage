package com.alukianov.FileServer.servises;

import com.alukianov.FileServer.models.FileData;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public class S3FileStorage implements FileStorageService{

    @Override
    public void uploadFile(MultipartFile file) {

    }

    @Override
    public void uploadMultipleFiles(MultipartFile[] files) {

    }

    @Override
    public Resource downloadFile(long id) {
        return null;

    }

    @Override
    public void deleteFile(long id) {

    }

    @Override
    public List<FileData> filesList() {
        return null;
    }

    @Override
    public Optional<FileData> fileData(long id) {
        return null;
    }
}
