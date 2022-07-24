package app.nush.examclock.utils.io;

import app.nush.examclock.model.Exam;
import app.nush.examclock.utils.io.serial.LocalDateTimeSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.time.LocalDateTime;
import java.util.Arrays;

public class FileIO {
    public static final FileNameExtensionFilter FILTER = new FileNameExtensionFilter("JSON Files", "json");
    public static final JFileChooser CHOOSER = new JFileChooser() {{
        setFileFilter(FILTER);
    }};
    private static final Gson gson = new GsonBuilder().setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
            .create();

    public static Exam[] load(File file) throws FileNotFoundException {
        if (!file.exists() || file.isDirectory()) throw new RuntimeException("Invalid File");
        return gson.fromJson(new BufferedReader(new FileReader(file)), Exam[].class);
    }

    public static void save(File file, Exam[] exams) throws IOException {
        if (file.exists() && JOptionPane.showConfirmDialog(null, "Overwrite?") != JOptionPane.OK_OPTION) return;
        System.out.println(Arrays.toString(exams));
        FileWriter writer = new FileWriter(file);
        gson.toJson(exams, writer);
        writer.flush();
    }

    public static Exam[] load() {
        if (CHOOSER.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) try {
            return load(CHOOSER.getSelectedFile());
        } catch (FileNotFoundException | JsonIOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public static void save(Exam[] exams) {
        if (CHOOSER.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) try {
            String filename = CHOOSER.getSelectedFile().toString();
            if (!filename.endsWith(".json")) filename += ".json";
            save(new File(filename), exams);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
