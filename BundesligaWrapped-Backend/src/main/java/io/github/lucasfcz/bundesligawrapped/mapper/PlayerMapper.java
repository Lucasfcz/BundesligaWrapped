package io.github.lucasfcz.bundesligawrapped.mapper;

import io.github.lucasfcz.bundesligawrapped.model.Player;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class PlayerMapper {

    public Player toEntity(Element element) {
        return new Player(
                element.getAttribute("ObjectId"),
                element.getAttribute("ClubId"),
                element.getAttribute("FirstName"),
                element.getAttribute("LastName"),
                element.getAttribute("Alias"),
                parseInteger(element.getAttribute("ShirtNumber")),
                element.getAttribute("PlayingPositionEnglish"),
                element.getAttribute("BirthDate"),
                element.getAttribute("NationalityEnglish"),
                parseInteger(element.getAttribute("Height")),
                parseInteger(element.getAttribute("Weight")),
                parseDate(element.getAttribute("LeaveDate"))
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

    private LocalDate parseDate(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            return LocalDate.parse(value, formatter);
        } catch (Exception e) {
            return null;
        }
    }
}
