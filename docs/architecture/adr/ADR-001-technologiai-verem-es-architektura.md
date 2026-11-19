# ADR-001: Technológiai verem és rétegzett architektúra kiválasztása

## Státusz
Elfogadva (Accepted)

## Dátum
2026-11-17

## Kontextus és Problémafelvetés
A Színház Jegyértékesítő Rendszer (ThaliaTickets) célja, hogy modern, reszponzív felületen tegye lehetővé a színházi előadások böngészését, az interaktív nézőtéri székválasztást, a jegyvásárlást, valamint az adminisztrációs feladatok és statisztikák ellátását.
Olyan stabil, hordozható, típusbiztos és vállalati környezetben is bevált technológiai veremre van szükség, amely:
- Gyors fejlesztést tesz lehetővé, beépített megoldásokkal az adatbázis-kezelésre, biztonságra és webes kiszolgálásra.
- Minimális külső függőséggel futtatható (beágyazott szerver, önálló futtatású JAR).
- Megfelel a platformfüggetlenségi (Windows, Linux, macOS) elvárásoknak.
- Könnyen tesztelhető JUnit keretrendszerrel.

## Döntési Megfontolások (Drivers)
1. Fejlesztési sebesség és kiforrott ökoszisztéma.
2. Karbantarthatóság és tiszta rétegzett architektúra (Separation of Concerns).
3. ACID tranzakciókezelés a pénzügyi és székfoglalási integritás garantálására.
4. Könnyű telepíthetőség és tesztkörnyezet biztosítása.

## Mérlegelt Opciók
- **1. opció:** Java (OpenJDK) + Spring Boot keretrendszer + Spring Data JPA + H2 / PostgreSQL + Thymeleaf / Vanilla JS
- **2. opció:** Node.js + Express + TypeORM + React SPA
- **3. opció:** Python + Django / Flask + SQLite + Jinja2

## Kiválasztott Döntés
Az **1. opciót** választottuk: **Java (Spring Boot 3) alapú moduláris monolit architektúra**, Spring Data JPA perzisztenciaréteggel, beágyazott H2 adatbázissal (termelésben PostgreSQL kompatibilis dialektussal) és Thymeleaf szerveroldali sablonmotorral, kliensoldali interaktív nézőtéri JavaScript/SVG komponenssel.

### Architekturális rétegek:
1. **Prezentációs réteg (Web / Controller Layer):**
   - Spring MVC Controller-ek a HTML oldalak kiszolgálására (`PerformanceController`, `BookingController`, `TicketController`, `AdminController`).
   - REST Controller a valós idejű ülésállapotok lekérdezéséhez (`BookingApiController`).
2. **Üzleti logikai réteg (Service Layer):**
   - `PerformanceService`: Előadások kezelése, keresés, szűrés, műsorfüzet.
   - `BookingService`: Foglalások validálása, jegykiállítás, visszaváltás, statisztikák.
   - Tranzakcióhatárok `@Transactional` annotációval a konzisztencia biztosítására.
3. **Adathozzáférési réteg (Repository Layer):**
   - Spring Data JPA interfészek (`PerformanceRepository`, `SeatRepository`, `BookingRepository`, `TicketRepository`).
4. **Adatbázis réteg (Database Layer):**
   - H2 in-memory / file-based relációs adatbázis JPA / Hibernate entitás-leképezéssel.

## Következmények és Előnyök

### Pozitívumok:
- **Nulla külső szoftvertelepítési igény:** A beágyazott Apache Tomcat és a H2 adatbázis révén a projekt egyetlen Maven paranccsal (`mvn spring-boot:run`) azonnal elindítható és demonstrálható.
- **Erős típusosság és megbízhatóság:** A Java nyelv fordítási idejű típusellenőrzése és a Hibernate relációs leképezése minimalizálja a futásidejű hibákat.
- **Tranzakciós garanciák:** A Spring deklaratív tranzakciókezelése zökkenőmentessé teszi az atomi műveletek végrehajtását (pl. székfoglalás és jegygenerálás).
- **Kiváló tesztelhetőség:** A beépített Spring Boot Test és Mockito segítségével az üzleti logika izoláltan tesztelhető.

### Negatívumok / Kompromisszumok:
- A JVM indulási ideje minimálisan magasabb lehet, mint egy egyszerű Node.js szkripté, de cserébe hosszú távon magasabb áteresztőképességet és memóriabiztonságot nyújt.
