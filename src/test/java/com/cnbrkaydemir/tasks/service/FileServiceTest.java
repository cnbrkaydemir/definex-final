package com.cnbrkaydemir.tasks.service;

import com.cnbrkaydemir.tasks.exception.filestorage.StorageException;
import com.cnbrkaydemir.tasks.exception.filestorage.StorageFileNotFoundException;
import com.cnbrkaydemir.tasks.service.impl.FileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FileServiceTest {

    @InjectMocks
    private FileServiceImpl fileService;

    @Mock
    private MultipartFile multipartFile;

    private Path rootLocation;
    private final String UPLOAD_DIR = "upload/dir";

    @BeforeEach
    void setUp() throws IOException {
        rootLocation = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();

        ReflectionTestUtils.setField(fileService, "uploadDir", UPLOAD_DIR);
        ReflectionTestUtils.setField(fileService, "rootLocation", rootLocation);

        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.createDirectories(rootLocation)).thenReturn(rootLocation);
            fileService.init();
        }
    }

    @Test
    void testInitCreatesDirectories() {
        Path actualRootLocation = (Path) ReflectionTestUtils.getField(fileService, "rootLocation");
        assertNotNull(actualRootLocation);
        assertEquals(rootLocation.toString(), actualRootLocation.toString());
    }

    @Test
    void testStoreFileSuccessfully() throws IOException {
        // Prepare file data
        String filename = "test.txt";
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getOriginalFilename()).thenReturn(filename);
        when(multipartFile.getInputStream()).thenReturn(mock(InputStream.class));

        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.copy(any(InputStream.class), any(Path.class))).thenReturn(0L);
            fileService.store(multipartFile);
        }
        Path loadedPath = fileService.load(filename);
        Path expectedPath = rootLocation.resolve(filename);
        assertEquals(expectedPath.toString(), loadedPath.toString());
    }

    @Test
    void testStoreEmptyFileThrowsException() {
        when(multipartFile.isEmpty()).thenReturn(true);
        assertThrows(StorageException.class, () -> fileService.store(multipartFile));
    }

    @Test
    void testLoadAllFiles() throws IOException {
        Path mockPath1 = rootLocation.resolve("file1.txt");
        Path mockPath2 = rootLocation.resolve("file2.txt");
        Stream<Path> mockPathStream = Stream.of(mockPath1, mockPath2);

        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.walk(rootLocation, 1)).thenReturn(mockPathStream);
            Stream<Path> result = fileService.loadAll();
            assertEquals(2, result.count());
        }
    }

    @Test
    void testLoadFile() {
        String filename = "test.txt";
        Path path = fileService.load(filename);
        assertNotNull(path);
        assertEquals(rootLocation.resolve(filename).toString(), path.toString());
    }


    @Test
    void testLoadAsResourceFileNotFound() {
        String filename = "missing.txt";
        Path filePath = rootLocation.resolve(filename);

        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.exists(filePath)).thenReturn(false);
            assertThrows(StorageFileNotFoundException.class, () -> fileService.loadAsResource(filename));
        }
    }

    @Test
    void testDeleteFile() throws IOException {
        String filename = "test.txt";
        Path filePath = rootLocation.resolve(filename);

        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.deleteIfExists(filePath)).thenReturn(true);
            fileService.delete(filename);
            mockedFiles.verify(() -> Files.deleteIfExists(filePath));
        }
    }

    @Test
    void testDeleteAllFiles() throws IOException {
        try (MockedStatic<FileSystemUtils> mockedFileUtils = mockStatic(FileSystemUtils.class)) {
            mockedFileUtils.when(() -> FileSystemUtils.deleteRecursively(rootLocation)).thenReturn(true);
            fileService.deleteAll();
            mockedFileUtils.verify(() -> FileSystemUtils.deleteRecursively(rootLocation));
        }
    }
}