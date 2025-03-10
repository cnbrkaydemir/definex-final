package com.cnbrkaydemir.tasks.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper=false)
public class DepartmentDto {

    private UUID id;

    private String name;

    private String description;

}
