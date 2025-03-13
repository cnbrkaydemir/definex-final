package com.cnbrkaydemir.tasks.dto;

import com.cnbrkaydemir.tasks.model.Task;
import com.cnbrkaydemir.tasks.model.TaskPriority;
import com.cnbrkaydemir.tasks.model.TaskProgress;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper=false)
public class CreateTaskDto extends BaseDto{
    private UUID id;

    private String name;

    private String description;

    private TaskPriority priority;

    private TaskProgress progress;

    private String reason;

    private UUID userId;

    private UUID projectId;

    public static Task convertToTask(CreateTaskDto dto){
        return Task.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .priority(dto.getPriority())
                .progress(dto.getProgress())
                .reason(dto.getReason())
                .build();
    }

}
