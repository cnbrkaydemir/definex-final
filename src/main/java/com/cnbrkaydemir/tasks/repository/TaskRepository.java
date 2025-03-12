package com.cnbrkaydemir.tasks.repository;

import com.cnbrkaydemir.tasks.model.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends BaseRepository<Task, UUID> {

    @Query("SELECT u from Users u JOIN u.tasks t WHERE t.id = :taskId AND " +
            "t.assignee.deleted = false AND u.deleted = false")
    Users findActiveUserByTaskId(@Param("taskId") UUID taskId);

    @Query("SELECT p from Project p JOIN p.tasks t WHERE t.id = :taskId AND " +
            "t.project.deleted = false AND p.deleted = false")
    Project findActiveProjectByTaskId(@Param("taskId") UUID taskId);

    @Query("SELECT a from Attachment a where a.task.id = :taskId AND " +
            "a.deleted = false AND a.task.deleted = false")
    List<Attachment> findActiveAttachmentsByTaskId(@Param("taskId") UUID taskId);

    @Query("SELECT c from Comment c where c.task.id = :taskId AND " +
            "c.deleted = false AND c.task.deleted = false")
    List<Comment> findActiveCommentsByTaskId(@Param("taskId") UUID taskId);

}
