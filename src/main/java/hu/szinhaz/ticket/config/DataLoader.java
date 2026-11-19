package hu.szinhaz.ticket.config;

import hu.szinhaz.ticket.model.*;
import hu.szinhaz.ticket.repository.BookingRepository;
import hu.szinhaz.ticket.repository.PerformanceRepository;
import hu.szinhaz.ticket.repository.PerformanceSeatRepository;
import hu.szinhaz.ticket.repository.TheaterHallRepository;
import hu.szinhaz.ticket.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

    private final TheaterHallRepository hallRepository;
    private final PerformanceRepository performanceRepository;
    private final PerformanceSeatRepository seatRepository;
    private final BookingRepository bookingRepository;
    private final TicketRepository ticketRepository;

    public DataLoader(TheaterHallRepository hallRepository,
                      PerformanceRepository performanceRepository,
                      PerformanceSeatRepository seatRepository,
                      BookingRepository bookingRepository,
                      TicketRepository ticketRepository) {
        this.hallRepository = hallRepository;
        this.performanceRepository = performanceRepository;
        this.seatRepository = seatRepository;
        this.bookingRepository = bookingRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void run(String... args) {
        if (hallRepository.count() > 0) {
            return;
        }
        log.info("Mintaadatok betöltése a színházi jegyrendszerhez...");

        // Termek
        TheaterHall nagyszinpad = new TheaterHall(
                "Nagyszínpad",
                "A színház 80 fős, klasszikus reprezentatív nagyterme páhollyal és erkéllyel.",
                8, 10
        );
        TheaterHall kamaraszinpad = new TheaterHall(
                "Kamaraszínház",
                "Intim hangulatú, 48 fős stúdiószínpad a közvetlen nézői élményért.",
                6, 8
        );
        hallRepository.save(nagyszinpad);
        hallRepository.save(kamaraszinpad);

        // Fiktív, eredeti színházi előadások
        LocalDateTime now = LocalDateTime.now();

        Performance p1 = new Performance(
                "Az éjféli szonáta",
                "Varga Bálint",
                "Megindító zenés színmű egy rejtélyes zeneszerzőről és egy elfeledett dallamról, amely megváltoztatja egy kisváros lakóinak életét.",
                "Dráma",
                now.plusDays(3).withHour(19).withMinute(0).withSecond(0),
                BigDecimal.valueOf(4500),
                135,
                "https://images.unsplash.com/photo-1507676184212-d03ab07a01bf?auto=format&fit=crop&w=800&q=80",
                nagyszinpad
        );

        Performance p2 = new Performance(
                "A titokzatos kastély",
                "Kovács Dániel",
                "Fordulatos és humoros színpadi komédia váratlan vendégekről, félreértésekről és egy különös családi végrendeletről.",
                "Vígjáték",
                now.plusDays(5).withHour(18).withMinute(30).withSecond(0),
                BigDecimal.valueOf(4200),
                120,
                "https://images.unsplash.com/photo-1469488865564-c2de10f69f96?auto=format&fit=crop&w=800&q=80",
                nagyszinpad
        );

        Performance p3 = new Performance(
                "A csillagok vándora",
                "Németh Zoltán - Szabó Anna",
                "Látványos családi zenés játék a bátorságról, az összefogásról és az álmok erejéről, felejthetetlen dallamokkal.",
                "Musical",
                now.plusDays(7).withHour(15).withMinute(0).withSecond(0),
                BigDecimal.valueOf(5000),
                140,
                "https://images.unsplash.com/photo-1514306191717-452ec28c7814?auto=format&fit=crop&w=800&q=80",
                nagyszinpad
        );

        Performance p4 = new Performance(
                "Városi fények",
                "Molnár Eszter",
                "Lírai stúdiószínházi előadás két fiatal véletlen találkozásáról a nyüzsgő nagyváros forgatagában.",
                "Színmű",
                now.plusDays(10).withHour(19).withMinute(30).withSecond(0),
                BigDecimal.valueOf(3800),
                105,
                "https://images.unsplash.com/photo-1503095396549-807759245b35?auto=format&fit=crop&w=800&q=80",
                kamaraszinpad
        );

        performanceRepository.saveAll(List.of(p1, p2, p3, p4));

        populateSeatsForPerformance(p1, nagyszinpad);
        populateSeatsForPerformance(p2, nagyszinpad);
        populateSeatsForPerformance(p3, nagyszinpad);
        populateSeatsForPerformance(p4, kamaraszinpad);

        seedInitialBookings(p1, "01");
        seedInitialBookings(p3, "02");

        log.info("Mintaadatok betöltése befejezve.");
    }

    private void populateSeatsForPerformance(Performance performance, TheaterHall hall) {
        List<PerformanceSeat> seats = new ArrayList<>();
        int rows = hall.getRowsCount();
        int cols = hall.getColsCount();

        for (int r = 1; r <= rows; r++) {
            Sector sector;
            if (r <= 3) {
                sector = Sector.ZSOLLYE;
            } else if (r <= 6) {
                sector = Sector.ERKELY;
            } else {
                sector = Sector.PAHOLY;
            }

            for (int c = 1; c <= cols; c++) {
                PerformanceSeat seat = new PerformanceSeat(performance, r, c, sector);
                seats.add(seat);
            }
        }
        seatRepository.saveAll(seats);
        performance.setSeats(seats);
    }

    private void seedInitialBookings(Performance performance, String codeSuffix) {
        List<PerformanceSeat> seats = seatRepository.findByPerformanceIdOrderByRowNumberAscSeatNumberAsc(performance.getId());
        if (seats.size() < 10) return;

        PerformanceSeat s1 = seats.get(4);
        PerformanceSeat s2 = seats.get(5);
        PerformanceSeat s3 = seats.get(14);

        s1.setStatus(SeatStatus.SOLD);
        s2.setStatus(SeatStatus.SOLD);
        s3.setStatus(SeatStatus.SOLD);
        seatRepository.saveAll(List.of(s1, s2, s3));

        BigDecimal total = s1.calculatePrice().add(s2.calculatePrice()).add(s3.calculatePrice());

        Booking booking = new Booking(
                "THX-DEMO-" + codeSuffix,
                "Kovács András",
                "kovacs.andras@example.hu",
                "+36301234567",
                LocalDateTime.now().minusHours(2),
                total
        );
        bookingRepository.save(booking);

        Ticket t1 = new Ticket("TKT-2026-DEMO-" + codeSuffix + "-A", s1, booking, s1.calculatePrice(), LocalDateTime.now().minusHours(2));
        Ticket t2 = new Ticket("TKT-2026-DEMO-" + codeSuffix + "-B", s2, booking, s2.calculatePrice(), LocalDateTime.now().minusHours(2));
        Ticket t3 = new Ticket("TKT-2026-DEMO-" + codeSuffix + "-C", s3, booking, s3.calculatePrice(), LocalDateTime.now().minusHours(2));

        ticketRepository.saveAll(List.of(t1, t2, t3));
        booking.setTickets(List.of(t1, t2, t3));
    }
}
