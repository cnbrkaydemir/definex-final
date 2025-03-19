package com.cnbrkaydemir.tasks.dto;

import com.cnbrkaydemir.tasks.model.UserRole;
import lombok.Data;

import java.util.UUID;

@Data
public class UserTokenDto extends BaseDto {

    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private UserRole role;

    private String token;

    public static UserTokenDto of(UserDto userDto, String token) {
        UserTokenDto userTokenDto = new UserTokenDto();

        userTokenDto.setId(userDto.getId());
        userTokenDto.setFirstName(userDto.getFirstName());
        userTokenDto.setLastName(userDto.getLastName());
        userTokenDto.setEmail(userDto.getEmail());
        userTokenDto.setPhoneNumber(userDto.getPhoneNumber());
        userTokenDto.setRole(userDto.getRole());
        userTokenDto.setToken(token);
        return userTokenDto;
    }
}