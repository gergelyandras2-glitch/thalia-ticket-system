# Színházi Jegyértékesítő Rendszer (ThaliaTickets)

Modern, reszponzív Java Spring Boot alapú webalkalmazás színházi előadások böngészésére, interaktív nézőtéri székválasztásra, elektronikus jegyvásárlásra és adminisztrációra.

A projekt a szoftverfejlesztési és rendszertervezési követelményeknek megfelelően készült, tartalmazza a teljes forráskódot, a követelményspecifikációt, az UML diagramokat (.drawio és .png) és az Architectural Decision Record (ADR) dokumentációt.

---

## 📁 A Projekt Mappaszerkezete

```
project/
├── docs/
│   ├── requirements.md               # 2. feladat: Funkcionális és nem funkcionális követelmények
│   └── architecture/                 # 3. feladat: UML tervek és architektúra
│       ├── use_case.drawio           # Use Case diagram (draw.io)
│       ├── use_case.png              # Use Case diagram (kép)
│       ├── class_diagram.drawio      # Osztálydiagram (draw.io)
│       ├── class_diagram.png         # Osztálydiagram (kép)
│       ├── sequence_diagram.drawio   # Szekvenciadiagram (draw.io)
│       ├── sequence_diagram.png      # Szekvenciadiagram (kép)
│       └── adr/
│           ├── ADR-001-technologiai-verem-es-architektura.md
│           └── ADR-002-egyideju-ulesfoglalas-es-versenyhelyzet-kezeles.md
├── src/
│   ├── main/
│   │   ├── java/hu/szinhaz/ticket/
│   │   │   ├── TheaterApplication.java
│   │   │   ├── config/DataLoader.java
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   └── util/DiagramGenerator.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/ (css/styles.css, js/seats.js)
│   │       └── templates/ (performances, seat-selection, checkout, confirmation, ticket-view, admin)
│   └── test/java/hu/szinhaz/ticket/
│       ├── BookingServiceTest.java
│       └── PerformanceServiceTest.java
└── pom.xml
```

---

## 🛠️ Alkalmazott Technológiák

- **Nyelv:** Java 17 / 21 / 25
- **Keretrendszer:** Spring Boot 3.3.4 (Spring MVC, Spring Data JPA, Spring Validation)
- **Adatbázis:** H2 beágyazott relációs adatbázis (in-memory, automatikus sémaépítés)
- **Sablonmotor és Felület:** Thymeleaf, modern HTML5, CSS3 és JavaScript
- **Tranzakciókezelés & Zárolás:** JPA Pessimistic Locking (`PESSIMISTIC_WRITE`) a kettős foglalás (double-booking) kizárására
- **Tesztelés:** JUnit 5, Spring Boot Test

---

## 🚀 Futtatási Útmutató

### 1. Előfeltételek
- Telepített Java Runtime / JDK (Java 17 vagy újabb)
- Apache Maven (vagy beépített Maven az IDE-ben)

### 2. Alkalmazás indítása parancssorból
Nyisson meg egy terminált a `project` mappában, majd futtassa:

```bash
mvn spring-boot:run
```

Vagy hozzon létre futtatható JAR állományt:

```bash
mvn clean package
java -jar target/thalia-ticket-system-1.0.0.jar
```

### 3. Az alkalmazás elérése
Az indítást követően nyissa meg a böngészőt:
- **Kezdőlap / Műsorfüzet:** [http://localhost:8080/performances](http://localhost:8080/performances)
- **Jegykezelés és Lemondás:** [http://localhost:8080/tickets/lookup](http://localhost:8080/tickets/lookup)
- **Adminisztrációs Vezérlőpult:** [http://localhost:8080/admin](http://localhost:8080/admin)
- **H2 Adatbázis Konzol:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (JDBC URL: `jdbc:h2:mem:szinhazdb`, Felhasználó: `sa`, Jelszó: *üres*)

---

## 🧪 Automatizált Tesztek Futtatása

A tesztek ellenőrzik az ülésfoglalást, az egyidejű zárolást és a műsorok keresését:

```bash
mvn test
```

---

## 📋 Megvalósított Követelmények és Mellékletek

1. **Követelményspecifikáció (`docs/requirements.md`):**
   - Felhasználói történetek (User Story) és Acceptance Criteria megfogalmazások.
   - Nem funkcionális követelmények (Teljesítmény, Biztonság, Megbízhatóság, Használhatóság, Hordozhatóság).
   - Nyomonkövethetőségi mátrix.
2. **Architektúra és UML diagramok (`docs/architecture/`):**
   - **Use Case diagram:** `use_case.drawio` és `use_case.png`
   - **Osztálydiagram:** `class_diagram.drawio` és `class_diagram.png`
   - **Szekvenciadiagram:** `sequence_diagram.drawio` és `sequence_diagram.png`
3. **Rendszertervezési Döntések (`docs/architecture/adr/`):**
   - `ADR-001`: Technológiai verem és rétegzett architektúra kiválasztása.
   - `ADR-002`: Egyidejű ülésfoglalások és versenyhelyzetek kezelése Pessimistic Lock segítségével.
