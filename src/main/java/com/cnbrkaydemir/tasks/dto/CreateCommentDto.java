package com.cnbrkaydemir.tasks.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper=false)
public class CreateCommentDto {
    private UUID id;

    private String title;

    private String description;

    private Date createdDate;

    private UUID userId;

    private UUID taskId;
}
