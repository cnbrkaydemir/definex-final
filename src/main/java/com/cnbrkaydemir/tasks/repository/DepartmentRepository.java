package com.cnbrkaydemir.tasks.repository;

import com.cnbrkaydemir.tasks.model.Department;
import com.cnbrkaydemir.tasks.model.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DepartmentRepository extends BaseRepository<Department, UUID> {
    @Query("SELECT p FROM Project p WHERE p.department.id = :departmentId" +
            " AND p.department.deleted = false AND p.deleted = false")
    List<Project> findActiveProjectsByDepartmentId(@Param("departmentId") UUID departmentId);


    @Query("SELECT t FROM Team t WHERE t.department.id = :departmentId" +
            " AND t.department.deleted = false AND t.deleted = false")
    List<Project> findActiveTeamsByDepartmentId(@Param("departmentId") UUID departmentId);
}
