package app.nush.examclock.components;

import app.nush.examclock.Context;
import app.nush.examclock.ExamClock;
import app.nush.examclock.components.shapes.Icons;
import app.nush.examclock.model.Exam;
import app.nush.examclock.utils.Fonts;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

import static app.nush.examclock.utils.Fonts.montserrat;
import static app.nush.examclock.utils.Fonts.opensans;
import static app.nush.examclock.utils.Graphical.drawCenteredString;
import static java.awt.Font.PLAIN;

public class ClockFace extends JComponent {
    private static final RenderingHints HINTS_QUALITY = new RenderingHints(new HashMap<RenderingHints.Key, Object>() {{
        put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);

        put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
    }});

    private static final RenderingHints HINTS_FAST = new RenderingHints(new HashMap<RenderingHints.Key, Object>() {{
        put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);

        put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
    }});
    private static final Ellipse2D[] RINGS = {new Ellipse2D.Double(-180, -180, 180 * 2, 180 * 2), new Ellipse2D.Double(-190, -190, 190 * 2, 190 * 2)};
    private static final Line2D[] LINES = new Line2D[60];

    static {
        for (int i = 0, ir = 160, nr = 170, rr = 175, or = 180; i < 60; i++) {
            int r = i % 15 == 0 ? ir : i % 5 == 0 ? nr : rr;
            double a = mapToRadian(i / 60d);
            LINES[i] = new Line2D.Double(r * Math.sin(a), r * Math.cos(a), or * Math.sin(a), or * Math.cos(a));
        }
    }

    private static final int[][] HOUR_HAND = {{-6, 0, 6, 4, 0, -4}, {12, 15, 12, -100, -105, -100}};
    private static final int[][] MINUTE_HAND = {{-3, 0, 3, 2, 0, -2}, {20, 23, 20, -150, -155, -150}};
    private static final int[][] SECOND_HAND = {{-2, 2, 1, 0, -1}, {30, 30, -170, -175, -170}};
    private static final int[][][] HANDS = {SECOND_HAND, MINUTE_HAND, HOUR_HAND};
    private static final Color[] HAND_COLORS = {Color.RED, Color.BLACK, Color.BLACK};
    private final double[] handAngles = {0, 0, 0};
    public LocalDateTime now = LocalDateTime.now();
    private ExamClock clock;

    public ClockFace(ExamClock clock) {
        this.clock = clock;
        setFont(montserrat);
    }

    @Override
    public void paintComponent(Graphics g1d) {
        super.paintComponent(g1d);
        Graphics2D g = (Graphics2D) g1d;
        setForeground(Context.dark.get() ? Color.WHITE : Color.BLACK);
        g.setBackground(Context.dark.get() ? Color.BLACK : Color.WHITE);
        g.clearRect(0, 0, getWidth(), getHeight());
        g.setRenderingHints(Context.quality.get() ? HINTS_QUALITY : HINTS_FAST);
        now = LocalDateTime.now();
        calculateHands();
        scaleToSize(g);
        drawFace(g);
        if (Context.face_arcs.get()) drawExams(g);
        drawToilet(g);
        drawHands(g);
        if (Context.debug.get()) drawDebug(g);
    }

    private void drawToilet(Graphics2D g) {
        g.setColor(Context.womanToilet.get() ? Color.RED : getForeground());
        g.fill(Icons.WOMAN);
        g.setColor(Context.manToilet.get() ? Color.RED : getForeground());
        g.fill(Icons.MAN);
    }

    private void scaleToSize(Graphics2D g) {
        g.translate(getWidth() / 2, getHeight() / 2);
        double scale = Math.min(getWidth(), getHeight()) / 400d;
        g.scale(scale, scale);
    }

    private void drawHands(Graphics2D g) {
        AffineTransform transform = g.getTransform();
        for (int i = 2; i >= 0; i--) {
            g.rotate(mapToRadian(handAngles[i]));
            g.setColor(HAND_COLORS[i]);
            g.fillPolygon(HANDS[i][0], HANDS[i][1], HANDS[i][0].length);
            g.setColor(Color.WHITE);
            g.drawPolygon(HANDS[i][0], HANDS[i][1], HANDS[i][0].length);
            g.setTransform(transform);
        }

        g.setColor(HAND_COLORS[2].darker());
        g.fillOval(-4, -4, 8, 8);
    }

    private void calculateHands() {
        handAngles[0] = (now.getSecond() + interpolate(now.getNano() / 1e9d)) / 60;
        handAngles[1] = (now.getMinute() + handAngles[0]) / 60d;
        handAngles[2] = (now.getHour() + handAngles[1]) / 12d;
    }

    private void drawFace(Graphics2D g) {
        g.setColor(getForeground());
        for (Ellipse2D ring : RINGS) g.draw(ring);
        for (Line2D line : LINES) g.draw(line);
        for (int i = 0, r = 140; i < 12; i++) {
            g.setFont(g.getFont().deriveFont(i % 3 == 0 ? Font.BOLD : PLAIN, i % 3 == 0 ? 32f : 24f));
            drawCenteredString(g, String.valueOf(i == 0 ? 12 : i), (float) (r * Math.sin(mapToRadian(i / 12d))), -(float) (r * Math.cos(mapToRadian(i / 12d))));
        }
        g.setFont(opensans.deriveFont(Font.BOLD, 48f));
        drawCenteredString(g, now.format(DateTimeFormatter.ofPattern("hh:mm:ss")), 0, 70);
        g.setFont(montserrat);
    }

    private void drawExams(Graphics2D g) {
        if (clock == null) return;
        g.setColor(Color.WHITE);
        double hr = 105, mr = 155;
        for (Exam exam : clock.getList()) drawArcTo(g, exam.endTime, hr += 5, mr += 5);
    }

    private static double mapToRadian(double alpha) {
        return Math.PI * 2 * alpha;
    }

    private static double interpolate(double a) {
        double sq = a * a;
        return sq / (2.0 * (sq - a) + 1.0);
    }

    private long last_frame_nanos = 0;

    private void drawDebug(Graphics2D g) {
        g.setFont(Fonts.spacemono.deriveFont(Font.PLAIN, 5));
        g.setColor(getForeground());
        int x = -200, y = 200;
        Runtime runtime = Runtime.getRuntime();
        g.drawString(String.format("OS: %s (%s)", System.getProperty("os.name"), System.getProperty("os.version")), x, y -= 5);
        g.drawString(String.format("ARCH: %s (%d cores)", System.getProperty("os.arch"), runtime.availableProcessors()), x, y -= 5);
        g.drawString(String.format("MEM: %s/%s", formatBytes(runtime.totalMemory() - runtime.freeMemory()), formatBytes(runtime.totalMemory())), x, y -= 5);
        g.drawString(String.format("FPS: %5.1f (%.0f)", 1e9d / -(this.last_frame_nanos - (this.last_frame_nanos = System.nanoTime())), ExamClock.loop.getFPS()), x, y -= 5);
    }

    public static String formatBytes(long bytes) {
        if (-1000 < bytes && bytes < 1000) return bytes + " B";
        CharacterIterator ci = new StringCharacterIterator("kMGTPE");
        while (bytes <= -999_950 || bytes >= 999_950) {
            bytes /= 1000;
            ci.next();
        }
        return String.format("%.1f %cB", bytes / 1000.0, ci.current());
    }

    public void drawArcTo(Graphics2D g, LocalDateTime time, double hr, double mr) {
        if (now.isAfter(time)) return;
        long correction = (long) (1e9d * interpolate(now.getNano() / 1e9d) - now.getNano()); // fancy second hand
        long diff = now.until(time, ChronoUnit.NANOS) - correction;
        if (diff > 1e9 * 60 * 60 * 12) return; // erm well, can't be right?
        drawArc(g, handAngles[1], diff / 1e9d / 60 / 60, mr, Color.WHITE, 5);
        if (diff > 1e9 * 60 * 60) return;
        drawArc(g, handAngles[2], diff / 1e9d / 60 / 60 / 12, hr, Color.GRAY, 5);
        if (diff > 1e9 * 60) return;
        drawArc(g, handAngles[0], diff / 1e9d / 60, 182, Color.RED, 5);
    }

    private void drawArc(Graphics2D g, double start, double extent, double r, Color color, float thickness) {
        Color c = g.getColor();
        g.setColor(color);
        Path2D.Double path = new Path2D.Double();
        double r2 = r + thickness;
        path.append(new Arc2D.Double(-r2, -r2, r2 * 2, r2 * 2, toArcAngle(start), -extent * 360, Arc2D.OPEN), true);
        path.append(new Arc2D.Double(-r, -r, r * 2, r * 2, toArcAngle(start + extent), extent * 360, Arc2D.OPEN), true);
        path.closePath();
        g.fill(path);
        g.setColor(c);
    }

    private double toArcAngle(double alpha) {
        return (90 - 360 * alpha);
    }
}
