package com.cnbrkaydemir.tasks.dto;

import com.cnbrkaydemir.tasks.model.ProjectStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper=false)
public class ProjectDto extends BaseDto {

    private UUID id;

    private String name;

    private String description;

    private Date startDate;

    private Date dueDate;

    private ProjectStatus status;
}
