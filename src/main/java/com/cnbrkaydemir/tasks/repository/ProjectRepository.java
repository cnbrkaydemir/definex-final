package com.cnbrkaydemir.tasks.repository;

import com.cnbrkaydemir.tasks.model.Department;
import com.cnbrkaydemir.tasks.model.Project;
import com.cnbrkaydemir.tasks.model.Task;
import com.cnbrkaydemir.tasks.model.Team;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectRepository extends BaseRepository<Project, UUID> {

    @Query("SELECT d from Department d JOIN d.projects p WHERE p.id = :projectId AND " +
            "d.deleted = false AND p.deleted = false")
    Department findActiveDepartmentByProjectId(@Param("projectId") UUID projectId);

    @Query("Select t from Task t WHERE t.project.id = :projectId AND " +
            "t.deleted = false and t.project.deleted = false")
    List<Task> findActiveTasksByProjectId(@Param("projectId") UUID projectId);

    @Query("Select t from Team t WHERE t.project.id = :projectId AND " +
            "t.deleted = false and t.project.deleted = false")
    List<Team> findActiveTeamsByProjectId(@Param("projectId") UUID projectId);

}
