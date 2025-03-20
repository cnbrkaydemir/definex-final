package com.cnbrkaydemir.tasks.controller;

import com.cnbrkaydemir.tasks.dto.AttachmentDto;
import com.cnbrkaydemir.tasks.exception.GlobalExceptionHandler;
import com.cnbrkaydemir.tasks.exception.notfound.AttachmentNotFoundException;
import com.cnbrkaydemir.tasks.factory.AttachmentTestDataFactory;
import com.cnbrkaydemir.tasks.model.Attachment;
import com.cnbrkaydemir.tasks.service.AttachmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AttachmentControllerTest {

    private final String BASE_PATH = "/api/attachment";

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private AttachmentController attachmentController;

    @Mock
    private AttachmentService attachmentService;

    private ObjectMapper objectMapper;

    private ModelMapper modelMapper;

    private Attachment attachment;

    private AttachmentDto attachmentDto;

    private final MultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Hello World".getBytes());

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(attachmentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        modelMapper = new ModelMapper();

        attachment = AttachmentTestDataFactory.createAttachment();
        attachmentDto = modelMapper.map(attachment, AttachmentDto.class);

        objectMapper = new ObjectMapper();
    }

    @Test
    void findAllAttachments_ShouldReturnOk() throws Exception {
        when(attachmentService.getAllAttachments()).thenReturn(List.of(attachmentDto));

        String apiPath = BASE_PATH + "/v1";
        mockMvc.perform(get(apiPath))
                .andExpect(status().isOk());
    }

    @Test
    void findAttachmentById_ShouldReturnOk() throws Exception {
        when(attachmentService.getAttachment(attachment.getId())).thenReturn(attachmentDto);

        String apiPath = BASE_PATH + "/v1/"+attachment.getId();
        mockMvc.perform(get(apiPath))
                .andExpect(status().isOk());
    }

    @Test
    void findAttachmentById_ShouldReturnNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(attachmentService.getAttachment(randomId))
                .thenThrow(new AttachmentNotFoundException(randomId));

        String apiPath = BASE_PATH + "/v1/" + randomId.toString();
        mockMvc.perform(get(apiPath))
                .andExpect(status().isNotFound());
    }

    @Test
    void createAttachment_ShouldReturnOk() throws Exception {
        when(attachmentService.createAttachment(attachment.getTask().getId(), file)).thenReturn(attachmentDto);

        String apiPath = BASE_PATH + "/v1/"+attachment.getTask().getId();
        mockMvc.perform(post(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(file.getBytes()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteAttachment_ShouldReturnNoContent() throws Exception {
        when(attachmentService.deleteAttachment(attachment.getId())).thenReturn(true);

        String apiPath = BASE_PATH + "/v1/"+attachment.getId();
        mockMvc.perform(delete(apiPath))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteAttachment_ShouldReturnNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(attachmentService.deleteAttachment(randomId))
                .thenThrow(new AttachmentNotFoundException(randomId));

        String apiPath = BASE_PATH + "/v1/"+randomId.toString();
        mockMvc.perform(delete(apiPath))
                .andExpect(status().isNotFound());
    }


}
