package io.github.lucasfcz.bundesligawrapped.mapper;

import io.github.lucasfcz.bundesligawrapped.model.PlayerStats;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

@Component
public class PlayerStatsMapper {

    public PlayerStats toEntity(Element element, String clubId) {
        return new PlayerStats(
                element.getAttribute("PlayerId"),
                clubId,
                element.getAttribute("PlayerFirstName"),
                element.getAttribute("PlayerLastName"),
                parseInteger(element.getAttribute("ShotsAtGoalSuccessfull")),
                parseInteger(element.getAttribute("Assists")),
                parseInteger(element.getAttribute("ShotsAtGoalSum")),
                parseInteger(element.getAttribute("ShotsAtGoalSuccessfull")),
                element.getAttribute("PlayingTime"),
                parseLong(element.getAttribute("DistanceCovered")),
                parseDouble(element.getAttribute("MaximumSpeed")),
                parseInteger(element.getAttribute("CardsYellow")),
                parseInteger(element.getAttribute("CardsRed")),
                parseDouble(element.getAttribute("xG")),
                parseDouble(element.getAttribute("xGEfficiency")),
                parseInteger(element.getAttribute("PassesSuccessfulSum")),
                parseInteger(element.getAttribute("PassesSum")),
                parseInteger(element.getAttribute("TacklingGamesWon")),
                parseInteger(element.getAttribute("TacklingGamesSum"))
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

    private Long parseLong(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Double parseDouble(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
