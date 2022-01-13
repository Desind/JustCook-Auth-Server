# Instrukcja uruchomienia projektu

#### Wymagania oprogramowania:
Docker desktop:   
https://hub.docker.com/editions/community/docker-ce-desktop-windows

Java SDK w wersji minimum 16:\
Ważne, żeby podczas instalacji java została dodana do ścieżki systemowej.
Zweryfikować to można poprzez wpisanie w konsolę
`java -version`

#### Użyte technologii:
- Backend: Spring Boot (Java)
- Baza danych: MongoDB (znajdujące się w kontenerze dockera)

## Uruchomienie serwera

1. Uruchomienie Docker Desktop
2. Z folderu głównego należy przejść do /auth-server oraz uruchomić polecenie `docker-compose up`
3. Z tego samego folderu w środowisku InteliJ IDEA wcisnąć skrót klawiszowy SHIFT + F10, który uruchomi projekt
4. Podgląd bazy danych będzie znajdował się pod adresem http://localhost:8081 (Baza danych oraz odpowiednia kolekcja zostaną utworzone dopiero po rejestracji pierwszego użytkownika)
