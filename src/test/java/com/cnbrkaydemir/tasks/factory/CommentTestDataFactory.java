package com.cnbrkaydemir.tasks.factory;

import com.cnbrkaydemir.tasks.dto.CommentDto;
import com.cnbrkaydemir.tasks.model.Comment;

import java.util.UUID;

public class CommentTestDataFactory {
    public static Comment createComment() {
        Comment comment = new Comment();
        comment.setId(UUID.randomUUID());
        comment.setCommentedBy(UsersTestDataFactory.createDefaultUser());
        comment.setTask(TaskTestDataFactory.createDefaultTask());
        comment.setTitle("New Comment 1");
        comment.setDescription("Please Solve it ASAP");
        return comment;
    }

    public static CommentDto dtoFromComment(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setTitle(comment.getTitle());
        commentDto.setDescription(comment.getDescription());
        return commentDto;
    }
}
