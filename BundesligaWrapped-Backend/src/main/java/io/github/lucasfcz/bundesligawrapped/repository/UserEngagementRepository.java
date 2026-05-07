package io.github.lucasfcz.bundesligawrapped.repository;


import io.github.lucasfcz.bundesligawrapped.model.UserEngagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserEngagementRepository extends JpaRepository<UserEngagement, Long> {
    List<UserEngagement> findByUserId(String userId);
}
