package com.cnbrkaydemir.tasks.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper=false)
public class CreateTeamDto extends BaseDto{
    private UUID id;

    private String name;

    private String goal;

    private UUID department;
}
