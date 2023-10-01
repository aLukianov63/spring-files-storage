package com.alukianov.FileServer;

import com.alukianov.FileServer.models.FileData;
import com.alukianov.FileServer.repositories.FileDataRepository;
import com.alukianov.FileServer.servises.S3FileStorage;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class S3FileStorageTest {

    @InjectMocks
    private S3FileStorage fileStorage;

    @Mock
    private AmazonS3 amazonS3Client;

    @Mock
    private FileDataRepository fileDataRepository;


    @Test
    public void testUploadFile() throws IOException {
        MockMultipartFile mockFile = new MockMultipartFile("file",
                "test.txt", "text/plain", "Hello, World!".getBytes());
        String path = "test/path";
        String bucketName = "test-bucket";

        assertDoesNotThrow(() -> fileStorage.uploadFile(mockFile, path));

        assertNotNull(mockFile);
    }

    @Test
    public void testDownloadFile() throws IOException {
        long fileId = 1L;
        FileData fileData = new FileData();
        fileData.setFilePath("test/path/test.txt");

        byte[] content = "Hello, World!".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content);

        S3Object s3Object = new S3Object();
        s3Object.setObjectContent(new S3ObjectInputStream(inputStream, null));

        when(amazonS3Client.getObject(any(), anyString())).thenReturn(s3Object);

        when(fileDataRepository.findById(fileId)).thenReturn(Optional.of(fileData));

        Resource resource = fileStorage.downloadFile(fileId);

        assertNotNull(resource);
        assertTrue(resource.exists());

        byte[] readContent = new byte[content.length];
        assertEquals(content.length, resource.getInputStream().read(readContent));
        assertArrayEquals(content, readContent);
    }


    @Test
    public void testDeleteFile() {
        long fileId = 1L;
        FileData fileData = new FileData();
        fileData.setName("test.txt");

        when(fileDataRepository.findById(fileId)).thenReturn(Optional.of(fileData));

        assertDoesNotThrow(() -> fileStorage.deleteFile(fileId));
    }

    @Test
    public void testDeleteFileWhenFileDoesNotExist() {
        long fileId = 1L;

        when(fileDataRepository.findById(fileId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> fileStorage.deleteFile(fileId));
    }

    @Test
    public void testFilesList() {
        List<FileData> mockFilesList = List.of(new FileData(), new FileData());

        when(fileDataRepository.findAll()).thenReturn(mockFilesList);

        List<FileData> files = fileStorage.filesList();

        assertNotNull(files);
        assertEquals(mockFilesList.size(), files.size());
    }

    @Test
    public void testFileData() {
        long fileId = 1L;
        FileData mockFileData = new FileData();

        when(fileDataRepository.findById(fileId)).thenReturn(Optional.of(mockFileData));

        Optional<FileData> fileData = fileStorage.fileData(fileId);

        assertTrue(fileData.isPresent());
        assertEquals(mockFileData, fileData.get());
    }

    @Test
    public void testFileDataWhenFileDoesNotExist() {
        long fileId = 1L;

        when(fileDataRepository.findById(fileId)).thenReturn(Optional.empty());

        Optional<FileData> fileData = fileStorage.fileData(fileId);

        assertFalse(fileData.isPresent());
    }
}
