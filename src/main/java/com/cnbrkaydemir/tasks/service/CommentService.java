package com.cnbrkaydemir.tasks.service;

import com.cnbrkaydemir.tasks.dto.CommentDto;
import com.cnbrkaydemir.tasks.model.Comment;

import java.util.List;
import java.util.UUID;

public interface CommentService {
    CommentDto createComment(Comment comment);
    CommentDto getComment(UUID id);
    CommentDto updateComment(UUID id, CommentDto comment);
    boolean deleteComment(UUID id);
    List<CommentDto> getAllComments();
}
