package com.ToolRent.ToolRent.Repository;

import com.ToolRent.ToolRent.Entity.ToolsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ToolsRepository extends JpaRepository<ToolsEntity, Long> {
}