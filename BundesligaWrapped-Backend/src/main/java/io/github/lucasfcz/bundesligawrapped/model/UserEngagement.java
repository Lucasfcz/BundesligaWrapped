package io.github.lucasfcz.bundesligawrapped.model;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_engagements")
public class UserEngagement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "age_group")
    private String ageGroup;

    private String country;

    @Column(name = "device_family")
    private String deviceFamily;

    @Column(name = "favorite_club")
    private String favoriteClub;

    private String gender;
    private String language;
    private String platform;

    private LocalDate month;

    @Column(name = "favorite_video_title")
    private String favoriteVideoTitle;

    @Column(name = "article_view_count")
    private Integer articleViewCount;

    @Column(name = "story_view_count")
    private Integer storyViewCount;

    @Column(name = "video_view_count")
    private Integer videoViewCount;

    @Column(name = "screen_view_home_count")
    private Integer screenViewHomeCount;

    @Column(name = "screen_view_table_count")
    private Integer screenViewTableCount;

    @Column(name = "screen_view_profile_count")
    private Integer screenViewProfileCount;

    @Column(name = "match_center_total_count")
    private Integer matchCenterTotalCount;

    @Column(name = "match_center_ticker_count")
    private Integer matchCenterTickerCount;

    @Column(name = "match_center_stats_count")
    private Integer matchCenterStatsCount;

    @Column(name = "match_center_lineups_count")
    private Integer matchCenterLineupCount;

    @Column(name = "match_center_table_count")
    private Integer matchCenterTableCount;

    public UserEngagement(String userId, String ageGroup, String country, String deviceFamily, String favoriteClub, String gender, String language, String platform, LocalDate month, String favoriteVideoTitle, Integer articleViewCount, Integer storyViewCount, Integer videoViewCount, Integer screenViewHomeCount, Integer screenViewTableCount, Integer screenViewProfileCount, Integer matchCenterTotalCount, Integer matchCenterTickerCount, Integer matchCenterStatsCount, Integer matchCenterLineupCount, Integer matchCenterTableCount) {
        this.userId = userId;
        this.ageGroup = ageGroup;
        this.country = country;
        this.deviceFamily = deviceFamily;
        this.favoriteClub = favoriteClub;
        this.gender = gender;
        this.language = language;
        this.platform = platform;
        this.month = month;
        this.favoriteVideoTitle = favoriteVideoTitle;
        this.articleViewCount = articleViewCount;
        this.storyViewCount = storyViewCount;
        this.videoViewCount = videoViewCount;
        this.screenViewHomeCount = screenViewHomeCount;
        this.screenViewTableCount = screenViewTableCount;
        this.screenViewProfileCount = screenViewProfileCount;
        this.matchCenterTotalCount = matchCenterTotalCount;
        this.matchCenterTickerCount = matchCenterTickerCount;
        this.matchCenterStatsCount = matchCenterStatsCount;
        this.matchCenterLineupCount = matchCenterLineupCount;
        this.matchCenterTableCount = matchCenterTableCount;
    }
}
