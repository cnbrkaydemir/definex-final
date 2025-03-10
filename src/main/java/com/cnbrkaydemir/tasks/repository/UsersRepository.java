package com.cnbrkaydemir.tasks.repository;

import com.cnbrkaydemir.tasks.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<Users, UUID> {
}
