package com.cnbrkaydemir.tasks.repository;

import com.cnbrkaydemir.tasks.model.Department;
import com.cnbrkaydemir.tasks.model.Project;
import com.cnbrkaydemir.tasks.model.Team;
import com.cnbrkaydemir.tasks.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TeamRepository extends BaseRepository<Team, UUID> {

    @Query("SELECT u FROM Users u JOIN u.teams t WHERE t.id = :teamId AND " +
            "t.deleted = false AND u.deleted = false")
    List<Users> findActiveUsersByTeamId(@Param("teamId") UUID teamId);

    @Query("SELECT d FROM Department d JOIN d.teams t WHERE t.id = :teamId AND " +
            "t.deleted = false AND d.deleted = false")
    Department findActiveDepartmentByTeamId(@Param("teamId") UUID teamId);

    @Query("SELECT p FROM Project p JOIN p.teams t WHERE t.id = :teamId AND " +
            "t.deleted = false AND p.deleted = false")
    Project findActiveProjectByTeamId(@Param("teamId") UUID teamId);

}
