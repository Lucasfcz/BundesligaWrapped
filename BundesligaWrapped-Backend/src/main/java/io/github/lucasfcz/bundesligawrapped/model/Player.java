package io.github.lucasfcz.bundesligawrapped.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "person_id", unique = true, nullable = false)
    private String personId;

    @Column(name = "club_id", nullable = false)
    private String clubId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String alias;

    @Column(name = "shirt_number")
    private Integer shirtNumber;

    private String position;

    @Column(name = "birth_date")
    private String birthDate;

    private String nationality;
    private Integer height;
    private Integer weight;

    @Column(name = "leave_date")
    private LocalDate leaveDate;

    public Player(String personId, String clubId, String firstName, String lastName, String alias, Integer shirtNumber, String position, String birthDate, String nationality, Integer height, Integer weight, LocalDate leaveDate) {
        this.personId = personId;
        this.clubId = clubId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.alias = alias;
        this.shirtNumber = shirtNumber;
        this.position = position;
        this.birthDate = birthDate;
        this.nationality = nationality;
        this.height = height;
        this.weight = weight;
        this.leaveDate = leaveDate;
    }
}