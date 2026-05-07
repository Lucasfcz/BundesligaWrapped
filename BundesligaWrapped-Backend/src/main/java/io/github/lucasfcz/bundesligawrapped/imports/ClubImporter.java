package io.github.lucasfcz.bundesligawrapped.imports;

import io.github.lucasfcz.bundesligawrapped.exception.DataImportException;
import io.github.lucasfcz.bundesligawrapped.mapper.ClubMapper;
import io.github.lucasfcz.bundesligawrapped.model.Club;
import io.github.lucasfcz.bundesligawrapped.repository.ClubRepository;
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
public class ClubImporter {

    private final ClubRepository repository;
    private final ClubMapper mapper;
    private final S3DataService s3DataService;

    public void importData(String s3Key) {
        log.info("Starting clubs import...");

        Document document = parseXml(s3Key);
        NodeList clubNodes = document.getElementsByTagName("Club"); // Read all elements with <Club> in document

        List<Club> clubs = new ArrayList<>();
        for (int i = 0; i < clubNodes.getLength(); i++) {
            Element element = (Element) clubNodes.item(i);
            clubs.add(mapper.toEntity(element)); // converts json data to object and add to array
        }

        repository.saveAll(clubs);
        log.info("Import done: {} clubs saved.", clubs.size());
    }

    private Document parseXml(String s3Key) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(s3DataService.getFile(s3Key)); // converts s3 data to read
        } catch (Exception e) {
            throw new DataImportException("Failed to read XML from S3: " + s3Key, e);
        }
    }
}