package com.ToolRent.ToolRent.Repository;

import com.ToolRent.ToolRent.Entity.ToolStatus;
import com.ToolRent.ToolRent.Entity.ToolUnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ToolUnitRepository extends JpaRepository<ToolUnitEntity, Long> {
    List<ToolUnitEntity> findByTool_IdAndStatus(Long toolId, ToolStatus status);
}