package com.cnbrkaydemir.tasks.service;

import com.cnbrkaydemir.tasks.dto.AttachmentDto;
import com.cnbrkaydemir.tasks.model.Attachment;

import java.util.List;
import java.util.UUID;

public interface AttachmentService {
    AttachmentDto getAttachment(UUID id);
    List<AttachmentDto> getAllAttachments();
    boolean deleteAttachment(UUID id);
    AttachmentDto createAttachment(Attachment attachment);
    AttachmentDto updateAttachment(UUID id, AttachmentDto attachment);
}
