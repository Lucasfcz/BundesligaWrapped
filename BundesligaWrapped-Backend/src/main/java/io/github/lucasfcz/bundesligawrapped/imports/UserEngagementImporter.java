package io.github.lucasfcz.bundesligawrapped.imports;

import io.github.lucasfcz.bundesligawrapped.imports.json.UserEngagementJson;
import io.github.lucasfcz.bundesligawrapped.exception.DataImportException;
import io.github.lucasfcz.bundesligawrapped.mapper.UserEngagementMapper;
import io.github.lucasfcz.bundesligawrapped.model.UserEngagement;
import io.github.lucasfcz.bundesligawrapped.repository.UserEngagementRepository;
import io.github.lucasfcz.bundesligawrapped.service.S3DataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserEngagementImporter {

    private final UserEngagementRepository repository;
    private final ObjectMapper objectMapper;
    private final UserEngagementMapper mapper;
    private final S3DataService s3DataService;

    public void importData(String s3Key) {
        log.info("Starting the import of user engagements...");

        List<UserEngagementJson> records = readFile(s3Key);
        List<UserEngagement> entities = records.stream()
                .map(mapper::toEntity)
                .toList();

        repository.saveAll(entities);
        log.info("Import done: {} records saved.", entities.size());
    }

    private List<UserEngagementJson> readFile(String s3Key) {
        try {
            return objectMapper.readValue(
                    s3DataService.getFile(s3Key),
                    objectMapper.getTypeFactory()
                            .constructCollectionType(List.class, UserEngagementJson.class)
            );
        } catch (Exception e) {
            throw new DataImportException("Error reading file from S3: " + s3Key, e);
        }
    }
}