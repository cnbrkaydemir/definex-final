package com.cnbrkaydemir.tasks.repository;


import com.cnbrkaydemir.tasks.model.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity, ID> extends JpaRepository<T, ID> {
    @Override
    @Query("SELECT e From #{#entityName} e where e.deleted = false")
    List<T> findAll();

    @Query("SELECT e From #{#entityName} e where e.deleted = true")
    List<T> findDeleted();

}
