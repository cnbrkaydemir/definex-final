package com.cnbrkaydemir.tasks.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper=false)
public class CommentDto {

    private UUID id;

    private String title;

    private String description;

    private Date createdDate;
}
