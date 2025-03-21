package com.cnbrkaydemir.tasks.factory;

import com.cnbrkaydemir.tasks.dto.AttachmentDto;
import com.cnbrkaydemir.tasks.model.Attachment;

import java.util.Date;
import java.util.UUID;

public class AttachmentTestDataFactory {
    public static Attachment createAttachment() {
        Attachment attachment = new Attachment();
        attachment.setTask(TaskTestDataFactory.createDefaultTask());
        attachment.setId(UUID.randomUUID());
        attachment.setName("ScreenShot 1");
        attachment.setCreatedDate(new Date());
        attachment.setType("image/jpeg");
        return attachment;
    }

    public static AttachmentDto dtoFromAttachment(Attachment attachment) {
        AttachmentDto attachmentDto = new AttachmentDto();
        attachmentDto.setId(attachment.getId());
        attachmentDto.setName(attachment.getName());
        attachmentDto.setCreatedDate(attachment.getCreatedDate());
        attachmentDto.setType(attachment.getType());
        return attachmentDto;
    }
}
