package com.cnbrkaydemir.tasks.exception.notfound;

import java.util.UUID;

public class CommentNotFoundException extends NotFoundException {
    public CommentNotFoundException(UUID commentId) {
        super("Comment with id " + commentId + " not found");
    }
}
