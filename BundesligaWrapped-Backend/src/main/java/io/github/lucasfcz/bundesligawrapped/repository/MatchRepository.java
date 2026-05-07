package io.github.lucasfcz.bundesligawrapped.repository;

import io.github.lucasfcz.bundesligawrapped.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
}

