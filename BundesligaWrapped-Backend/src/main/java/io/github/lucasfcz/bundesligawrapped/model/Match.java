package io.github.lucasfcz.bundesligawrapped.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "match_id", unique = true, nullable = false)
    private String matchId;

    @Column(name = "match_day")
    private Integer matchDay;

    @Column(name = "kickoff_time")
    private LocalDateTime kickoffTime;

    @Column(name = "home_team_id")
    private String homeTeamId;

    @Column(name = "guest_team_id")
    private String guestTeamId;

    @Column(name = "home_team_name")
    private String homeTeamName;

    @Column(name = "guest_team_name")
    private String guestTeamName;

    private String result;

    @Column(name = "stadium_name")
    private String stadiumName;

    private Integer spectators;
    private Integer temperature;

    @Column(name = "sold_out")
    private Boolean soldOut;

    public Match(String matchId, Integer matchDay, LocalDateTime kickoffTime, String homeTeamId, String guestTeamId, String homeTeamName, String guestTeamName, String result, String stadiumName, Integer spectators, Integer temperature, Boolean soldOut) {
        this.matchId = matchId;
        this.matchDay = matchDay;
        this.kickoffTime = kickoffTime;
        this.homeTeamId = homeTeamId;
        this.guestTeamId = guestTeamId;
        this.homeTeamName = homeTeamName;
        this.guestTeamName = guestTeamName;
        this.result = result;
        this.stadiumName = stadiumName;
        this.spectators = spectators;
        this.temperature = temperature;
        this.soldOut = soldOut;
    }
}

