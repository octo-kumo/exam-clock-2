package app.nush.examclock.components;

import app.nush.examclock.Config;
import app.nush.examclock.ExamClock;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import static app.nush.examclock.utils.Fonts.montserrat;
import static app.nush.examclock.utils.Fonts.opensans;
import static app.nush.examclock.utils.Graphical.drawCenteredString;
import static java.awt.Font.PLAIN;

public class ClockFace extends JComponent {
    private static final RenderingHints HINTS = new RenderingHints(new HashMap<RenderingHints.Key, Object>() {{
        put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
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

    private static final Color[] HAND_COLORS = {Color.BLACK, Color.BLACK, Color.RED};
    public LocalDateTime now = LocalDateTime.now();
    private final Config config;

    public ClockFace(Config config) {
        this.config = config;
        setFont(montserrat);
    }

    @Override
    public void paintComponent(Graphics g1d) {
        super.paintComponent(g1d);
        Graphics2D g = (Graphics2D) g1d;
        g.setBackground(config.isDark() ? Color.BLACK : Color.WHITE);
        g.clearRect(0, 0, getWidth(), getHeight());
        g.setRenderingHints(HINTS);
        now = LocalDateTime.now();
        scaleToSize(g);
        drawFace(g);
        drawHands(g);
    }

    private void scaleToSize(Graphics2D g) {
        g.translate(getWidth() / 2, getHeight() / 2);
        double scale = Math.min(getWidth(), getHeight()) / 400d;
        g.scale(scale, scale);
    }

    private void drawHands(Graphics2D g) {
        double a1 = (now.getSecond() + interpolate(now.getNano() / 1e9d)) / 60;
        double a2 = (now.getMinute() + a1) / 60d;
        double a3 = (now.getHour() + a2) / 12d;
        AffineTransform transform = g.getTransform();
        g.rotate(mapToRadian(a3));
        g.setColor(HAND_COLORS[0]);
        g.fillPolygon(HOUR_HAND[0], HOUR_HAND[1], HOUR_HAND[0].length);
        g.setColor(Color.WHITE);
        g.drawPolygon(HOUR_HAND[0], HOUR_HAND[1], HOUR_HAND[0].length);
        g.setTransform(transform);

        g.rotate(mapToRadian(a2));
        g.setColor(HAND_COLORS[1]);
        g.fillPolygon(MINUTE_HAND[0], MINUTE_HAND[1], MINUTE_HAND[0].length);
        g.setColor(Color.WHITE);
        g.drawPolygon(MINUTE_HAND[0], MINUTE_HAND[1], MINUTE_HAND[0].length);
        g.setTransform(transform);

        g.rotate(mapToRadian(a1));
        g.setColor(HAND_COLORS[2]);
        g.fillPolygon(SECOND_HAND[0], SECOND_HAND[1], SECOND_HAND[0].length);
        g.setColor(Color.WHITE);
        g.drawPolygon(SECOND_HAND[0], SECOND_HAND[1], SECOND_HAND[0].length);
        g.setTransform(transform);

        g.setColor(HAND_COLORS[2].darker());
        g.fillOval(-4, -4, 8, 8);
    }

    private void drawFace(Graphics2D g) {
        g.setColor(config.isDark() ? Color.WHITE : Color.BLACK);
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

    private static double mapToRadian(double alpha) {
        return Math.PI * 2 * alpha;
    }

    private static double interpolate(double a) {
        double sq = a * a;
        return sq / (2.0 * (sq - a) + 1.0);
    }
}
