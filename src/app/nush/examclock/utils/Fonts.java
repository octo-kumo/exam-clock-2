package app.nush.examclock.utils;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class Fonts {
    public static Font montserrat;

    public static void loadFonts() {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(montserrat = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(Fonts.class.getResourceAsStream("/fonts/montserrat.ttf"))));
        } catch (IOException | FontFormatException e) {
            //Handle exception
        }
    }
}
