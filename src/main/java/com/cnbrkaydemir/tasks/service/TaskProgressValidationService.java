package com.cnbrkaydemir.tasks.service;

import com.cnbrkaydemir.tasks.dto.UpdateTaskProgressDto;
import com.cnbrkaydemir.tasks.model.TaskProgress;

public interface TaskProgressValidationService {
    TaskProgress validateTransition(TaskProgress oldProgress, TaskProgress newProgress);
    void validateReason(TaskProgress taskProgress, String reason);
}
