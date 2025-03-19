package com.cnbrkaydemir.tasks.factory;

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
}
