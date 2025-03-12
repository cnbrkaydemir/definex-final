package com.cnbrkaydemir.tasks.repository;

import com.cnbrkaydemir.tasks.model.Comment;
import com.cnbrkaydemir.tasks.model.Task;
import com.cnbrkaydemir.tasks.model.Team;
import com.cnbrkaydemir.tasks.model.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UsersRepository extends BaseRepository<Users, UUID> {

    @Query("SELECT t FROM Team t JOIN t.teamMembers m WHERE m.id = :userId AND " +
            "t.deleted = false AND m.deleted = false")
    List<Team> findActiveTeamsByUserId(@Param("userId") UUID userId);

    @Query("SELECT t FROM Task t WHERE t.assignee.id = :userId AND " +
            "t.assignee.deleted = false AND t.deleted = false")
    List<Task> findActiveTasksByUserId(@Param("userId") UUID userId);

    @Query("SELECT c FROM Comment c WHERE c.commentedBy.id = :userId AND " +
            "c.commentedBy.deleted = false AND c.deleted = false")
    List<Comment> findActiveCommentsByUserId(@Param("userId") UUID userId);

}
