package com.cnbrkaydemir.tasks.dto;

import com.cnbrkaydemir.tasks.model.TaskPriority;
import com.cnbrkaydemir.tasks.model.TaskProgress;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper=false)
public class TaskDto extends BaseDto{
    private UUID id;

    private String name;

    private String description;

    private TaskPriority priority;

    private TaskProgress progress;

    private String reason;

}
