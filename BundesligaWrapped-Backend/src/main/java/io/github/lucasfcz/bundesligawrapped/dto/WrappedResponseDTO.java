package io.github.lucasfcz.bundesligawrapped.dto;

import io.github.lucasfcz.bundesligawrapped.dto.slide.*;

public record WrappedResponseDTO(
        String userId,
        FanIdentitySlideDTO fanIdentity,
        MostActiveMonthSlideDTO mostActiveMonth,
        FavoriteClubSlideDTO favoriteClub,
        TopPlayerSlideDTO topPlayer,
        SeasonHighlightSlideDTO seasonHighlight,
        NarrativeSlideDTO narrative
) {
}
