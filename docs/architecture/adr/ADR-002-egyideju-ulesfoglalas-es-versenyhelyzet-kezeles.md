# ADR-002: Egyidejű ülésfoglalások és versenyhelyzetek (Race Conditions) kezelése

## Státusz
Elfogadva (Accepted)

## Dátum
2026-11-17

## Kontextus és Problémafelvetés
Egy színházi jegyértékesítő rendszerben a legkritikusabb üzleti kockázat a **dupla foglalás (double-booking)**.
Amikor egy népszerű előadásra (pl. premierre) egyszerre több száz látogató próbálja ugyanazt a kiváló helyet (pl. Zsöllye 1. sor 10. szék) lefoglalni, versenyhelyzet (Race Condition) alakulhat ki:
- Felhasználó A és Felhasználó B egyszerre látja a széket szabadnak.
- Mindketten beküldik a fizetési és foglalási szándékot azonos ezredmásodpercben.
- Megfelelő zárolási stratégia nélkül a rendszer mindkét kérést jóváhagyhatná, ami súlyos üzleti és presztízsveszteséget okozna a színháznak.

## Döntési Megfontolások
- Garantált adatkonzisztencia (senki se vehesse meg a már lefoglalt/eladott széket).
- Jó válaszidő és minimális holtpont (deadlock) kockázat.
- Áttekinthető, JPA szabványt támogató implementáció.

## Mérlegelt Opciók
1. **Alkalmazásszintű szinkronizáció (`synchronized` blokk Java memóriában):**
   - *Hátrány:* Csak egyetlen szerverpéldány esetén működik; elosztott/többpéldányos futtatáskor nem véd.
2. **Optimistic Locking (`@Version` mező):**
   - *Előny:* Nem blokkolja az adatbázis sorait olvasáskor.
   - *Hátrány:* Magas párhuzamosság esetén gyakori `OptimisticLockException` kivételekhez vezet, a felhasználónak újra kell próbálkoznia.
3. **Pessimistic Locking (`LockModeType.PESSIMISTIC_WRITE` a tranzakcióban) + egyedi adatbázis index:**
   - *Előny:* Az adatbázis szintjén zárolja az érintett széksorokat a tranzakció lezárásáig (`SELECT ... FOR UPDATE`). Aki előbb zárol, az sikeresen foglalhat, a másik kérés pedig azonnal hibát vagy friss állapotot kap.
   - *Kiegészítés:* Időkorlátos foglalási státusz (`RESERVED` 10 perc lejárattal vagy azonnali tranzakciós `SOLD`).

## Kiválasztott Döntés
A rendszer a **Pessimistic Locking** stratégiát és tranzakciós izolációt alkalmazza:
- A `PerformanceSeatRepository` `findByIdForUpdate` metódusa `@Lock(LockModeType.PESSIMISTIC_WRITE)` annotációval van ellátva.
- A foglalási szolgáltatás (`BookingService.createBooking`) `@Transactional(isolation = Isolation.READ_COMMITTED)` környezetben fut.
- A foglaláskor a rendszer sorban zárolja a kiválasztott székeket, ellenőrzi, hogy a státuszuk `FREE`. Ha bármelyik szék állapota megváltozott időközben, a tranzakció azonnal visszagörget (Rollback) és `IllegalStateException` kivételt dob felhasználóbarát hibaüzenettel.
- Emellett a `Ticket` entitás egyedi kényszert (Unique Constraint) tart fenn az előadás-ülés párosra.

## Következmények
- **Előnyök:** 100%-os garancia arra, hogy egy széket nem lehet kétszer eladni azonos előadásra még extrém egyidejű terhelés mellett sem.
- **Kompromisszumok:** A zárolási művelet minimális várakozást jelenthet a lock feloldásáig, de mivel a tranzakciók rövidek (néhány millimásodperc), ez a felhasználói élményt és a skálázódást nem korlátozza.
