package com.cnbrkaydemir.tasks.dto;

import com.cnbrkaydemir.tasks.model.UserRole;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Data
public class UserDto extends BaseDto {
    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private UserRole role;
}
