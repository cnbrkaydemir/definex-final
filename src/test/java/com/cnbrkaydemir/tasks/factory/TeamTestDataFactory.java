package com.cnbrkaydemir.tasks.factory;

import com.cnbrkaydemir.tasks.dto.TeamDto;
import com.cnbrkaydemir.tasks.model.Team;

import java.util.UUID;

public class TeamTestDataFactory {
    public static Team createDefaultTeam() {
        Team team = new Team();
        team.setId(UUID.randomUUID());
        team.setName("Default Team");
        team.setGoal("Doing Default Things");
        team.setProject(ProjectTestDataFactory.createDefaultProject());
        team.setDepartment(DepartmentTestDataFactory.createDefaultDepartment());
        return team;
    }

    public static Team createCustomTeam(String teamName, String teamGoal) {
        Team team = new Team();
        team.setId(UUID.randomUUID());
        team.setName(teamName);
        team.setGoal(teamGoal);
        team.setProject(ProjectTestDataFactory.createDefaultProject());
        team.setDepartment(DepartmentTestDataFactory.createDefaultDepartment());
        return team;
    }

    public static TeamDto createDtoFromTeam(Team team) {
        TeamDto teamDto = new TeamDto();
        teamDto.setId(team.getId());
        teamDto.setName(team.getName());
        teamDto.setGoal(team.getGoal());
        return teamDto;
    }
}
