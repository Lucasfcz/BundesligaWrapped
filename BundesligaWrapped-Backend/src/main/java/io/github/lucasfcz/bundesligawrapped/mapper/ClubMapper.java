package io.github.lucasfcz.bundesligawrapped.mapper;

import io.github.lucasfcz.bundesligawrapped.model.Club;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

@Component
public class ClubMapper {

    public Club toEntity(Element element) {
        return new Club(
                element.getAttribute("ClubId"),
                element.getAttribute("LongName"),
                element.getAttribute("ShortName"),
                element.getAttribute("ThreeLetterCode"),
                element.getAttribute("StadiumName"),
                parseInteger(element.getAttribute("Founded")),
                element.getAttribute("City"),
                element.getAttribute("ClubColorOneRGB"),
                element.getAttribute("ClubColorTwoRGB")
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
}
