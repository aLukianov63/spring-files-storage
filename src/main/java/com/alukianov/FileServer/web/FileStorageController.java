package com.alukianov.FileServer.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/v1/files")
public class FileStorageController {
    @PostMapping
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok("upload");
    }

    @PostMapping
    public ResponseEntity<?> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        return ResponseEntity.ok("upload");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable Long id) {
        return ResponseEntity.ok("download");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable Long id) {
        return ResponseEntity.ok("delete");
    }
}
