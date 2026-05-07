package io.github.lucasfcz.bundesligawrapped.repository;

import io.github.lucasfcz.bundesligawrapped.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByPersonId(String personId);

}
