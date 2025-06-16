# System Zarządzania Biurem Podróży

System do zarządzania biurem podróży z interfejsem JavaFX i backendem Spring Boot.

## Wymagania wstępne

- JDK 17 lub nowszy
- Maven 3.6.3 lub nowszy
- Git
- IDE (IntelliJ IDEA, Eclipse lub VS Code)

## Instalacja

1. Sklonuj repozytorium:
```bash
git clone [URL_REPOZYTORIUM]
cd biuropodrozy
```

2. Skonfiguruj backend:
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

3. Skonfiguruj frontend:
```bash
cd frontend
mvn clean install
mvn javafx:run
```

## Struktura projektu

```
biuropodrozy/
├── backend/                 # Backend Spring Boot
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/       # Kod źródłowy Java
│   │   │   └── resources/  # Pliki konfiguracyjne
│   │   └── test/          # Testy
│   └── pom.xml            # Konfiguracja Maven
│
└── frontend/              # Frontend JavaFX
    ├── src/
    │   ├── main/
    │   │   ├── java/     # Kod źródłowy Java
    │   │   └── resources/# Zasoby (FXML, CSS)
    │   └── test/        # Testy
    └── pom.xml          # Konfiguracja Maven
```

## Funkcjonalności

- **Uwierzytelnianie użytkowników**
  - Logowanie
  - Rejestracja
  - Role użytkowników (ADMIN/USER)

- **Zarządzanie wycieczkami**
  - Dodawanie nowych wycieczek
  - Edycja istniejących wycieczek
  - Usuwanie wycieczek
  - Przeglądanie dostępnych wycieczek

- **Zarządzanie rezerwacjami**
  - Tworzenie rezerwacji
  - Anulowanie rezerwacji
  - Przeglądanie rezerwacji
  - Generowanie PDF z rezerwacją

## Domyślne konto administratora

- Email: admin@example.com
- Hasło: admin123

## Rozwój

### Backend
- Pliki konfiguracyjne znajdują się w `backend/src/main/resources/`
- Główna klasa aplikacji: `BackendApplication.java`
- Endpointy API: `/api/*`

### Frontend
- Widoki FXML: `frontend/src/main/resources/views/`
- Style CSS: `frontend/src/main/resources/styles/`
- Główna klasa aplikacji: `FrontendApplication.java`

## Rozwiązywanie problemów

1. **Problem z portem**
   - Backend domyślnie używa portu 8080
   - Jeśli port jest zajęty, zmień go w `application.properties`

2. **Błąd budowania Maven**
   - Upewnij się, że masz zainstalowane JDK 17
   - Wykonaj `mvn clean install` w obu katalogach

3. **Problem z połączeniem frontend-backend**
   - Sprawdź, czy backend jest uruchomiony
   - Sprawdź adres URL w konfiguracji frontendu

## Współtworzenie

1. Utwórz fork repozytorium
2. Utwórz nową gałąź (`git checkout -b feature/nazwa-funkcjonalnosci`)
3. Wprowadź zmiany i zatwierdź je (`git commit -am 'Dodano nową funkcjonalność'`)
4. Wypchnij zmiany do forka (`git push origin feature/nazwa-funkcjonalnosci`)
5. Utwórz Pull Request

## Licencja

Ten projekt jest udostępniany na licencji MIT. Szczegóły znajdują się w pliku LICENSE. 