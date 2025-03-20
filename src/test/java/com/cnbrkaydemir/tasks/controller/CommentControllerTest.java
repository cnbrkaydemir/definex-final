package com.cnbrkaydemir.tasks.controller;

import com.cnbrkaydemir.tasks.dto.CommentDto;
import com.cnbrkaydemir.tasks.dto.CreateCommentDto;
import com.cnbrkaydemir.tasks.exception.GlobalExceptionHandler;
import com.cnbrkaydemir.tasks.exception.notfound.CommentNotFoundException;
import com.cnbrkaydemir.tasks.factory.CommentTestDataFactory;
import com.cnbrkaydemir.tasks.model.Comment;
import com.cnbrkaydemir.tasks.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommentControllerTest {

    private final String BASE_PATH = "/api/comment";

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private CommentController commentController;

    @Mock
    private CommentService commentService;

    private ObjectMapper objectMapper;

    private ModelMapper modelMapper;

    private Comment comment;

    private CommentDto commentDto;

    private CreateCommentDto createCommentDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(commentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        modelMapper = new ModelMapper();

        comment = CommentTestDataFactory.createComment();
        commentDto = modelMapper.map(comment, CommentDto.class);
        createCommentDto = modelMapper.map(commentDto, CreateCommentDto.class);
        createCommentDto.setUserId(comment.getCommentedBy().getId());

        objectMapper = new ObjectMapper();
    }

    @Test
    void findAllComments_ShouldReturnOk() throws Exception {
        when(commentService.getAllComments()).thenReturn(List.of(commentDto));

        String apiPath = BASE_PATH + "/v1";
        mockMvc.perform(get(apiPath))
                .andExpect(status().isOk());
    }

    @Test
    void findCommentById_ShouldReturnOk() throws Exception {
        when(commentService.getComment(comment.getId())).thenReturn(commentDto);

        String apiPath = BASE_PATH + "/v1/"+comment.getId();
        mockMvc.perform(get(apiPath))
                .andExpect(status().isOk());
    }

    @Test
    void findCommentById_ShouldReturnNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(commentService.getComment(randomId))
                .thenThrow(new CommentNotFoundException(randomId));

        String apiPath = BASE_PATH + "/v1/" + randomId.toString();
        mockMvc.perform(get(apiPath))
                .andExpect(status().isNotFound());
    }

    @Test
    void createComment_ShouldReturnCreated() throws Exception {
        when(commentService.createComment(createCommentDto)).thenReturn(commentDto);

        String apiPath = BASE_PATH + "/v1";
        mockMvc.perform(post(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCommentDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void updateComment_ShouldReturnOk() throws Exception {
        commentDto.setId(UUID.randomUUID());
        commentDto.setTitle("Defect is still existent");
        when(commentService.updateComment(commentDto.getId(), commentDto)).thenReturn(commentDto);

        String apiPath = BASE_PATH + "/v1/"+commentDto.getId();
        mockMvc.perform(patch(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk());
    }

    @Test
    void updateComment_ShouldReturnNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(commentService.updateComment(randomId, commentDto))
                .thenThrow(new CommentNotFoundException(randomId));

        String apiPath = BASE_PATH + "/v1/"+randomId.toString();
        mockMvc.perform(patch(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteComment_ShouldReturnNoContent() throws Exception {
        when(commentService.deleteComment(comment.getId())).thenReturn(true);

        String apiPath = BASE_PATH + "/v1/"+comment.getId();
        mockMvc.perform(delete(apiPath))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteComment_ShouldReturnNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(commentService.deleteComment(randomId))
                .thenThrow(new CommentNotFoundException(randomId));

        String apiPath = BASE_PATH + "/v1/"+randomId.toString();
        mockMvc.perform(delete(apiPath))
                .andExpect(status().isNotFound());
    }

}
