package com.cnbrkaydemir.tasks.dto;

import com.cnbrkaydemir.tasks.model.TaskProgress;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UpdateTaskProgressDto extends BaseDto{

    private TaskProgress progress;

    private String reason;
}
