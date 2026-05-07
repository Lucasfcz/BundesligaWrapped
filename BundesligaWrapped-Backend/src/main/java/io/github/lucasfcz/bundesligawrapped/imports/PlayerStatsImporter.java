package io.github.lucasfcz.bundesligawrapped.imports;

import io.github.lucasfcz.bundesligawrapped.exception.DataImportException;
import io.github.lucasfcz.bundesligawrapped.mapper.PlayerStatsMapper;
import io.github.lucasfcz.bundesligawrapped.model.PlayerStats;
import io.github.lucasfcz.bundesligawrapped.repository.PlayerStatsRepository;
import io.github.lucasfcz.bundesligawrapped.service.S3DataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerStatsImporter {

    private final PlayerStatsRepository repository;
    private final PlayerStatsMapper mapper;
    private final S3DataService s3DataService;

    public void importData(String s3Key) {
        log.info("Starting player stats import...");

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(s3DataService.getFile(s3Key));

            Element seasonStatistic = (Element) document
                    .getElementsByTagName("SeasonStatistic").item(0);
            String clubId = seasonStatistic.getAttribute("TeamId");

            NodeList nodes = document.getElementsByTagName("PlayerStatistic");
            List<PlayerStats> stats = new ArrayList<>();

            for (int i = 0; i < nodes.getLength(); i++) {
                Element element = (Element) nodes.item(i);
                stats.add(mapper.toEntity(element, clubId));
            }

            repository.saveAll(stats);
            log.info("Import done: {} player stats saved for club {}.", stats.size(), clubId);

        } catch (Exception e) {
            throw new DataImportException("Failed to parse player stats file: " + s3Key, e);
        }
    }
}
