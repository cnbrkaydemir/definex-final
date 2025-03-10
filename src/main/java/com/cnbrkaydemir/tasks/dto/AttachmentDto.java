package com.cnbrkaydemir.tasks.dto;

import com.cnbrkaydemir.tasks.model.AttachmentType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper=false)
public class AttachmentDto extends BaseDto{

    private UUID id;

    private String name;

    private String path;

    private AttachmentType type;

    private Date createdDate;
}
