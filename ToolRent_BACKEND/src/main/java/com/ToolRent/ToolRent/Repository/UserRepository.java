package com.ToolRent.ToolRent.Repository;

import com.ToolRent.ToolRent.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    @Query("SELECT COUNT(l) " +
            "FROM LoanEntity l " +
            "WHERE l.client.id = :userId " +
            "AND l.delivered = false")
    long countActiveLoans(@Param("userId") Long userId);

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END " +
            "FROM LoanEntity l JOIN l.toolUnit tu " +
            "WHERE l.client.id = :userId " +
            "AND tu.tool.id = :toolId " +
            "AND l.delivered = false")
    boolean existsActiveLoanForTool(@Param("userId") Long userId, @Param("toolId") Long toolId);
}