package com.cnbrkaydemir.tasks.repository;

import com.cnbrkaydemir.tasks.model.Comment;
import com.cnbrkaydemir.tasks.model.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommentRepository extends BaseRepository<Comment, UUID> {
    @Query("Select t FROM Task t JOIN t.comments c WHERE t.id = :commentId AND "
            + "t.deleted = false AND c.deleted = false")
    Task findActiveTaskByCommentId(@Param("commentId") UUID commentId);
}
