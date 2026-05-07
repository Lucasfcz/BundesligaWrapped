package io.github.lucasfcz.bundesligawrapped.mapper;

import io.github.lucasfcz.bundesligawrapped.imports.json.UserEngagementJson;
import io.github.lucasfcz.bundesligawrapped.model.UserEngagement;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class UserEngagementMapper {

    public UserEngagement toEntity(UserEngagementJson json) {
        return new UserEngagement(
                sanitizeUserId(json.getUserId()),
                json.getAgeGroup(),
                json.getCountry(),
                json.getDeviceFamily(),
                json.getFavoriteClub(),
                json.getGender(),
                json.getLanguage(),
                json.getPlatform(),
                parseMonth(json.getMonth()),
                json.getFavoriteVideoTitle(),
                json.getArticleViewCount(),
                json.getStoryViewCount(),
                json.getVideoViewCount(),
                json.getScreenViewHomeCount(),
                json.getScreenViewTableCount(),
                json.getScreenViewProfileCount(),
                json.getMatchCenterTotalCount(),
                json.getMatchCenterTickerCount(),
                json.getMatchCenterStatsCount(),
                json.getMatchCenterLineupCount(),
                json.getMatchCenterTableCount()
        );
    }

    private String sanitizeUserId(String rawId) {
        if (rawId == null) return null;
        return rawId.replace("'", "").trim();
    }

    private LocalDate parseMonth(String month) {
        if (month == null) return null;
        return LocalDate.parse(month);
    }
}