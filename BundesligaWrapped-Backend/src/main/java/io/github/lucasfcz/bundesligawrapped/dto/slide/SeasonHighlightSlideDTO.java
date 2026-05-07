package io.github.lucasfcz.bundesligawrapped.dto.slide;

import io.github.lucasfcz.bundesligawrapped.dto.ClubRefDTO;

public record SeasonHighlightSlideDTO(
        ClubRefDTO homeTeam,
        ClubRefDTO guestTeam,
        String result,
        int spectators,
        int matchDay
) {}