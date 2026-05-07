package io.github.lucasfcz.bundesligawrapped.dto.slide;

public record TopPlayerSlideDTO(
        String playerName,
        String clubId,
        String clubName,
        String position,
        int goals,
        int assists
) {}
