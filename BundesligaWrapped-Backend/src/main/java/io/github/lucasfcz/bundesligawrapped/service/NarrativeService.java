package io.github.lucasfcz.bundesligawrapped.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.lucasfcz.bundesligawrapped.dto.slide.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;



import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class NarrativeService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;

    public NarrativeService(
            ObjectMapper objectMapper,
            @Value("${groq.api.key}") String apiKey
    ) {
        this.restClient = RestClient.builder()
                .baseUrl("https://api.groq.com/openai")
                .build();
        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
    }

    public NarrativeSlideDTO generate(
            String userId,
            String fanType,
            int totalEngagements,
            FavoriteClubSlideDTO club,
            TopPlayerSlideDTO player,
            MostActiveMonthSlideDTO activeMonth
    ) {
        String prompt = buildPrompt(userId, fanType, totalEngagements, club, player, activeMonth);

        try {
            Map<String, Object> body = Map.of(
                    "model", "llama-3.3-70b-versatile",
                    "max_tokens", 300,
                    "messages", List.of(Map.of("role", "user", "content", prompt))
            );

            String json = restClient.post()
                    .uri("/v1/chat/completions")
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .body(objectMapper.writeValueAsString(body))
                    .retrieve()
                    .body(String.class);

            var response = objectMapper.readTree(json);
            String text = response.path("choices").get(0).path("message").path("content").asText();

            return new NarrativeSlideDTO(text);

        } catch (Exception e) {
            log.error("Grok API call failed for userId={}: {}", userId, e.getMessage());
            return new NarrativeSlideDTO("What a season it's been! Keep following the Bundesliga.");
        }
    }

    private String buildPrompt(
            String userId, String fanType, int total,
            FavoriteClubSlideDTO club, TopPlayerSlideDTO player,
            MostActiveMonthSlideDTO activeMonth
    ) {
        return """
                You are generating a personalized Bundesliga season recap for a fan.
                Write 2-3 engaging sentences summarizing their season. Be energetic and fun.
                
                Fan data:
                - Fan type: %s
                - Total engagements: %d
                - Favorite club: %s
                - Top player followed: %s (%d goals, %d assists)
                - Most active month: %s
                
                Respond with only the narrative text, no extra formatting.
                """.formatted(
                fanType, total,
                club.clubName(),
                player.playerName(), player.goals(), player.assists(),
                activeMonth.month()
        );
    }
}

