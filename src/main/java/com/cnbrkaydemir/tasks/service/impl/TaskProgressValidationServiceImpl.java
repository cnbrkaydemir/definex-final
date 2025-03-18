package com.cnbrkaydemir.tasks.service.impl;

import com.cnbrkaydemir.tasks.dto.UpdateTaskProgressDto;
import com.cnbrkaydemir.tasks.exception.state.InvalidProgressTransitionException;
import com.cnbrkaydemir.tasks.exception.state.ReasonCannotBeEmptyException;
import com.cnbrkaydemir.tasks.model.TaskProgress;
import com.cnbrkaydemir.tasks.service.TaskProgressValidationService;
import org.springframework.stereotype.Service;


import java.util.Map;
import java.util.Set;

@Service
public class TaskProgressValidationServiceImpl implements TaskProgressValidationService {

    private final Map<TaskProgress, Set<TaskProgress>> taskProgressMap = Map.of(
            TaskProgress.IN_PROGRESS, Set.of(TaskProgress.COMPLETED, TaskProgress.CANCELLED, TaskProgress.BLOCKED),
            TaskProgress.BACKLOG, Set.of(TaskProgress.IN_ANALYSIS, TaskProgress.CANCELLED, TaskProgress.IN_PROGRESS),
            TaskProgress.BLOCKED, Set.of(TaskProgress.CANCELLED, TaskProgress.IN_ANALYSIS, TaskProgress.IN_PROGRESS),
            TaskProgress.CANCELLED, Set.of(),
            TaskProgress.COMPLETED, Set.of(),
            TaskProgress.IN_ANALYSIS, Set.of(TaskProgress.IN_PROGRESS, TaskProgress.CANCELLED, TaskProgress.BLOCKED, TaskProgress.BACKLOG)
    );

    @Override
    public TaskProgress validateTransition(TaskProgress oldProgress, TaskProgress newProgress) {
        Set<TaskProgress> taskProgressSet = taskProgressMap.get(oldProgress);
        if (!taskProgressSet.contains(newProgress)) {
            throw new InvalidProgressTransitionException("Transition from " + oldProgress + " to " + newProgress+" is invalid !");
        }
        return newProgress;
    }

    @Override
    public void validateReason(TaskProgress taskProgress, String reason) {
        if (reason == null &&
                (taskProgress == TaskProgress.BLOCKED || taskProgress == TaskProgress.CANCELLED)) {
            throw new ReasonCannotBeEmptyException("Progress state "+taskProgress+ " requires a reason.");
        }
    }
}
