package com.cnbrkaydemir.tasks.factory;

import com.cnbrkaydemir.tasks.model.*;

import java.util.List;
import java.util.UUID;

public class UsersTestDataFactory {
    public static Users createDefaultUser(){
        Users user = new Users();
        user.setId(UUID.randomUUID());
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@doe.com");
        user.setPassword("password");
        Task userTask = TaskTestDataFactory.createCustomTask("Task 1", "Desc", TaskProgress.IN_PROGRESS, TaskPriority.LOW, user);
        user.setTasks(List.of(userTask));
        user.setPhoneNumber("+90 0000000");
        user.setRole(UserRole.ADMIN);
        return user;
    }

    public static Users createCustomUser(String firstName, String lastName, String email, String password){
        Users user = new Users();
        user.setId(UUID.randomUUID());
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        Task userTask = TaskTestDataFactory.createCustomTask("Task 1", "Desc", TaskProgress.IN_PROGRESS, TaskPriority.LOW, user);
        user.setTasks(List.of(userTask));
        user.setPhoneNumber("+90 0000000");
        user.setRole(UserRole.ADMIN);
        return user;
    }

}
