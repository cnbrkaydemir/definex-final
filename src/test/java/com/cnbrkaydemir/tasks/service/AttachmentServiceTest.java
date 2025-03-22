package com.cnbrkaydemir.tasks.service;

import com.cnbrkaydemir.tasks.dto.AttachmentDto;
import com.cnbrkaydemir.tasks.exception.notfound.AttachmentNotFoundException;
import com.cnbrkaydemir.tasks.exception.notfound.TaskNotFoundException;
import com.cnbrkaydemir.tasks.factory.AttachmentTestDataFactory;
import com.cnbrkaydemir.tasks.model.Attachment;
import com.cnbrkaydemir.tasks.model.Task;
import com.cnbrkaydemir.tasks.repository.AttachmentRepository;
import com.cnbrkaydemir.tasks.repository.TaskRepository;
import com.cnbrkaydemir.tasks.service.impl.AttachmentServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AttachmentServiceTest {

    @InjectMocks
    private AttachmentServiceImpl attachmentService;

    @Mock
    private AttachmentRepository attachmentRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private FileService fileService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private MultipartFile multipartFile;

    @Mock
    private Resource resource;

    @BeforeAll
    void setup() {
        initMocks(this);
    }

    @Test
    void testGetAllAttachments() {
        Attachment attachment = AttachmentTestDataFactory.createAttachment();
        AttachmentDto attachmentDto = AttachmentTestDataFactory.dtoFromAttachment(attachment);
        List<Attachment> attachments = List.of(attachment);

        when(attachmentRepository.findAll()).thenReturn(attachments);
        when(modelMapper.map(attachment, AttachmentDto.class)).thenReturn(attachmentDto);

        List<AttachmentDto> attachmentDtos = attachmentService.getAllAttachments();

        assertNotNull(attachmentDtos);
        assertFalse(attachmentDtos.isEmpty());
        verify(attachmentRepository).findAll();
    }

    @Test
    void testGetAttachmentAsResource() {
        UUID id = UUID.randomUUID();
        Attachment attachment = AttachmentTestDataFactory.createAttachment();

        when(attachmentRepository.findById(id)).thenReturn(Optional.of(attachment));
        when(fileService.loadAsResource(attachment.getName())).thenReturn(resource);

        Resource result = attachmentService.getAttachmentAsResource(id);

        assertNotNull(result);
        verify(attachmentRepository).findById(id);
        verify(fileService).loadAsResource(attachment.getName());
    }

    @Test
    void testGetAttachmentAsResourceNotFound() {
        UUID id = UUID.randomUUID();

        when(attachmentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(AttachmentNotFoundException.class, () -> attachmentService.getAttachmentAsResource(id));
        verify(attachmentRepository).findById(id);
    }

    @Test
    void testGetAttachment() {
        UUID id = UUID.randomUUID();
        Attachment attachment = AttachmentTestDataFactory.createAttachment();
        AttachmentDto attachmentDto = AttachmentTestDataFactory.dtoFromAttachment(attachment);
        Optional<Attachment> optionalAttachment = Optional.of(attachment);

        when(attachmentRepository.findById(id)).thenReturn(optionalAttachment);
        when(modelMapper.map(optionalAttachment, AttachmentDto.class)).thenReturn(attachmentDto);

        AttachmentDto result = attachmentService.getAttachment(id);

        assertNotNull(result);
        verify(attachmentRepository).findById(id);
    }

    @Test
    void testGetAllAttachmentsAsResource() {
        Attachment attachment = AttachmentTestDataFactory.createAttachment();
        List<Attachment> attachments = List.of(attachment);

        when(attachmentRepository.findAll()).thenReturn(attachments);
        when(fileService.loadAsResource(attachment.getName())).thenReturn(resource);

        List<Resource> resources = attachmentService.getAllAttachmentsAsResource();

        assertNotNull(resources);
        assertFalse(resources.isEmpty());
    }

    @Test
    void testDeleteAttachment() {
        UUID id = UUID.randomUUID();
        Attachment attachment = AttachmentTestDataFactory.createAttachment();

        when(attachmentRepository.findById(id)).thenReturn(Optional.of(attachment));

        boolean result = attachmentService.deleteAttachment(id);

        assertTrue(result);
        verify(attachmentRepository).findById(id);
        verify(fileService).delete(attachment.getName());
        verify(attachmentRepository).save(attachment);
        assertTrue(attachment.isDeleted());
    }

    @Test
    void testDeleteAttachmentNotFound() {
        UUID id = UUID.randomUUID();

        when(attachmentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(AttachmentNotFoundException.class, () -> attachmentService.deleteAttachment(id));
        verify(attachmentRepository).findById(id);
    }

    @Test
    void testCreateAttachment() {
        UUID taskId = UUID.randomUUID();
        Task task = new Task();
        Attachment attachment = new Attachment();
        attachment.setName("test.txt");
        AttachmentDto attachmentDto = new AttachmentDto();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(multipartFile.getOriginalFilename()).thenReturn("test.txt");
        when(multipartFile.getContentType()).thenReturn("text/plain");
        when(attachmentRepository.save(any(Attachment.class))).thenReturn(attachment);
        when(modelMapper.map(attachment, AttachmentDto.class)).thenReturn(attachmentDto);

        AttachmentDto result = attachmentService.createAttachment(taskId, multipartFile);

        assertNotNull(result);
        verify(taskRepository).findById(taskId);
        verify(fileService).store(multipartFile);
        verify(attachmentRepository).save(any(Attachment.class));
    }

    @Test
    void testCreateAttachmentTaskNotFound() {
        UUID taskId = UUID.randomUUID();

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> attachmentService.createAttachment(taskId, multipartFile));
        verify(taskRepository).findById(taskId);
    }

    @Test
    void testCreateAttachmentFromFile() {
        when(multipartFile.getOriginalFilename()).thenReturn("test.txt");
        when(multipartFile.getContentType()).thenReturn("text/plain");

        Attachment attachment = attachmentService.createAttachmentFromFile(multipartFile);

        assertNotNull(attachment);
        assertEquals("test.txt", attachment.getName());
        assertEquals("text/plain", attachment.getType());
        assertEquals("./uploads/test.txt", attachment.getPath());
        assertFalse(attachment.isDeleted());
        assertNotNull(attachment.getCreatedDate());
    }

}
