package io.github.lucasfcz.bundesligawrapped.mapper;

import io.github.lucasfcz.bundesligawrapped.model.Match;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Component
public class MatchMapper {

    public Match toEntity(Document document) {
        Element general = (Element) document
                .getElementsByTagName("General").item(0);
        Element environment = (Element) document
                .getElementsByTagName("Environment").item(0);

        return new Match(
                general.getAttribute("MatchId"),
                parseInteger(general.getAttribute("MatchDay")),
                parseKickoffTime(general.getAttribute("KickoffTime")),
                general.getAttribute("HomeTeamId"),
                general.getAttribute("GuestTeamId"),
                general.getAttribute("HomeTeamName"),
                general.getAttribute("GuestTeamName"),
                general.getAttribute("Result"),
                environment.getAttribute("StadiumName"),
                parseInteger(environment.getAttribute("NumberOfSpectators")),
                parseInteger(environment.getAttribute("Temperature")),
                parseBoolean(environment.getAttribute("SoldOut"))
        );
    }

    private Integer parseInteger(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private LocalDateTime parseKickoffTime(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return OffsetDateTime.parse(value).toLocalDateTime();
        } catch (Exception e) {
            return null;
        }
    }

    private Boolean parseBoolean(String value) {
        if (value == null || value.isBlank()) return null;
        return "true".equalsIgnoreCase(value);
    }
}
