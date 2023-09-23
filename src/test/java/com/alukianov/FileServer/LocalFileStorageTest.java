package com.alukianov.FileServer;

import com.alukianov.FileServer.models.FileData;
import com.alukianov.FileServer.repositories.FileDataRepository;
import com.alukianov.FileServer.servises.LocalFileStorage;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LocalFileStorageTest {

    @InjectMocks
    private LocalFileStorage fileStorage;

    @Mock
    private FileDataRepository fileDataRepository;

    @BeforeEach
    public void init() {
        fileStorage.STORAGE_DIRECTORY = "D:\\development\\storage\\";
        fileStorage.init();
    }


    @Test
    @SneakyThrows
    public void testUploadFile() {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("testfile.txt");
        when(mockFile.getContentType()).thenReturn("text/plain");
        when(mockFile.getSize()).thenReturn(100L);
        InputStream inputStream = new ByteArrayInputStream("test file data".getBytes());
        when(mockFile.getInputStream()).thenReturn(inputStream);

        assertDoesNotThrow(() -> fileStorage.uploadFile(mockFile, ""));
    }

    @Test
    public void testDownloadFile() {
        long fileId = 1;
        FileData fileData = new FileData();
        fileData.setId(fileId);
        fileData.setFilePath("testfile.txt");

        when(fileDataRepository.findById(fileId)).thenReturn(Optional.of(fileData));

        Resource resource = fileStorage.downloadFile(fileId);
        assertNotNull(resource);

        try {
            assertTrue(resource.getFile().exists());
            assertTrue(resource.getFile().isFile());
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testDeleteFile() {
        long fileId = 1;
        FileData fileData = new FileData();
        fileData.setId(fileId);
        fileData.setFilePath("/testfile1.txt");

        when(fileDataRepository.findById(fileId)).thenReturn(Optional.of(fileData));

        assertDoesNotThrow(() -> fileStorage.deleteFile(fileId));
    }

    @Test
    public void testFilesList() {
        when(fileDataRepository.findAll()).thenReturn(Collections.emptyList());

        assertEquals(0, fileStorage.filesList().size());
    }

    @Test
    public void testFileData() {
        long fileId = 1;
        FileData fileData = new FileData();
        fileData.setId(fileId);

        when(fileDataRepository.findById(fileId)).thenReturn(Optional.of(fileData));

        Optional<FileData> retrievedFileData = fileStorage.fileData(fileId);
        assertTrue(retrievedFileData.isPresent());
        assertEquals(fileId, retrievedFileData.get().getId());
    }
}

