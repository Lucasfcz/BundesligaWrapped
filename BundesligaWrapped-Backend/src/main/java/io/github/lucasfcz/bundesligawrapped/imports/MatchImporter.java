package io.github.lucasfcz.bundesligawrapped.imports;

import io.github.lucasfcz.bundesligawrapped.exception.DataImportException;
import io.github.lucasfcz.bundesligawrapped.mapper.MatchMapper;
import io.github.lucasfcz.bundesligawrapped.model.Match;
import io.github.lucasfcz.bundesligawrapped.repository.MatchRepository;
import io.github.lucasfcz.bundesligawrapped.service.S3DataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Slf4j
@Service
@RequiredArgsConstructor
public class MatchImporter {

    private final MatchRepository repository;
    private final MatchMapper mapper;
    private final S3DataService s3DataService;

    public void importData(String s3Prefix) {
        log.info("Starting matches import...");

        List<String> keys = s3DataService
                .listFiles(s3Prefix + "data/feeds-exports-24-25/matches/")
                .stream()
                .filter(k -> k.contains("DFL-MAT-") && k.endsWith(".xml")) // files validation
                .toList();

        if (keys.isEmpty()) {
            throw new DataImportException("No match files found in S3: " + s3Prefix);
        }

        List<Match> matches = new ArrayList<>();
        for (String key : keys) {
            parseFile(key).ifPresent(matches::add);
        }

        repository.saveAll(matches);
        log.info("Import done: {} matches saved.", matches.size());
    }

    private Optional<Match> parseFile(String key) {
        try { // Read files and converts to object
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(s3DataService.getFile(key));
            return Optional.of(mapper.toEntity(document));
        } catch (Exception e) { //keep going even though is empty
            log.warn("Failed to parse match file: {} — {}", key, e.getMessage());
            return Optional.empty();
        }
    }
}