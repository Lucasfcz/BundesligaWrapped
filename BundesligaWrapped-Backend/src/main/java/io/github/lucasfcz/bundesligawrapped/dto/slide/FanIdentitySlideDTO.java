package io.github.lucasfcz.bundesligawrapped.dto.slide;

public record FanIdentitySlideDTO(
        String fanType,
        int totalEngagements,
        int articles,
        int videos,
        int stories,
        int matchCenter
) {}
