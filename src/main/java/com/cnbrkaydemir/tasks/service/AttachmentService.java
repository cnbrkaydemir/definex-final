package com.cnbrkaydemir.tasks.service;

import com.cnbrkaydemir.tasks.dto.AttachmentDto;
import com.cnbrkaydemir.tasks.exception.notfound.AttachmentNotFoundException;
import com.cnbrkaydemir.tasks.model.Attachment;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface AttachmentService {
    Resource getAttachment(UUID id) throws AttachmentNotFoundException;
    List<Resource> getAllAttachments();
    boolean deleteAttachment(UUID id) throws AttachmentNotFoundException;
    AttachmentDto createAttachment(UUID taskId,MultipartFile file);
}
