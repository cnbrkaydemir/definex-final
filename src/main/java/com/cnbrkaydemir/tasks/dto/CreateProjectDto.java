package com.cnbrkaydemir.tasks.dto;

import com.cnbrkaydemir.tasks.model.ProjectStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper=false)
public class CreateProjectDto {
    private UUID id;

    private String name;

    private String description;

    private Date startDate;

    private Date dueDate;

    private ProjectStatus status;

    private UUID departmentId;
}
