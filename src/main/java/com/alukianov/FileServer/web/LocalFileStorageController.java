package com.alukianov.FileServer.web;

import com.alukianov.FileServer.models.Response;
import com.alukianov.FileServer.servises.LocalFileStorage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Tag(name = "Local files storage", description = "Interacts with files, in local storage")
@RestController
@RequestMapping(value = "/api/v1/local/files")
@RequiredArgsConstructor
public class LocalFileStorageController {

    private final LocalFileStorage fileStorage;

    @Operation(summary = "Upload files to local storage")
    @PostMapping
    public ResponseEntity<Response> uploadFiles(@RequestParam("files") MultipartFile[] files,
                                                @RequestParam(value = "path",
                                                        required = false,
                                                        defaultValue = "") String path) {
        fileStorage.uploadMultipleFiles(files, path);
        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Files successfully uploaded!")
                .payload(null)
                .build()
        );
    }

    @Operation(summary = "Get local storage files list")
    @GetMapping
    public ResponseEntity<Response> getAllFilesData() {
        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Files list")
                .payload(fileStorage.filesList())
                .build()
        );
    }

    @Operation(summary = "Get file with current id")
    @GetMapping("/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        Resource resource = fileStorage.downloadFile(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);

    }

    @Operation(summary = "Delete file with current id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteFile(@PathVariable Long id) {
        fileStorage.deleteFile(id);
        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("File with id " + id + " deleted!")
                .payload(null)
                .build()
        );
    }

}
