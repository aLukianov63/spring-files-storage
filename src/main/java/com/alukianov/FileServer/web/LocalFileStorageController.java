package com.alukianov.FileServer.web;

import com.alukianov.FileServer.servises.LocalFileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/v1/files")
@RequiredArgsConstructor
public class FileStorageController {

    private final LocalFileStorage fileStorage;

    @PostMapping
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        fileStorage.uploadFile(file);
        return ResponseEntity.ok("upload");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable Long id) {
        Resource file = fileStorage.downloadFile(id);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable Long id) {
        fileStorage.deleteFile(id);
        return ResponseEntity.ok("delete");
    }
}
