package com.project.milkcollection.auth.repository;

import com.project.milkcollection.auth.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ModuleRepository extends JpaRepository<Module, UUID> {
}
