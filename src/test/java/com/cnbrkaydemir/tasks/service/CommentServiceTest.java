package com.cnbrkaydemir.tasks.service;

import com.cnbrkaydemir.tasks.dto.CommentDto;
import com.cnbrkaydemir.tasks.dto.CreateCommentDto;
import com.cnbrkaydemir.tasks.exception.notfound.CommentNotFoundException;
import com.cnbrkaydemir.tasks.exception.notfound.TaskNotFoundException;
import com.cnbrkaydemir.tasks.exception.notfound.UserNotFoundException;
import com.cnbrkaydemir.tasks.factory.CommentTestDataFactory;
import com.cnbrkaydemir.tasks.factory.TaskTestDataFactory;
import com.cnbrkaydemir.tasks.factory.UsersTestDataFactory;
import com.cnbrkaydemir.tasks.model.Comment;
import com.cnbrkaydemir.tasks.model.Task;
import com.cnbrkaydemir.tasks.model.Users;
import com.cnbrkaydemir.tasks.repository.CommentRepository;
import com.cnbrkaydemir.tasks.repository.TaskRepository;
import com.cnbrkaydemir.tasks.repository.UsersRepository;
import com.cnbrkaydemir.tasks.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CommentServiceTest {

    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private ModelMapper modelMapper;


    @BeforeAll
    void setUp() {
        initMocks(this);
    }

    @Test
    void testGetComments() {
        Comment comment = CommentTestDataFactory.createComment();
        CommentDto commentDto = CommentTestDataFactory.dtoFromComment(comment);
        List<Comment> commentList = List.of(comment);

        when(commentRepository.findAll()).thenReturn(commentList);
        when(modelMapper.map(comment, CommentDto.class)).thenReturn(commentDto);

        List<CommentDto> retrievedComments = commentService.getAllComments();

        assertNotNull(retrievedComments);
        assertTrue(retrievedComments.size() > 0);
        verify(commentRepository, times(1)).findAll();
    }

    @Test
    void testGetComment() {
        Comment comment = CommentTestDataFactory.createComment();
        CommentDto commentDto = CommentTestDataFactory.dtoFromComment(comment);

        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
        when(modelMapper.map(comment, CommentDto.class)).thenReturn(commentDto);

        CommentDto retrievedComment = commentService.getComment(comment.getId());

        assertNotNull(retrievedComment);
        assertEquals(comment.getId(), retrievedComment.getId());
        assertEquals(comment.getTitle(), retrievedComment.getTitle());
        verify(commentRepository, times(1)).findById(comment.getId());
    }

    @Test
    void TestGetCommentInvalidId() {
        Comment comment = CommentTestDataFactory.createComment();

        when(commentRepository.findById(comment.getId())).thenReturn(Optional.empty());
        assertThrows(CommentNotFoundException.class, () -> commentService.getComment(comment.getId()));
    }

    @Test
    void testCreateComment() {
        Users user = UsersTestDataFactory.createDefaultUserWithoutTask();
        Task task = TaskTestDataFactory.createDefaultTask();

        Comment comment = CommentTestDataFactory.createComment();
        CommentDto commentDto = CommentTestDataFactory.dtoFromComment(comment);
        CreateCommentDto createCommentDto = new CreateCommentDto();
        createCommentDto.setUserId(user.getId());
        createCommentDto.setTaskId(task.getId());

        when(usersRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(commentRepository.save(comment)).thenReturn(comment);
        when(modelMapper.map(comment, CommentDto.class)).thenReturn(commentDto);
        when(modelMapper.map(createCommentDto, Comment.class)).thenReturn(comment);

        CommentDto createdComment = commentService.createComment(createCommentDto);
        assertNotNull(createdComment);
        assertEquals(comment.getId(), createdComment.getId());
        assertEquals(comment.getTitle(), createdComment.getTitle());
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void testCreateCommentInvalidUser() {
        UUID userId = UUID.randomUUID();
        Task task = TaskTestDataFactory.createDefaultTask();

        Comment comment = CommentTestDataFactory.createComment();
        CommentDto commentDto = CommentTestDataFactory.dtoFromComment(comment);
        CreateCommentDto createCommentDto = new CreateCommentDto();
        createCommentDto.setUserId(userId);
        createCommentDto.setTaskId(task.getId());

        when(usersRepository.findById(userId)).thenReturn(Optional.empty());
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(commentRepository.save(comment)).thenReturn(comment);
        when(modelMapper.map(comment, CommentDto.class)).thenReturn(commentDto);
        when(modelMapper.map(createCommentDto, Comment.class)).thenReturn(comment);

        assertThrows(UserNotFoundException.class, () -> commentService.createComment(createCommentDto));
    }

    @Test
    void testCreateCommentInvalidTask() {
        Users user = UsersTestDataFactory.createDefaultUserWithoutTask();
        UUID taskId = UUID.randomUUID();

        Comment comment = CommentTestDataFactory.createComment();
        CommentDto commentDto = CommentTestDataFactory.dtoFromComment(comment);
        CreateCommentDto createCommentDto = new CreateCommentDto();
        createCommentDto.setUserId(user.getId());
        createCommentDto.setTaskId(taskId);

        when(usersRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());
        when(commentRepository.save(comment)).thenReturn(comment);
        when(modelMapper.map(comment, CommentDto.class)).thenReturn(commentDto);
        when(modelMapper.map(createCommentDto, Comment.class)).thenReturn(comment);

        assertThrows(TaskNotFoundException.class, () -> commentService.createComment(createCommentDto));
    }


    @Test
    void testUpdateComment() {
        Comment comment = CommentTestDataFactory.createComment();
        CommentDto commentDto = CommentTestDataFactory.dtoFromComment(comment);

        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
        when(modelMapper.map(comment, CommentDto.class)).thenReturn(commentDto);
        when(commentRepository.save(comment)).thenReturn(comment);

        CommentDto updatedComment = commentService.updateComment(comment.getId(), commentDto);

        assertNotNull(updatedComment);
        assertEquals(comment.getId(), updatedComment.getId());
        assertEquals(comment.getTitle(), updatedComment.getTitle());
    }

    @Test
    void testUpdateCommentInvalidId() {
        UUID randomId = UUID.randomUUID();
        Comment comment = CommentTestDataFactory.createComment();
        CommentDto commentDto = CommentTestDataFactory.dtoFromComment(comment);

        when(commentRepository.findById(randomId)).thenReturn(Optional.empty());
        assertThrows(CommentNotFoundException.class, () -> commentService.updateComment(randomId, commentDto));
    }

    @Test
    void testDeleteComment() {
        Comment comment = CommentTestDataFactory.createComment();

        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
        when(commentRepository.save(comment)).thenReturn(comment);

        boolean isDeleted = commentService.deleteComment(comment.getId());

        assertTrue(isDeleted);
        assertTrue(comment.isDeleted());
    }

    @Test
    void testDeleteCommentInvalidId() {
        UUID randomId = UUID.randomUUID();

        when(commentRepository.findById(randomId)).thenReturn(Optional.empty());
        assertThrows(CommentNotFoundException.class, () -> commentService.deleteComment(randomId));
    }


}
