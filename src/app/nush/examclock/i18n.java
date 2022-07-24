package app.nush.examclock;

import java.util.Locale;
import java.util.ResourceBundle;

public class i18n {
    public static ResourceBundle B;
    public static String menu_add_exam = "menu_add_exam";
    public static String menu_delete_exam = "menu_delete_exam";
    public static String menu_edit_exam = "menu_edit_exam";
    public static String menu_sort_exam = "menu_sort_exam";

    public static void load() {
        Locale locale = Locale.getDefault();
        B = ResourceBundle.getBundle("locales.controls", locale);
    }
}
