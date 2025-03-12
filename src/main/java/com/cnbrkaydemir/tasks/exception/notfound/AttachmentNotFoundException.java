package com.cnbrkaydemir.tasks.exception.notfound;

import java.util.UUID;

public class AttachmentNotFoundException extends NotFoundException {
    public AttachmentNotFoundException(UUID attachmentId) {
        super("Attachment with id " + attachmentId + " not found");
    }
}
