package org.example.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Główna klasa aplikacji Spring Boot.
 * Służy jako punkt wejścia do aplikacji i konfiguruje podstawowe ustawienia.
 */
@SpringBootApplication
public class BackendApplication {

	/**
	 * Metoda główna uruchamiająca aplikację Spring Boot.
	 * @param args argumenty wiersza poleceń
	 */
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
