package com.cnbrkaydemir.tasks.service;

import com.cnbrkaydemir.tasks.dto.AttachmentDto;
import com.cnbrkaydemir.tasks.exception.notfound.AttachmentNotFoundException;
import com.cnbrkaydemir.tasks.model.Attachment;

import java.util.List;
import java.util.UUID;

public interface AttachmentService {
    AttachmentDto getAttachment(UUID id) throws AttachmentNotFoundException;
    List<AttachmentDto> getAllAttachments();
    boolean deleteAttachment(UUID id) throws AttachmentNotFoundException;
    AttachmentDto createAttachment(AttachmentDto attachmentDto);
    AttachmentDto updateAttachment(UUID id, AttachmentDto attachment) throws AttachmentNotFoundException;
}
