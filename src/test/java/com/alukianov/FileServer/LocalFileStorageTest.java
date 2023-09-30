package com.alukianov.FileServer;

import com.alukianov.FileServer.models.FileData;
import com.alukianov.FileServer.repositories.FileDataRepository;
import com.alukianov.FileServer.servises.LocalFileStorage;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LocalFileStorageTest {

    public static final String TEMP_DIRECTORY = System.getProperty("user.dir") + "\\" + "test-data";

    @InjectMocks
    private LocalFileStorage storage;

    @Mock
    private FileDataRepository repository;

    @BeforeEach
    public void init() {
        storage.STORAGE_DIRECTORY = TEMP_DIRECTORY;
        storage.init();
    }

    @AfterAll
    @SneakyThrows
    static void cleanup()  {
        FileUtils.deleteDirectory(new File(TEMP_DIRECTORY));
    }

    @Test
    @SneakyThrows
    public void testUploadFile() {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Test file".getBytes());
        String subPath = "/subfolder";

        assertDoesNotThrow(() -> storage.uploadFile(file, ""));
        assertDoesNotThrow(() -> storage.uploadFile(file, subPath));

        verify(repository, times(2)).save(any());
    }

    @Test
    void testUploadAndDownloadFile() throws IOException {
        String content = "Test file content";
        String fileName = "test-file-" + UUID.randomUUID() + ".txt";
        Path tempFile = Files.createTempFile("temp-file", ".txt");
        Files.write(tempFile, content.getBytes());

        Files.move(tempFile, Path.of(TEMP_DIRECTORY, fileName), StandardCopyOption.REPLACE_EXISTING);

        FileData fileData = new FileData();
        fileData.setFilePath(fileName);

        when(repository.findById(1L)).thenReturn(Optional.of(fileData));

        MockMultipartFile file = new MockMultipartFile("file", fileName, "text/plain", content.getBytes());
        storage.uploadFile(file, "");

        Resource downloadedFile = storage.downloadFile(1L);

        assertNotNull(downloadedFile);
        byte[] downloadedBytes = new byte[downloadedFile.getInputStream().available()];
        downloadedFile.getInputStream().read(downloadedBytes);
        String downloadedContent = new String(downloadedBytes);
        assertEquals(content, downloadedContent);
    }

    @Test
    @SneakyThrows
    public void testDeleteFile() {
        String content = "Test file content";
        String fileName = "test-file-" + UUID.randomUUID() + ".txt";
        Path tempFile = Files.createTempFile("temp-file", ".txt");
        Files.write(tempFile, content.getBytes());

        Files.move(tempFile, Path.of(storage.STORAGE_DIRECTORY, fileName), StandardCopyOption.REPLACE_EXISTING);

        FileData fileData = new FileData();
        fileData.setFilePath("\\" + fileName);

        when(repository.findById(1L)).thenReturn(Optional.of(fileData));

        storage.deleteFile(1L);

        assertFalse(Files.exists(Path.of(storage.STORAGE_DIRECTORY, fileName)));
        verify(repository, times(1)).delete(fileData);
    }

    @Test
    public void testFilesList() {
        when(repository.findAll()).thenReturn(List.of(new FileData(), new FileData()));
        List<FileData> fileList = storage.filesList();
        assertNotNull(fileList);
        assertEquals(2, fileList.size());
    }

    @Test
    public void testFileData() {
        long fileId = 1;
        FileData fileData = new FileData();
        fileData.setId(fileId);

        when(repository.findById(fileId)).thenReturn(Optional.of(fileData));

        Optional<FileData> retrievedFileData = storage.fileData(fileId);
        assertTrue(retrievedFileData.isPresent());
        assertEquals(fileId, retrievedFileData.get().getId());
    }

}

