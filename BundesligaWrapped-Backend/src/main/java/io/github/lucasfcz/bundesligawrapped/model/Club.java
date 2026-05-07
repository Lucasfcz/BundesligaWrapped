package io.github.lucasfcz.bundesligawrapped.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "clubs")
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "club_id", unique = true, nullable = false)
    private String clubId;

    @Column(name = "club_name")
    private String clubName;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "three_letter_code")
    private String threeLetterCode;

    @Column(name = "stadium_name")
    private String stadiumName;

    private Integer founded;
    private String city;

    @Column(name = "color_primary")
    private String colorPrimary;

    @Column(name = "color_secondary")
    private String colorSecondary;

    public Club(String clubId, String clubName, String shortName, String threeLetterCode, String stadiumName, Integer founded, String city, String colorPrimary, String colorSecondary) {
        this.clubId = clubId;
        this.clubName = clubName;
        this.shortName = shortName;
        this.threeLetterCode = threeLetterCode;
        this.stadiumName = stadiumName;
        this.founded = founded;
        this.city = city;
        this.colorPrimary = colorPrimary;
        this.colorSecondary = colorSecondary;
    }
}
