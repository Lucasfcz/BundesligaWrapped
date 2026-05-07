CREATE TABLE clubs
(
    id                BIGSERIAL PRIMARY KEY,
    club_id           VARCHAR(20)  NOT NULL UNIQUE,
    club_name         VARCHAR(100),
    short_name        VARCHAR(50),
    three_letter_code VARCHAR(5),
    stadium_name      VARCHAR(100),
    founded           INTEGER,
    city              VARCHAR(100),
    color_primary     VARCHAR(10),
    color_secondary   VARCHAR(10)
);

CREATE TABLE players
(
    id           BIGSERIAL PRIMARY KEY,
    person_id    VARCHAR(20)  NOT NULL UNIQUE,
    club_id      VARCHAR(20)  NOT NULL,
    first_name   VARCHAR(100),
    last_name    VARCHAR(100),
    alias        VARCHAR(100),
    shirt_number INTEGER,
    position     VARCHAR(50),
    birth_date   VARCHAR(15),
    nationality  VARCHAR(50),
    height       INTEGER,
    weight       INTEGER
);

CREATE TABLE matches
(
    id              BIGSERIAL PRIMARY KEY,
    match_id        VARCHAR(50)  NOT NULL UNIQUE,
    match_day       INTEGER,
    kickoff_time    TIMESTAMP,
    home_team_id    VARCHAR(50),
    home_team_name  VARCHAR(100),
    guest_team_id   VARCHAR(50),
    guest_team_name VARCHAR(100),
    result          VARCHAR(20),
    stadium_name    VARCHAR(100),
    spectators      INTEGER,
    temperature     INTEGER,
    sold_out        BOOLEAN
);

CREATE TABLE player_stats
(
    id                BIGSERIAL PRIMARY KEY,
    player_id         VARCHAR(20)  NOT NULL UNIQUE,
    club_id           VARCHAR(20),
    first_name        VARCHAR(100),
    last_name         VARCHAR(100),
    goals             INTEGER,
    assists           INTEGER,
    shots_at_goal     INTEGER,
    shots_successful  INTEGER,
    playing_time      VARCHAR(15),
    distance_covered  BIGINT,
    max_speed         DOUBLE PRECISION,
    yellow_cards      INTEGER,
    red_cards         INTEGER,
    xg                DOUBLE PRECISION,
    xg_efficiency     DOUBLE PRECISION,
    passes_successful INTEGER,
    passes_total      INTEGER,
    duels_won         INTEGER,
    duels_total       INTEGER
);

CREATE TABLE user_engagements
(
    id                         BIGSERIAL PRIMARY KEY,
    user_id                    VARCHAR(100) NOT NULL,
    age_group                  VARCHAR(10),
    country                    VARCHAR(5),
    device_family              VARCHAR(100),
    favorite_club              VARCHAR(100),
    gender                     VARCHAR(10),
    language                   VARCHAR(10),
    platform                   VARCHAR(20),
    month                      DATE,
    favorite_video_title       VARCHAR(255),
    article_view_count         INTEGER,
    story_view_count           INTEGER,
    video_view_count           INTEGER,
    screen_view_home_count     INTEGER,
    screen_view_table_count    INTEGER,
    screen_view_profile_count  INTEGER,
    match_center_total_count   INTEGER,
    match_center_ticker_count  INTEGER,
    match_center_stats_count   INTEGER,
    match_center_lineups_count INTEGER,
    match_center_table_count   INTEGER
);

CREATE INDEX idx_user_engagements_user_id ON user_engagements (user_id);
CREATE INDEX idx_user_engagements_club ON user_engagements (favorite_club);