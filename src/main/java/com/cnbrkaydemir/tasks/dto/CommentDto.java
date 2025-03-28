package com.cnbrkaydemir.tasks.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper=false)
public class CommentDto extends BaseDto{

    private UUID id;

    private String title;

    private String description;

    private Date createdDate;
}
