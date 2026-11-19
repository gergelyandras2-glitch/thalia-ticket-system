package hu.szinhaz.ticket.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class DiagramGenerator {

    public static void main(String[] args) throws Exception {
        File outDir = new File("C:/Users/USER/Desktop/project/docs/architecture");
        outDir.mkdirs();

        generateUseCaseDiagram(new File(outDir, "use_case.png"));
        generateClassDiagram(new File(outDir, "class_diagram.png"));
        generateSequenceDiagram(new File(outDir, "sequence_diagram.png"));
        System.out.println("ALL DIAGRAMS SUCCESSFULLY GENERATED!");
    }

    private static void setupGraphics(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }

    public static void generateUseCaseDiagram(File file) throws Exception {
        int w = 1200, h = 800;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        setupGraphics(g);

        // Background
        g.setColor(new Color(248, 250, 252));
        g.fillRect(0, 0, w, h);

        // Title
        g.setFont(new Font("Segoe UI", Font.BOLD, 22));
        g.setColor(new Color(15, 23, 42));
        g.drawString("Use Case Diagram - Színházi Jegyértékesítő Rendszer (ThaliaTickets)", 50, 45);

        // System Boundary Box
        int sysX = 220, sysY = 70, sysW = 740, sysH = 690;
        g.setColor(Color.WHITE);
        g.fillRoundRect(sysX, sysY, sysW, sysH, 16, 16);
        g.setColor(new Color(148, 163, 184));
        g.setStroke(new BasicStroke(2.0f));
        g.drawRoundRect(sysX, sysY, sysW, sysH, 16, 16);

        g.setFont(new Font("Segoe UI", Font.BOLD, 14));
        g.setColor(new Color(71, 85, 105));
        g.drawString("Rendszer Határ (System Boundary): ThaliaTickets Webalkalmazás", sysX + 20, sysY + 30);

        // Actors
        drawActor(g, 90, 250, "Színházlátogató\n/ Vásárló", new Color(30, 64, 175));
        drawActor(g, 1070, 210, "Színházi\nAdminisztrátor", new Color(180, 83, 9));
        drawActor(g, 1070, 500, "Fizetési Rendszer\n(OTP / Stripe)", new Color(22, 101, 52));

        // Use cases
        // Customer UCs
        drawUseCase(g, 260, 120, 280, 55, "UC1: Előadások böngészése és keresése", new Color(219, 234, 254), new Color(37, 99, 235));
        drawUseCase(g, 260, 200, 280, 55, "UC2: Előadás adatlap megtekintése", new Color(219, 234, 254), new Color(37, 99, 235));
        drawUseCase(g, 260, 280, 280, 55, "UC3: Nézőtéri székek kiválasztása", new Color(219, 234, 254), new Color(37, 99, 235));
        drawUseCase(g, 260, 370, 280, 55, "UC4: Jegyvásárlás és fizetés", new Color(219, 234, 254), new Color(37, 99, 235));
        drawUseCase(g, 260, 470, 280, 55, "UC5: Jegy lemondása / visszaváltása", new Color(219, 234, 254), new Color(37, 99, 235));

        // Included / connected UCs
        drawUseCase(g, 640, 340, 280, 55, "UC6: E-jegy kiállítás és letöltés", new Color(224, 231, 255), new Color(79, 70, 229));
        drawUseCase(g, 640, 440, 280, 55, "UC7: Tranzakció feldolgozása", new Color(220, 252, 231), new Color(22, 163, 74));

        // Admin UCs
        drawUseCase(g, 630, 130, 290, 55, "UC8: Előadások kezelése (CRUD)", new Color(254, 243, 199), new Color(217, 119, 6));
        drawUseCase(g, 630, 220, 290, 55, "UC9: Bevételi & telítettségi statisztika", new Color(254, 243, 199), new Color(217, 119, 6));

        // Association lines
        g.setStroke(new BasicStroke(1.8f));
        g.setColor(new Color(30, 64, 175));
        drawLine(g, 130, 270, 260, 147);
        drawLine(g, 130, 270, 260, 227);
        drawLine(g, 130, 270, 260, 307);
        drawLine(g, 130, 270, 260, 397);
        drawLine(g, 130, 270, 260, 497);

        g.setColor(new Color(180, 83, 9));
        drawLine(g, 1030, 240, 920, 157);
        drawLine(g, 1030, 240, 920, 247);

        g.setColor(new Color(22, 101, 52));
        drawLine(g, 1030, 520, 920, 467);

        // Include lines (dashed)
        float[] dash = {6.0f, 4.0f};
        g.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
        g.setColor(new Color(100, 116, 139));
        drawDashedLineWithLabel(g, 540, 385, 640, 370, "<<include>>");
        drawDashedLineWithLabel(g, 540, 410, 640, 455, "<<include>>");

        g.dispose();
        ImageIO.write(img, "png", file);
    }

    private static void drawActor(Graphics2D g, int cx, int cy, String label, Color color) {
        g.setColor(color);
        g.setStroke(new BasicStroke(2.2f));
        // Head
        g.drawOval(cx - 15, cy - 45, 30, 30);
        // Body
        g.drawLine(cx, cy - 15, cx, cy + 25);
        // Arms
        g.drawLine(cx - 25, cy, cx + 25, cy);
        // Legs
        g.drawLine(cx, cy + 25, cx - 20, cy + 65);
        g.drawLine(cx, cy + 25, cx + 20, cy + 65);

        // Label
        g.setFont(new Font("Segoe UI", Font.BOLD, 13));
        FontMetrics fm = g.getFontMetrics();
        String[] lines = label.split("\n");
        int ly = cy + 85;
        for (String line : lines) {
            int lw = fm.stringWidth(line);
            g.drawString(line, cx - lw / 2, ly);
            ly += 16;
        }
    }

    private static void drawUseCase(Graphics2D g, int x, int y, int w, int h, String text, Color bg, Color border) {
        g.setStroke(new BasicStroke(1.8f));
        g.setColor(bg);
        g.fillOval(x, y, w, h);
        g.setColor(border);
        g.drawOval(x, y, w, h);

        g.setColor(new Color(15, 23, 42));
        g.setFont(new Font("Segoe UI", Font.BOLD, 12));
        FontMetrics fm = g.getFontMetrics();
        int tw = fm.stringWidth(text);
        g.drawString(text, x + (w - tw) / 2, y + (h + fm.getAscent() - fm.getDescent()) / 2);
    }

    private static void drawLine(Graphics2D g, int x1, int y1, int x2, int y2) {
        g.drawLine(x1, y1, x2, y2);
    }

    private static void drawDashedLineWithLabel(Graphics2D g, int x1, int y1, int x2, int y2, String label) {
        g.drawLine(x1, y1, x2, y2);
        int mx = (x1 + x2) / 2;
        int my = (y1 + y2) / 2;
        g.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        g.setColor(new Color(71, 85, 105));
        g.drawString(label, mx - 25, my - 6);
    }

    public static void generateClassDiagram(File file) throws Exception {
        int w = 1200, h = 820;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        setupGraphics(g);

        // Background
        g.setColor(new Color(248, 250, 252));
        g.fillRect(0, 0, w, h);

        // Title
        g.setFont(new Font("Segoe UI", Font.BOLD, 22));
        g.setColor(new Color(15, 23, 42));
        g.drawString("Class Diagram - Színházi Jegyértékesítő Rendszer (ThaliaTickets)", 50, 45);

        // Draw Classes
        // TheaterHall
        drawUmlClass(g, 50, 90, 210, 190, "TheaterHall",
                new String[]{"- id: Long", "- name: String", "- totalSeats: int", "- rowsCount: int", "- colsCount: int"},
                new String[]{"+ getId(): Long", "+ getName(): String", "+ generateSeats(): List"},
                new Color(241, 245, 249), new Color(71, 85, 105));

        // Performance
        drawUmlClass(g, 330, 80, 240, 260, "Performance",
                new String[]{"- id: Long", "- title: String", "- author: String", "- description: String", "- genre: String", "- dateTime: LocalDateTime", "- basePrice: BigDecimal", "- hall: TheaterHall"},
                new String[]{"+ getId(): Long", "+ getTitle(): String", "+ getFormattedDate(): String", "+ getAvailableSeats(): int"},
                new Color(239, 246, 255), new Color(37, 99, 235));

        // PerformanceSeat
        drawUmlClass(g, 640, 80, 260, 260, "PerformanceSeat",
                new String[]{"- id: Long", "- performance: Performance", "- rowNumber: int", "- seatNumber: int", "- sector: Sector [Enum]", "- priceMultiplier: double", "- status: SeatStatus [Enum]", "- version: Long"},
                new String[]{"+ calculatePrice(): BigDecimal", "+ isAvailable(): boolean", "+ markSold(): void", "+ markFree(): void"},
                new Color(239, 246, 255), new Color(37, 99, 235));

        // Booking
        drawUmlClass(g, 330, 420, 240, 260, "Booking",
                new String[]{"- id: Long", "- bookingCode: String", "- customerName: String", "- customerEmail: String", "- customerPhone: String", "- bookingTime: LocalDateTime", "- totalAmount: BigDecimal", "- status: BookingStatus"},
                new String[]{"+ getBookingCode(): String", "+ getTickets(): List<Ticket>", "+ cancel(): void", "+ isCancellable(): boolean"},
                new Color(254, 242, 242), new Color(220, 38, 38));

        // Ticket
        drawUmlClass(g, 640, 420, 260, 240, "Ticket",
                new String[]{"- id: Long", "- ticketCode: String", "- performanceSeat: PerformanceSeat", "- booking: Booking", "- price: BigDecimal", "- issueTime: LocalDateTime"},
                new String[]{"+ getTicketCode(): String", "+ getSeatLabel(): String", "+ getQrData(): String"},
                new Color(254, 242, 242), new Color(220, 38, 38));

        // Service box: PerformanceService
        drawUmlClass(g, 940, 90, 230, 200, "<<Service>>\nPerformanceService",
                new String[]{"- performanceRepo", "- hallRepo"},
                new String[]{"+ findAll(): List", "+ findById(id): Performance", "+ search(query): List", "+ create(dto): Performance"},
                new Color(240, 253, 244), new Color(22, 163, 74));

        // Service box: BookingService
        drawUmlClass(g, 940, 420, 230, 220, "<<Service>>\nBookingService",
                new String[]{"- bookingRepo", "- seatRepo", "- ticketRepo"},
                new String[]{"+ createBooking(req): Booking", "+ cancelBooking(code): void", "+ getBooking(code): Booking", "+ getStats(): StatsDto"},
                new Color(240, 253, 244), new Color(22, 163, 74));

        // Association connectors
        g.setStroke(new BasicStroke(2.0f));
        g.setColor(new Color(71, 85, 105));

        // Hall -> Performance (1 to 0..*)
        g.drawLine(260, 180, 330, 180);
        g.drawString("1", 268, 172);
        g.drawString("0..*", 310, 172);

        // Performance -> PerformanceSeat (1 to 1..*)
        g.setColor(new Color(37, 99, 235));
        g.drawLine(570, 180, 640, 180);
        g.drawString("1", 578, 172);
        g.drawString("1..*", 615, 172);

        // Booking -> Ticket (1 to 1..*)
        g.setColor(new Color(220, 38, 38));
        g.drawLine(570, 530, 640, 530);
        g.drawString("1", 578, 522);
        g.drawString("1..*", 615, 522);

        // PerformanceSeat -> Ticket (1 to 0..1)
        g.setColor(new Color(100, 116, 139));
        g.drawLine(770, 340, 770, 420);
        g.drawString("1", 755, 360);
        g.drawString("0..1", 750, 405);

        // Performance -> Booking link
        g.drawLine(440, 340, 440, 420);
        g.drawString("1", 450, 360);
        g.drawString("0..*", 450, 405);

        g.dispose();
        ImageIO.write(img, "png", file);
    }

    private static void drawUmlClass(Graphics2D g, int x, int y, int w, int h, String name, String[] attrs, String[] methods, Color bg, Color border) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, w, h);

        int headerH = name.contains("\n") ? 42 : 30;

        // Header
        g.setColor(bg);
        g.fillRect(x, y, w, headerH);
        g.setColor(border);
        g.setStroke(new BasicStroke(1.8f));
        g.drawRect(x, y, w, h);
        g.drawLine(x, y + headerH, x + w, y + headerH);

        g.setFont(new Font("Segoe UI", Font.BOLD, 13));
        FontMetrics fm = g.getFontMetrics();
        String[] titles = name.split("\n");
        int ty = y + (headerH / (titles.length + 1)) + 6;
        for (String t : titles) {
            int tw = fm.stringWidth(t);
            g.drawString(t, x + (w - tw) / 2, ty);
            ty += 16;
        }

        // Attributes
        g.setFont(new Font("Consolas", Font.PLAIN, 11));
        g.setColor(new Color(30, 41, 59));
        int curY = y + headerH + 18;
        for (String a : attrs) {
            g.drawString(a, x + 8, curY);
            curY += 16;
        }

        // Divider
        g.setColor(border);
        g.drawLine(x, curY - 4, x + w, curY - 4);
        curY += 12;

        // Methods
        g.setColor(new Color(15, 23, 42));
        for (String m : methods) {
            g.drawString(m, x + 8, curY);
            curY += 16;
        }
    }

    public static void generateSequenceDiagram(File file) throws Exception {
        int w = 1200, h = 820;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        setupGraphics(g);

        // Background
        g.setColor(new Color(248, 250, 252));
        g.fillRect(0, 0, w, h);

        // Title
        g.setFont(new Font("Segoe UI", Font.BOLD, 22));
        g.setColor(new Color(15, 23, 42));
        g.drawString("Sequence Diagram - Jegyfoglalási folyamat Pessimistic Lock zárolással", 50, 45);

        // Lifelines definition
        int[] xs = {120, 320, 560, 810, 1040};
        String[] names = {
                "Jegyvásárló\n(Browser)",
                "BookingController\n(Spring MVC)",
                "BookingService\n(@Transactional)",
                "SeatRepository\n(PESSIMISTIC_WRITE)",
                "Adatbázis\n(H2 Relational DB)"
        };
        Color[] colors = {
                new Color(219, 234, 254),
                new Color(254, 243, 199),
                new Color(220, 252, 231),
                new Color(237, 233, 254),
                new Color(241, 245, 249)
        };
        Color[] borders = {
                new Color(29, 78, 216),
                new Color(217, 119, 6),
                new Color(22, 163, 74),
                new Color(124, 58, 237),
                new Color(71, 85, 105)
        };

        int topY = 75, bottomY = 760;

        // Draw lifeline heads and dashed lines
        for (int i = 0; i < xs.length; i++) {
            int cx = xs[i];
            // Header box
            g.setColor(colors[i]);
            g.fillRoundRect(cx - 75, topY, 150, 50, 10, 10);
            g.setColor(borders[i]);
            g.setStroke(new BasicStroke(1.8f));
            g.drawRoundRect(cx - 75, topY, 150, 50, 10, 10);

            g.setFont(new Font("Segoe UI", Font.BOLD, 12));
            FontMetrics fm = g.getFontMetrics();
            String[] lines = names[i].split("\n");
            g.drawString(lines[0], cx - fm.stringWidth(lines[0]) / 2, topY + 20);
            g.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            fm = g.getFontMetrics();
            g.drawString(lines[1], cx - fm.stringWidth(lines[1]) / 2, topY + 38);

            // Dashed lifeline
            float[] dash = {6.0f, 6.0f};
            g.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
            g.setColor(new Color(148, 163, 184));
            g.drawLine(cx, topY + 50, cx, bottomY);
        }

        // Draw Message Arrows
        drawSeqArrow(g, xs[0], xs[1], 160, "1: POST /bookings/checkout (seatIds, customerInfo)", true, new Color(29, 78, 216));
        drawSeqArrow(g, xs[1], xs[2], 210, "2: createBooking(bookingRequest)", true, new Color(217, 119, 6));

        // Transaction start marker
        g.setColor(new Color(22, 163, 74, 30));
        g.fillRect(xs[2] - 25, 240, xs[4] - xs[2] + 50, 360);
        g.setColor(new Color(22, 163, 74));
        g.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 11));
        g.drawString("Tranzakció és PESSIMISTIC_WRITE blokk", xs[2] - 15, 255);

        drawSeqArrow(g, xs[2], xs[3], 280, "3: findByIdInWithLock(seatIds)", true, new Color(22, 163, 74));
        drawSeqArrow(g, xs[3], xs[4], 320, "4: SELECT * FROM seat WHERE id IN (...) FOR UPDATE", true, new Color(124, 58, 237));
        drawSeqArrow(g, xs[4], xs[3], 360, "5: Adatbázis zárolt rekordokat ad vissza", false, new Color(71, 85, 105));
        drawSeqArrow(g, xs[3], xs[2], 400, "6: List<PerformanceSeat> entitások", false, new Color(124, 58, 237));

        // Self-call validation
        drawSelfCall(g, xs[2], 430, 40, "7: Ellenőrzés: székek státusza == FREE");

        drawSeqArrow(g, xs[2], xs[3], 490, "8: Mentés: Booking + Ticket-ek, Seat.status = SOLD", true, new Color(22, 163, 74));
        drawSeqArrow(g, xs[3], xs[4], 530, "9: INSERT INTO bookings, tickets; UPDATE seats; COMMIT", true, new Color(124, 58, 237));
        drawSeqArrow(g, xs[4], xs[3], 570, "10: Tranzakció sikeresen kommittálva", false, new Color(71, 85, 105));
        drawSeqArrow(g, xs[3], xs[2], 605, "11: Booking visszaadása jegyekkel", false, new Color(124, 58, 237));
        drawSeqArrow(g, xs[2], xs[1], 645, "12: return BookingResponse(bookingCode)", false, new Color(22, 163, 74));
        drawSeqArrow(g, xs[1], xs[0], 690, "13: Redirect /bookings/confirm?code=... (E-jegy nézet és nyomtatás)", false, new Color(29, 78, 216));

        g.dispose();
        ImageIO.write(img, "png", file);
    }

    private static void drawSeqArrow(Graphics2D g, int x1, int x2, int y, String label, boolean sync, Color color) {
        g.setColor(color);
        if (sync) {
            g.setStroke(new BasicStroke(1.8f));
        } else {
            float[] dash = {6.0f, 4.0f};
            g.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
        }
        g.drawLine(x1, y, x2, y);

        // Arrow head
        int dir = (x2 > x1) ? 1 : -1;
        g.setStroke(new BasicStroke(1.8f));
        if (sync) {
            Polygon arrow = new Polygon();
            arrow.addPoint(x2, y);
            arrow.addPoint(x2 - dir * 10, y - 5);
            arrow.addPoint(x2 - dir * 10, y + 5);
            g.fill(arrow);
        } else {
            g.drawLine(x2, y, x2 - dir * 10, y - 5);
            g.drawLine(x2, y, x2 - dir * 10, y + 5);
        }

        // Label
        g.setFont(new Font("Segoe UI", Font.BOLD, 11));
        FontMetrics fm = g.getFontMetrics();
        int midX = (x1 + x2) / 2;
        int tw = fm.stringWidth(label);
        g.drawString(label, midX - tw / 2, y - 6);
    }

    private static void drawSelfCall(Graphics2D g, int x, int y, int h, String label) {
        g.setStroke(new BasicStroke(1.8f));
        g.setColor(new Color(22, 163, 74));
        g.drawLine(x, y, x + 35, y);
        g.drawLine(x + 35, y, x + 35, y + h);
        g.drawLine(x + 35, y + h, x, y + h);

        Polygon arrow = new Polygon();
        arrow.addPoint(x, y + h);
        arrow.addPoint(x + 8, y + h - 4);
        arrow.addPoint(x + 8, y + h + 4);
        g.fill(arrow);

        g.setFont(new Font("Segoe UI", Font.BOLD, 11));
        g.drawString(label, x + 42, y + h / 2 + 4);
    }
}
