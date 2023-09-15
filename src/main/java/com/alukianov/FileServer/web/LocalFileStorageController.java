package com.alukianov.FileServer.web;

import com.alukianov.FileServer.models.Response;
import com.alukianov.FileServer.servises.LocalFileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping(value = "/api/v1/local/files")
@RequiredArgsConstructor
public class LocalFileStorageController {

    private final LocalFileStorage fileStorage;

    @PostMapping
    public ResponseEntity<Response> uploadFiles(@RequestParam("files") MultipartFile[] files,
                                                @RequestParam(value = "path",
                                                        required=false,
                                                        defaultValue = "") String path) {
        fileStorage.uploadMultipleFiles(files, path);
        return ResponseEntity.ok(Response.builder()
                .message("Files successfully uploaded!")
                .payload(null)
                .build()
        );
    }

    @GetMapping
    public ResponseEntity<Response> getAllFilesData() {
        return ResponseEntity.ok(Response.builder()
                .message("Files list")
                .payload(fileStorage.filesList())
                .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        Resource resource = fileStorage.downloadFile(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteFile(@PathVariable Long id) {
        fileStorage.deleteFile(id);
        return ResponseEntity.ok(Response.builder()
                .message("File with id " + id + " deleted!")
                .payload(null)
                .build()
        );
    }
}
