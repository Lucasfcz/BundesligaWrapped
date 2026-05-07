package io.github.lucasfcz.bundesligawrapped.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "player_stats")

public class PlayerStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "player_id", unique = true, nullable = false)
    private String playerId;

    @Column(name = "club_id")
    private String clubId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private Integer goals;
    private Integer assists;

    @Column(name = "shots_at_goal")
    private Integer shotsAtGoal;

    @Column(name = "shots_successful")
    private Integer shotsSuccessful;

    @Column(name = "playing_time")
    private String playingTime;

    @Column(name = "distance_covered")
    private Long distanceCovered;

    @Column(name = "max_speed")
    private Double maxSpeed;

    @Column(name = "yellow_cards")
    private Integer yellowCards;

    @Column(name = "red_cards")
    private Integer redCards;

    private Double xg;

    @Column(name = "xg_efficiency")
    private Double xgEfficiency;

    @Column(name = "passes_successful")
    private Integer passesSuccessful;

    @Column(name = "passes_total")
    private Integer passesTotal;

    @Column(name = "duels_won")
    private Integer duelsWon;

    @Column(name = "duels_total")
    private Integer duelsTotal;

    public PlayerStats(String playerId, String clubId, String firstName, String lastName, Integer goals, Integer assists, Integer shotsAtGoal, Integer shotsSuccessful, String playingTime, Long distanceCovered, Double maxSpeed, Integer yellowCards, Integer redCards, Double xg, Double xgEfficiency, Integer passesSuccessful, Integer passesTotal, Integer duelsWon, Integer duelsTotal) {
        this.playerId = playerId;
        this.clubId = clubId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.goals = goals;
        this.assists = assists;
        this.shotsAtGoal = shotsAtGoal;
        this.shotsSuccessful = shotsSuccessful;
        this.playingTime = playingTime;
        this.distanceCovered = distanceCovered;
        this.maxSpeed = maxSpeed;
        this.yellowCards = yellowCards;
        this.redCards = redCards;
        this.xg = xg;
        this.xgEfficiency = xgEfficiency;
        this.passesSuccessful = passesSuccessful;
        this.passesTotal = passesTotal;
        this.duelsWon = duelsWon;
        this.duelsTotal = duelsTotal;
    }
}
