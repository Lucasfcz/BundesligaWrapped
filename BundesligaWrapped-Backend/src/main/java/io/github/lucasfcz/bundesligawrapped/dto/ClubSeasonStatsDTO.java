package io.github.lucasfcz.bundesligawrapped.dto;

public record ClubSeasonStatsDTO(
        int matchesPlayed,
        int wins,
        int draws,
        int losses,
        int goalsScored,
        int goalsConceded,
        int biggestWinSpectators
) {}
