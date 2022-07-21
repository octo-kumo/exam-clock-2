package app.nush.examclock.components;

import app.nush.examclock.model.Exam;
import app.nush.examclock.utils.Fonts;
import com.formdev.flatlaf.FlatLaf;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;

public class ExamHolder extends JPanel {
    public static final DateTimeFormatter PATTERN = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final JLabel name;
    private final JLabel desc;
    private final JLabel start;
    private final JLabel end;
    private Exam exam;

    public ExamHolder(Exam exam) {
        setBorder(new CompoundBorder(BorderFactory.createLineBorder(FlatLaf.isLafDark() ? Color.WHITE : Color.BLACK),
                new EmptyBorder(10, 10, 10, 10)));
        GridBagLayout gridBagLayout = new GridBagLayout();
        this.setLayout(gridBagLayout);

        gridBagLayout.columnWeights = new double[]{1, 0, 0};
        gridBagLayout.rowWeights = new double[]{1, 1, 1};
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;

        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 2;
        this.add(name = new JLabel(), c);
        name.setFont(Fonts.montserrat.deriveFont(Map.of(TextAttribute.SIZE, 50f, TextAttribute.WEIGHT, TextAttribute.WEIGHT_ULTRABOLD)));

        c.gridy = 2;
        c.gridwidth = 3;
        c.gridheight = 1;
        this.add(desc = new JLabel(), c);
        desc.setFont(Fonts.opensans.deriveFont(Font.PLAIN, 18f));

        c.gridx = 2;
        c.gridy = 0;
        c.gridwidth = 1;
        this.add(start = new JLabel("00:00:00"), c);
        start.setFont(Fonts.opensans.deriveFont(Font.PLAIN, 40f));
        c.gridy = 1;
        this.add(end = new JLabel("00:00:00"), c);
        end.setFont(Fonts.opensans.deriveFont(Font.PLAIN, 40f));
        setPreferredSize(new Dimension(500, getPreferredSize().height));
        setExam(exam);
    }

    public Exam setExam(Exam exam) {
        this.exam = exam;
        this.name.setText(exam.name);
        this.desc.setText(exam.desc);
        this.start.setText(PATTERN.format(exam.startTime));
        this.end.setText(PATTERN.format(exam.endTime));
        setMaximumSize(getPreferredSize());
        return exam;
    }

    public Exam getExam() {
        return exam;
    }
}
