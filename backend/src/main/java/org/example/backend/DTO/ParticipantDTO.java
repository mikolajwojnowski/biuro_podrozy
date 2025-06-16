package org.example.backend.DTO;

/**
 * Klasa reprezentująca uczestnika rezerwacji.
 * Zawiera podstawowe dane uczestnika wycieczki.
 */
public class ParticipantDTO {
    /**
     * Imię uczestnika.
     */
    private String name;

    /**
     * Nazwisko uczestnika.
     */
    private String surname;

    /**
     * Domyślny konstruktor.
     */
    public ParticipantDTO() {
    }

    /**
     * Tworzy nowego uczestnika z podanymi danymi.
     * @param name imię uczestnika
     * @param surname nazwisko uczestnika
     */
    public ParticipantDTO(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    /**
     * Zwraca imię uczestnika.
     * @return imię uczestnika
     */
    public String getName() {
        return name;
    }

    /**
     * Ustawia imię uczestnika.
     * @param name imię uczestnika
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Zwraca nazwisko uczestnika.
     * @return nazwisko uczestnika
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Ustawia nazwisko uczestnika.
     * @param surname nazwisko uczestnika
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }
} 