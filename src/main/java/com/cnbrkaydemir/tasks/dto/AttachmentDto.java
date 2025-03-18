package com.cnbrkaydemir.tasks.dto;

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

    private String type;

    private Date createdDate;
}
