package com.cnbrkaydemir.tasks.dto;

import lombok.Data;

@Data
public class LoginDto extends BaseDto{
    private String username;

    private String password;
}
