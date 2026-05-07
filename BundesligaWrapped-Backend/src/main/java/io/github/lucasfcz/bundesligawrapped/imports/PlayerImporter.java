package io.github.lucasfcz.bundesligawrapped.imports;

import io.github.lucasfcz.bundesligawrapped.exception.DataImportException;
import io.github.lucasfcz.bundesligawrapped.mapper.PlayerMapper;
import io.github.lucasfcz.bundesligawrapped.model.Player;
import io.github.lucasfcz.bundesligawrapped.repository.PlayerRepository;
import io.github.lucasfcz.bundesligawrapped.service.S3DataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerImporter {

    private final PlayerRepository repository;
    private final PlayerMapper mapper;
    private final S3DataService s3DataService;

    public void importData(String s3Prefix) {
        log.info("Starting players import...");

        List<String> keys = s3DataService
                .listFiles(s3Prefix + "data/feeds-exports-24-25/players/")
                .stream()
                .filter(k -> k.contains("01.05.") && k.endsWith(".xml"))
                .toList();

        if (keys.isEmpty()) {
            throw new DataImportException("No player files found in S3: " + s3Prefix);
        }

        Map<String, Player> playersMap = new HashMap<>();

        for (String key : keys) {
            processFile(key, playersMap);
        }

        repository.saveAll(playersMap.values());
        log.info("Import done: {} players saved.", playersMap.size());
    }

    private void processFile(String key, Map<String, Player> playersMap) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(s3DataService.getFile(key));

            NodeList nodes = document.getElementsByTagName("Object");

            for (int i = 0; i < nodes.getLength(); i++) {
                Element element = (Element) nodes.item(i);

                if ("player".equals(element.getAttribute("Type"))) {

                    Player incoming = mapper.toEntity(element);
                    String playerId = incoming.getPersonId();

                    Player existing = playersMap.get(playerId);

                    if (existing == null) {
                        playersMap.put(playerId, incoming);
                        continue;
                    }

                    boolean existingLeft = existing.getLeaveDate() != null;
                    boolean incomingLeft = incoming.getLeaveDate() != null;

                    // If player already exists but has left, update with incoming data (which may be active)
                    if (existingLeft && !incomingLeft) {
                        playersMap.put(playerId, incoming);
                    }

                    // If both existing and incoming have left, keep the one with the latest leave date
                    else if (!existingLeft && !incomingLeft) {
                        playersMap.put(playerId, incoming);
                    }
                }
            }

        } catch (Exception e) {
            throw new DataImportException("Failed to parse player file: " + key, e);
        }
    }
}