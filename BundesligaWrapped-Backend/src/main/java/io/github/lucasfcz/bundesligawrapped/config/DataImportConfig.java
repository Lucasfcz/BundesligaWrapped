package io.github.lucasfcz.bundesligawrapped.config;

import io.github.lucasfcz.bundesligawrapped.imports.*;
import io.github.lucasfcz.bundesligawrapped.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataImportConfig implements ApplicationRunner {

    private final UserEngagementImporter userEngagementImporter;
    private final ClubImporter clubImporter;
    private final UserEngagementRepository userEngagementRepository;
    private final ClubRepository clubRepository;
    private final PlayerImporter playerImporter;
    private final PlayerRepository playerRepository;
    private final MatchImporter matchImporter;
    private final MatchRepository matchRepository;
    private final PlayerStatsImporter playerStatsImporter;
    private final PlayerStatsRepository playerStatsRepository;

    @Value("${app.s3.prefix}")
    private String s3Prefix;

    @Override
    public void run(ApplicationArguments args) {

        if (clubRepository.count() == 0) {
            clubImporter.importData(
                    s3Prefix + "data/feeds-exports-24-25/01.04.Clubs.xml"
            );
        } else {
            log.info("Clubs already imported, skipping.");
        }

        if (matchRepository.count() == 0) {
            matchImporter.importData(s3Prefix);
        } else {
            log.info("Matches already imported, skipping.");
        }

        if (playerRepository.count() == 0) {
            playerImporter.importData(s3Prefix);
        } else {
            log.info("Players already imported, skipping.");
        }

        if (playerStatsRepository.count() == 0) {
            playerStatsImporter.importData(
                    s3Prefix + "data/1K8_Bayern.xml"
            );
        } else {
            log.info("Player stats already imported, skipping.");
        }

        if (userEngagementRepository.count() == 0) {
            userEngagementImporter.importData(
                    s3Prefix + "data/bundesliga_wrapped_challenge_dataset.json"
            );
        } else {
            log.info("Engagements already imported, skipping.");
        }
    }
}