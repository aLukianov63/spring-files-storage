package com.alukianov.FileServer;

import com.alukianov.FileServer.models.FileData;
import com.alukianov.FileServer.models.Response;
import com.alukianov.FileServer.servises.LocalFileStorage;
import com.alukianov.FileServer.web.LocalFileStorageController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LocalFileStorageControllerTest {

    @InjectMocks
    private LocalFileStorageController fileStorageController;

    @Mock
    private LocalFileStorage fileStorage;

    @Test
    public void testUploadFiles() {
        MultipartFile[] mockFiles = new MultipartFile[2];

        doNothing().when(fileStorage).uploadMultipleFiles(any(), anyString());

        ResponseEntity<Response> response = fileStorageController.uploadFiles(mockFiles, "");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Files successfully uploaded!", Objects.requireNonNull(response.getBody()).message());
    }

    @Test
    public void testGetAllFilesData() {
        List<FileData> mockFilesList = new ArrayList<>();
        mockFilesList.add(new FileData());
        mockFilesList.add(new FileData());

        when(fileStorage.filesList()).thenReturn(mockFilesList);

        ResponseEntity<Response> response = fileStorageController.getAllFilesData();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Files list", Objects.requireNonNull(response.getBody()).message());
        assertEquals(mockFilesList, response.getBody().payload());
    }

    @Test
    public void testDownloadFile() {
        Resource mockResource = org.mockito.Mockito.mock(Resource.class);

        when(fileStorage.downloadFile(anyLong())).thenReturn(mockResource);

        ResponseEntity<Resource> response = fileStorageController.downloadFile(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testDeleteFile() {
        doNothing().when(fileStorage).deleteFile(anyLong());

        ResponseEntity<Response> response = fileStorageController.deleteFile(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("File with id 1 deleted!", Objects.requireNonNull(response.getBody()).message());
    }

}

