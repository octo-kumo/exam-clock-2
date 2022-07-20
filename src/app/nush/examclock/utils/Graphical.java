package app.nush.examclock.utils;

import java.awt.*;

public class Graphical {
    public static void drawCenteredString(Graphics2D g, String text, float x, float y) {
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        x -= metrics.stringWidth(text) / 2f;
        y -= metrics.getHeight() / 2f - metrics.getAscent();
        g.drawString(text, x, y);
    }
}
