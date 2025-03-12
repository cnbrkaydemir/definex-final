package com.cnbrkaydemir.tasks.repository;

import com.cnbrkaydemir.tasks.model.Attachment;
import com.cnbrkaydemir.tasks.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AttachmentRepository extends BaseRepository<Attachment, UUID> {

    @Query("Select t FROM Task t JOIN t.attachments a WHERE t.id = :attachmentId AND "
    + "t.deleted = false AND a.deleted = false")
    Task findActiveTaskByAttachmentId(@Param("attachmentId") UUID attachmentId);
}
