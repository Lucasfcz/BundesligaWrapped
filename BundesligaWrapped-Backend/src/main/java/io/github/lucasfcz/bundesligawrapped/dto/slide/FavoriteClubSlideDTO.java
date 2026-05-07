package io.github.lucasfcz.bundesligawrapped.dto.slide;

import io.github.lucasfcz.bundesligawrapped.dto.ClubSeasonStatsDTO;

public record FavoriteClubSlideDTO(
        String clubName,
        String shortName,
        String clubId,
        String primaryColor,
        String secondaryColor,
        ClubSeasonStatsDTO seasonStats
) {}
