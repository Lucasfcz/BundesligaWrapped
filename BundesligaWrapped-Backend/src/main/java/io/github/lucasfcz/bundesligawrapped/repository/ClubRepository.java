package io.github.lucasfcz.bundesligawrapped.repository;

import io.github.lucasfcz.bundesligawrapped.model.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
    Optional<Club> findByClubId(String clubId);
    Optional<Club> findByClubNameIgnoreCase(String clubName);
}

