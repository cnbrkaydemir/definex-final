package com.cnbrkaydemir.tasks.service;

import com.cnbrkaydemir.tasks.dto.CommentDto;
import com.cnbrkaydemir.tasks.exception.notfound.CommentNotFoundException;
import com.cnbrkaydemir.tasks.model.Comment;

import java.util.List;
import java.util.UUID;

public interface CommentService {
    CommentDto createComment(Comment comment);
    CommentDto getComment(UUID id) throws CommentNotFoundException;
    CommentDto updateComment(UUID id, CommentDto comment) throws CommentNotFoundException;
    boolean deleteComment(UUID id) throws CommentNotFoundException;
    List<CommentDto> getAllComments();
}
