package com.cnbrkaydemir.tasks.service;

import com.cnbrkaydemir.tasks.exception.state.InvalidProgressTransitionException;
import com.cnbrkaydemir.tasks.exception.state.ReasonCannotBeEmptyException;
import com.cnbrkaydemir.tasks.model.TaskProgress;
import com.cnbrkaydemir.tasks.service.impl.TaskProgressValidationServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.MockitoAnnotations.initMocks;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TaskProgressValidationServiceTest {

    @InjectMocks
    private TaskProgressValidationServiceImpl taskProgressValidationService;

    @BeforeAll
    void setUp() {
        initMocks(this);
    }


    @Test
    void testValidTransition() {
        assertEquals(TaskProgress.COMPLETED, taskProgressValidationService.validateTransition(TaskProgress.IN_PROGRESS, TaskProgress.COMPLETED));
        assertEquals(TaskProgress.IN_PROGRESS, taskProgressValidationService.validateTransition(TaskProgress.IN_ANALYSIS, TaskProgress.IN_PROGRESS));
    }

    @Test
    void testInvalidTransition() {
        assertThrows(InvalidProgressTransitionException.class, () ->
                taskProgressValidationService.validateTransition(TaskProgress.COMPLETED, TaskProgress.IN_PROGRESS));

        assertThrows(InvalidProgressTransitionException.class, () ->
                taskProgressValidationService.validateTransition(TaskProgress.CANCELLED, TaskProgress.IN_PROGRESS));
    }

    @Test
    void testValidateReasonWithReason() {
        assertDoesNotThrow(() -> taskProgressValidationService.validateReason(TaskProgress.BLOCKED, "Blocked due to issue"));
        assertDoesNotThrow(() -> taskProgressValidationService.validateReason(TaskProgress.CANCELLED, "Cancelled by user"));
    }

    @Test
    void testValidateReasonWithoutReason() {
        assertThrows(ReasonCannotBeEmptyException.class, () ->
                taskProgressValidationService.validateReason(TaskProgress.BLOCKED, null));

        assertThrows(ReasonCannotBeEmptyException.class, () ->
                taskProgressValidationService.validateReason(TaskProgress.CANCELLED, null));
    }
}
