package io.github.lucasfcz.bundesligawrapped.service;

import io.github.lucasfcz.bundesligawrapped.dto.ClubRefDTO;
import io.github.lucasfcz.bundesligawrapped.dto.ClubSeasonStatsDTO;
import io.github.lucasfcz.bundesligawrapped.dto.WrappedResponseDTO;
import io.github.lucasfcz.bundesligawrapped.dto.slide.*;
import io.github.lucasfcz.bundesligawrapped.model.Club;
import io.github.lucasfcz.bundesligawrapped.model.Player;
import io.github.lucasfcz.bundesligawrapped.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WrappedGeneratorService {

    private final UserEngagementRepository userEngagementRepository;
    private final PlayerStatsRepository playerStatsRepository;
    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;
    private final ClubRepository clubRepository;
    private final NarrativeService narrativeService;

    public WrappedResponseDTO generate(String userId) {
        log.info("Generating wrapped for userId={}", userId);

        var engagements = userEngagementRepository.findByUserId(userId);

        if (engagements.isEmpty()) {
            throw new IllegalArgumentException("No engagement data found for userId: " + userId);
        }

        int articles = engagements.stream().mapToInt(e -> safe(e.getArticleViewCount())).sum();
        int videos = engagements.stream().mapToInt(e -> safe(e.getVideoViewCount())).sum();
        int stories = engagements.stream().mapToInt(e -> safe(e.getStoryViewCount())).sum();
        int matchCenter = engagements.stream().mapToInt(e -> safe(e.getMatchCenterTotalCount())).sum();
        int total = articles + videos + stories + matchCenter;

        String fanType = resolveFanType(matchCenter, stories, videos);

        var mostActiveMonth = engagements.stream()
                .filter(e -> e.getMonth() != null)
                .max(Comparator.comparingInt(e -> safe(e.getMatchCenterTotalCount())))
                .map(e -> new MostActiveMonthSlideDTO(
                        e.getMonth().getMonth().name(),
                        safe(e.getMatchCenterTotalCount())
                ))
                .orElse(new MostActiveMonthSlideDTO("Unknown", 0));

        String favoriteClubName = engagements.stream()
                .filter(e -> e.getFavoriteClub() != null)
                .collect(Collectors.groupingBy(e -> e.getFavoriteClub(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        var favoriteClub = resolveClubByName(favoriteClubName);

        var topPlayer = playerStatsRepository.findAll().stream()
                .max(Comparator.comparingInt(ps -> safe(ps.getGoals())))
                .map(ps -> {
                    String clubName = clubRepository.findByClubId(ps.getClubId())
                            .map(Club::getClubName)
                            .orElse("");
                    String position = playerRepository.findByPersonId(ps.getPlayerId())
                            .map(Player:: getPosition)
                            .orElse("Unknown");
                    return new TopPlayerSlideDTO(
                            ps.getFirstName() + " " + ps.getLastName(),
                            clubName,
                            ps.getClubId(),
                            position,
                            safe(ps.getGoals()),
                            safe(ps.getAssists())
                    );
                })
                .orElse(new TopPlayerSlideDTO("Unknown", "", "", "Unknown", 0, 0
                ));

        var matchOpt = matchRepository.findAll().stream()
                .filter(m -> m.getSpectators() != null)
                .max(Comparator.comparingInt(m -> m.getSpectators()));

        matchOpt.ifPresent(m -> {
            log.info("TOP MATCH FOUND:");
            log.info("Home: {} ({})", m.getHomeTeamName(), m.getHomeTeamId());
            log.info("Away: {} ({})", m.getGuestTeamName(), m.getGuestTeamId());
            log.info("Result: {}", m.getResult());
            log.info("Spectators: {}", m.getSpectators());
        });

        var seasonHighlight = matchOpt
                .map(m -> new SeasonHighlightSlideDTO(
                                resolveClub(m.getHomeTeamName(), m.getHomeTeamId()),
                                resolveClub(m.getGuestTeamName(), m.getGuestTeamId()),
                        m.getResult(),
                        safe(m.getSpectators()),
                        safe(m.getMatchDay())
                ))
                .orElse(new SeasonHighlightSlideDTO(
                        new ClubRefDTO(null, "Unknown"),
                        new ClubRefDTO(null, "Unknown"),
                        "?",
                        0,
                        0
                ));

        var narrative = narrativeService.generate(
                userId, fanType, total, favoriteClub, topPlayer, mostActiveMonth
        );

        return new WrappedResponseDTO(
                userId,
                new FanIdentitySlideDTO(fanType, total, articles, videos, stories, matchCenter),
                mostActiveMonth,
                favoriteClub,
                topPlayer,
                seasonHighlight,
                narrative
        );
    }

    private String resolveFanType(int matchCenter, int stories, int videos) {
        if (matchCenter > 100) return "Match Center Addict";
        if (videos > 50) return "Stats Nerd";
        if (stories > 30) return "Story Lover";
        return "Casual Fan";
    }

    private FavoriteClubSlideDTO resolveClubByName(String clubName) {
        if (clubName == null) {
            return new FavoriteClubSlideDTO("Unknown", "", "", "", "", null);
        }

        return clubRepository.findByClubNameIgnoreCase(clubName)
                .map(c -> new FavoriteClubSlideDTO(
                        c.getClubName(),
                        c.getShortName(),
                        c.getClubId(),
                        c.getColorPrimary(),
                        c.getColorSecondary(),
                        resolveClubStats(clubName)
                ))
                .orElse(new FavoriteClubSlideDTO(clubName, "", "", "", "", null));
    }

    // Uses name if available, otherwise tries to resolve from ID, and falls back to "Unknown"
    private String resolveClubName(String name, String id) {
        if (name != null && !name.isBlank()) {
            return name;
        }

        if (id == null) {
            return "Unknown";
        }

        return clubRepository.findByClubId(id)
                .map(c -> c.getClubName())
                .orElse(id); // fallback
    }

    private int safe(Integer v) {
        return v == null ? 0 : v;
    }

    private ClubSeasonStatsDTO resolveClubStats(String clubName) {
        var matches = matchRepository.findAll();

        var clubMatches = matches.stream()
                .filter(m -> clubName.equals(m.getHomeTeamName()) ||
                        clubName.equals(m.getGuestTeamName()))
                .toList();

        int wins = 0, draws = 0, losses = 0, goalsFor = 0, goalsAgainst = 0;

        for (var m : clubMatches) {
            if (m.getResult() == null) continue;
            var parts = m.getResult().split(":");
            if (parts.length != 2) continue;
            try {
                int home = Integer.parseInt(parts[0].trim());
                int away = Integer.parseInt(parts[1].trim());
                boolean isHome = clubName.equals(m.getHomeTeamName());
                int gf = isHome ? home : away;
                int ga = isHome ? away : home;
                goalsFor += gf;
                goalsAgainst += ga;
                if (gf > ga) wins++;
                else if (gf == ga) draws++;
                else losses++;
            } catch (NumberFormatException ignored) {
            }
        }

        int biggestCrowd = clubMatches.stream()
                .filter(m -> clubName.equals(m.getHomeTeamName()))
                .mapToInt(m -> safe(m.getSpectators()))
                .max().orElse(0);

        return new ClubSeasonStatsDTO(
                clubMatches.size(), wins, draws, losses,
                goalsFor, goalsAgainst, biggestCrowd
        );
    }

    private ClubRefDTO resolveClub(String name, String id) {
        if (id != null) {
            var club = clubRepository.findByClubId(id);
            if (club.isPresent()) {
                return new ClubRefDTO(id, club.get().getClubName());
            }
        }

        return new ClubRefDTO(id, name != null ? name : "Unknown");
    }
}