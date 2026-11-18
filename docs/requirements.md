# Követelményspecifikáció (Requirements Specification)
## Projekt: Színházi Jegyértékesítő és Nézőtér-kezelő Rendszer (ThaliaTickets)

A dokumentum a Színház Jegyértékesítő Rendszer funkcionális és nem funkcionális követelményeit, valamint a hozzájuk tartozó felhasználói történeteket (User Stories) és elfogadási kritériumokat (Acceptance Criteria) rögzíti.

---

## 1. Csapattagok és Szerepkörök

| Név | Szerepkör | Felelősségi terület |
|---|---|---|
| **Fejlesztő 1** | Backend & Adatbázis mérnök | Színházi előadások kezelése, ülésrend modellezés, tranzakciókezelés |
| **Fejlesztő 2** | Full-Stack & Tranzakciós fejlesztő | Interaktív székválasztó, foglalási és fizetési folyamat, versenyhelyzet-kezelés |
| **Fejlesztő 3** | Frontend & UX mérnök | E-jegy kiállítás, keresés és szűrés, reszponzív felület, akadálymentesítés |
| **Fejlesztő 4** | DevOps & Rendszermérnök | Adminisztrációs felület, statisztikák, biztonság, nem funkcionális követelmények |

---

## 2. Funkcionális Követelmények (Functional Requirements)

A követelmények a kért formátumban készültek:
- **ID, Title**
- **Description:** `As a <type of user>, I want <the goal> so that <the reason>.`
- **Acceptance criterias:** `Given <the condition>, when <the action> then <the outcome>.`

### Csapattag 1: Előadások és Műsorterv kezelése

#### FR-01: Előadások és műsorfüzet böngészése
- **Description:** As a színházlátogató, I want böngészni az aktuális színházi műsorfüzetet dátum, cím és műfaj alapján so that kiválaszthassam az engem érdeklő színházi darabot.
- **Acceptance criterias:**
  - *Given* a felhasználó megnyitja a színház kezdőlapját, *when* kiválasztja a "Dráma" kategóriát vagy beír egy címet a keresőmezőbe, *then* a rendszer kizárólag a feltételeknek megfelelő aktív előadásokat jeleníti meg, feltüntetve azok időpontját, helyszínét és a jegyárak kezdő összegét.
  - *Given* nincs találat a keresési feltételekre, *when* a szűrés lefut, *then* a rendszer egy informatív üzenetet jelenít meg a felhasználónak javaslatokkal a keresés bővítésére.

#### FR-02: Részletes előadás adatlap megtekintése
- **Description:** As a színházlátogató, I want megtekinteni a kiválasztott előadás részletes ismertetőjét, szereposztását és időtartamát so that tájékozott döntést hozhassak a jegyvásárlás előtt.
- **Acceptance criterias:**
  - *Given* a látogató az előadáslistában rákattint egy adott színdarabra, *when* betöltődik az előadás adatlapja, *then* látható a darab szerzője, rendezője, hossza, korhatárbesorolása, szinopszisa és az elérhető időpontok listája "Jegyvásárlás" gombbal.

---

### Csapattag 2: Székválasztás és Jegyvásárlás

#### FR-03: Interaktív nézőtéri székválasztás valós idejű állapotkijelzéssel
- **Description:** As a jegyvásárló, I want egy interaktív nézőtéri térképen közvetlenül kiválasztani a kívánt ülőhelyeket szektorok (Zsöllye, Erkély, Páholy) szerint so that a számomra legkényelmesebb és legmegfelelőbb árkategóriájú helyekre ülhessek.
- **Acceptance criterias:**
  - *Given* a vásárló kiválaszt egy konkrét előadási időpontot, *when* megnyitja a székválasztó felületet, *then* megjelenik a nézőtér vizuális elrendezése, ahol a szabad székek zöld/arany színnel, a már foglalt székek szürke inaktív állapotban látszanak, a székekre kattintva pedig azok kijelölt állapotba kerülnek és a kosár összege azonnal frissül.
  - *Given* egy szék már korábban eladásra került vagy másik felhasználó által foglalás alatt áll, *when* a felhasználó rákattint, *then* a rendszer nem engedi a kijelölést és figyelmeztető jelzést ad.

#### FR-04: Jegyvásárlás és tranzakció lebonyolítása
- **Description:** As a vásárló, I want megadni a számlázási és kapcsolattartási adataimat és véglegesíteni a vásárlást so that a rendszer hitelesítse a foglalást és kiállítsa a jegyeket.
- **Acceptance criterias:**
  - *Given* a vásárló kijelölt legalább egy szabad széket és kitöltötte a kötelező mezőket (név, érvényes e-mail cím, telefonszám), *when* a "Fizetés és jegykiállítás" gombra kattint, *then* a rendszer tranzakció keretében rögzíti a foglalást, a székek státuszát 'SOLD' állapotra állítja, egyedi rendelési azonosítót generál, és átirányít a sikeres vásárlás visszaigazoló oldalára.
  - *Given* a vásárló hiányos vagy érvénytelen e-mail címet ad meg, *when* megpróbálja elküldeni az űrlapot, *then* a rendszer mezőszintű validációs hibaüzenetet jelenít meg és nem von le összeget.

---

### Csapattag 3: Digitális Jegykezelés és Visszaváltás

#### FR-05: E-jegy és virtuális belépőkártya megtekintése / letöltése
- **Description:** As a jegyvásárló, I want megtekinteni és letölthető/nyomtatható formátumban elérni a digitális e-jegyemet egyedi QR/vonalkóddal so that a színház bejáratánál gyorsan és papírmentesen igazolhassam a jogosultságomat.
- **Acceptance criterias:**
  - *Given* egy sikeres vásárlás utáni visszaigazolás vagy jegykeresés a jegykód alapján, *when* a felhasználó megnyitja a jegy adatlapját, *then* látható az előadás neve, időpontja, a terem, a szektor, a sor és a székszám, valamint az egyedi beolvasható QR-kód grafika, és elérhető egy szabványos nyomtatási nézet.

#### FR-06: Jegykeresés és jegylemondás / visszaváltás
- **Description:** As a jegytulajdonos, I want visszaváltani a megvásárolt jegyemet az előadás kezdete előtt legalább 24 órával a foglalási kód megadásával so that visszakaphassam a jegy árát, ha váratlanul akadályoztatva vagyok.
- **Acceptance criterias:**
  - *Given* a felhasználó megadja a foglalási kódját és e-mail címét a jegykezelő menüben, *when* a "Jegy lemondása" opciót választja és az előadásig több mint 24 óra van hátra, *then* a rendszer törli a foglalást, a székek státuszát újra 'FREE' (szabad) állapotra állítja, és jóváíró visszaigazolást állít ki.
  - *Given* az előadás kezdetéig kevesebb mint 24 óra van hátra, *when* a felhasználó lemondást kezdeményez, *then* a rendszer elutasítja a kérést és tájékoztatja az üzletszabályzat szerinti határidő lejártáról.

---

### Csapattag 4: Adminisztráció és Teremmenedzsment

#### FR-07: Új előadás meghirdetése és szerkesztése (Adminisztráció)
- **Description:** As a színházi adminisztrátor, I want új előadásokat és időpontokat rögzíteni a rendszerben, megadva a darab címét, termét és az alapjegyárat so that a közönség számára elérhetővé váljon az online jegyértékesítés.
- **Acceptance criterias:**
  - *Given* az adminisztrátor belép a vezérlőpultra és kitölti az új előadás űrlapját (cím, műfaj, kezdési idő, terem, alapár), *when* a "Mentés" gombra kattint, *then* a rendszer létrehozza az előadást, automatikusan legenerálja a teremhez tartozó ülésmátrixot alapértelmezett 'FREE' státusszal, és az előadás megjelenik a nyilvános műsorfüzetben.

#### FR-08: Kihasználtsági és bevételi statisztikák megtekintése
- **Description:** As a színház vezetőségi tagja / adminisztrátora, I want összesített kimutatást látni a darabonkénti jegyeladásokról, bevételről és a nézőtéri telítettségről so that üzletileg megalapozott döntéseket hozhassunk a műsorstruktúráról.
- **Acceptance criterias:**
  - *Given* az adminisztrátor megnyitja a statisztikai panelt, *when* kiválaszt egy időszakot vagy előadást, *then* a felületen megjelenik a teljes jegybevétel (Ft), az eladott székek száma, a százalékos kihasználtság, valamint a foglalások tételes listája.

---

## 3. Nem Funkcionális Követelmények (Non-Functional Requirements)

A feladatkiírásnak megfelelően csapattagonként 2-2 minőségi követelmény:

### Csapattag 1: Teljesítmény és Skálázhatóság (Performance)

- **NFR-01 (Válaszidő és Betöltési Sebesség):**
  - **Elvárás:** A rendszer webes felületének főoldala és az előadáskereső válaszideje átlagos hálózati feltételek mellett nem haladhatja meg az **1.0 másodpercet** (95th percentile < 1000 ms).
  - **Mérés/Ellenőrzés:** Böngésző Developer Tools (Lighthouse Performance Score >= 90) és JMeter terhelésteszt.

- **NFR-02 (Egyidejű Terhelés és Kapacitás):**
  - **Elvárás:** A rendszernek zökkenőmentesen kell kezelnie legalább **500 egyidejű aktív felhasználót** (concurrent sessions) csúcsidőszakban (pl. új kasszasiker darab jegyértékesítésének kezdetekor), 0% tranzakciós adatvesztés mellett.

---

### Csapattag 2: Biztonság és Adatvédelem (Security & Compliance)

- **NFR-03 (Adatvédelem és GDPR megfelelőség):**
  - **Elvárás:** A rendszer kizárólag a jegyértékesítéshez és számlázáshoz elengedhetetlen személyes adatokat tárolhatja (név, e-mail, telefonszám). Az érzékeny adatokat az adatbázisban és az adatátvitel során (TLS/HTTPS protokollal, AES-256 / SHA-256 alapú titkosítással) védeni kell. A felhasználónak biztosítani kell a jegytörléssel egybekötött "elfeledtetéshez való jogot".

- **NFR-04 (Integritás és Pénzügyi Tranzakciós Biztonság):**
  - **Elvárás:** A rendszernek hardveres és szoftveres szinten meg kell akadályoznia a párhuzamos foglalásokból eredő túlfoglalást (double-booking). A foglalási tranzakcióknak szigorúan ACID tulajdonságokkal kell rendelkezniük (Optimistic / Pessimistic Locking mechanizmussal az ülések szintjén). SQL Injection és Cross-Site Scripting (XSS) / CSRF támadások ellen beépített framework védelmet kell alkalmazni.

---

### Csapattag 3: Használhatóság és Akadálymentesség (Usability & Accessibility)

- **NFR-05 (Intuitív Felhasználói Élmény és Ergonómia):**
  - **Elvárás:** A jegyvásárlási folyamatnak (előadás kiválasztása -> székfoglalás -> fizetési adatok megadása -> e-jegy letöltése) legfeljebb **3 kattintásból** és átlagosan **90 másodperc alatt** elvégezhetőnek kell lennie egy átlagos felhasználó számára, előzetes betanulás nélkül.

- **NFR-06 (Akadálymentesség és Vizuális Kontraszt):**
  - **Elvárás:** A felhasználói felületnek meg kell felelnie a **WCAG 2.1 AA** szabvány előírásainak (megfelelő kontrasztarány a szöveg és háttér között, képernyőolvasó barát szemantikus HTML5 elemek, színvakok számára is megkülönböztethető székállapot-jelölések ikonokkal kiegészítve).

---

### Csapattag 4: Megbízhatóság és Hordozhatóság (Reliability & Portability)

- **NFR-07 (Rendelkezésre állás és Hibatűrés - Reliability):**
  - **Elvárás:** A rendszer éves szinten legalább **99.9% rendelkezésre állást (uptime)** garantál (évente maximum 8.76 óra nem tervezett leállás). Adatbázis hiba vagy szerver újraindulás esetén a már véglegesített tranzakciók állapota nem sérülhet (ACID perzisztencia).

- **NFR-08 (Hordozhatóság és Platformfüggetlenség - Portability):**
  - **Elvárás:** Az alkalmazásnak szabványos Java Virtual Machine környezetben (Java 17 LTS / Java 21 LTS / Java 25) platformfüggetlenül kell futnia Windows, Linux és macOS operációs rendszereken. A kliensoldalnak szabványos böngészőkben (Google Chrome, Mozilla Firefox, Safari, Microsoft Edge) asztali és mobil nézetben (reszponzív, 360px - 4K felbontás) hibátlanul kell renderelődnie külső bővítmények nélkül.

---

## 4. Nyomonkövethetőségi Mátrix (Traceability Matrix)

| Követelmény ID | Leírás | Tervezési elem / Architektúra komponens | Tesztelési megvalósítás |
|---|---|---|---|
| **FR-01** | Előadások böngészése | `PerformanceController`, `PerformanceService` | `PerformanceServiceTest.testFindAll()` |
| **FR-02** | Részletes adatlap | `PerformanceController`, `performance-details.html` | `PerformanceControllerTest.testDetailsPage()` |
| **FR-03** | Nézőtér és székválasztás | `BookingController`, `PerformanceSeatRepository` | `BookingServiceTest.testGetAvailableSeats()` |
| **FR-04** | Jegyvásárlás és tranzakció | `BookingService.createBooking()`, Pessimistic Lock | `BookingServiceTest.testSuccessfulBooking()` |
| **FR-05** | E-jegy kiállítás | `TicketController`, `ticket-view.html` | `BookingServiceTest.testTicketGeneration()` |
| **FR-06** | Jegy visszaváltása | `BookingService.cancelBooking()` | `BookingServiceTest.testCancelBooking()` |
| **FR-07** | Előadás adminisztráció | `AdminController`, `PerformanceService.create()` | `AdminControllerTest.testCreatePerformance()` |
| **FR-08** | Statisztikák és kimutatások| `AdminController`, `BookingRepository.sumRevenue()` | `AdminControllerTest.testStatistics()` |
