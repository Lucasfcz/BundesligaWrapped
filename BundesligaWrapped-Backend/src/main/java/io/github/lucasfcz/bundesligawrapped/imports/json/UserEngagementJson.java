package io.github.lucasfcz.bundesligawrapped.imports.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserEngagementJson {

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("age_group")
    private String ageGroup;

    @JsonProperty("country")
    private String country;

    @JsonProperty("device_family")
    private String deviceFamily;

    @JsonProperty("favorite_club")
    private String favoriteClub;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("language")
    private String language;

    @JsonProperty("platform")
    private String platform;

    @JsonProperty("month")
    private String month;

    @JsonProperty("favorite_video_title")
    private String favoriteVideoTitle;

    @JsonProperty("article_view_count")
    private Integer articleViewCount;

    @JsonProperty("story_view_count")
    private Integer storyViewCount;

    @JsonProperty("video_view_count")
    private Integer videoViewCount;

    @JsonProperty("screen_view_home_count")
    private Integer screenViewHomeCount;

    @JsonProperty("screen_view_table_count")
    private Integer screenViewTableCount;

    @JsonProperty("screen_view_profile_count")
    private Integer screenViewProfileCount;

    @JsonProperty("screen_view_match_center_total_count")
    private Integer matchCenterTotalCount;

    @JsonProperty("screen_view_match_center_ticker_count")
    private Integer matchCenterTickerCount;

    @JsonProperty("screen_view_match_center_stats_count")
    private Integer matchCenterStatsCount;

    @JsonProperty("screen_view_match_center_lineups_count")
    private Integer matchCenterLineupCount;

    @JsonProperty("screen_view_match_center_table_count")
    private Integer matchCenterTableCount;
}