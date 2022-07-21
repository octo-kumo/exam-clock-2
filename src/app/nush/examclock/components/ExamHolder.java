package app.nush.examclock.components;

import app.nush.examclock.model.Exam;
import app.nush.examclock.utils.Fonts;
import app.nush.examclock.windows.ExamEditor;
import com.formdev.flatlaf.FlatLaf;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.font.TextAttribute;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class ExamHolder extends JPanel {
    public static final DateTimeFormatter PATTERN = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final JLabel name;
    private final JLabel desc;
    private final JLabel start;
    private final JLabel end;
    private Exam exam;

    public ExamHolder(Exam exam) {
        setBorder(new CompoundBorder(BorderFactory.createLineBorder(FlatLaf.isLafDark() ? Color.WHITE : Color.BLACK), new EmptyBorder(10, 10, 10, 10)));
        setComponentPopupMenu(new JPopupMenu("WWW") {{
            add(new JMenuItem("Edit") {{
                addActionListener(ExamHolder.this::edit);
            }});
            add(new JMenuItem("Delete") {{
                addActionListener(ExamHolder.this::delete);
            }});
        }});
        GridBagLayout gridBagLayout = new GridBagLayout();
        this.setLayout(gridBagLayout);

        gridBagLayout.columnWeights = new double[]{1, 0};
        gridBagLayout.rowWeights = new double[]{1, 1, 1};
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;

        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 2;
        this.add(name = new JLabel(), c);
        name.setFont(Fonts.montserrat.deriveFont(Font.BOLD, 50f));

        c.gridy = 2;
        c.gridwidth = 2;
        c.gridheight = 1;
        this.add(desc = new JLabel(), c);
        desc.setFont(Fonts.opensans.deriveFont(Font.PLAIN, 18f));

        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 1;
        this.add(start = new JLabel("00:00:00"), c);
        start.setFont(Fonts.spacemono.deriveFont(Font.PLAIN, 40f));
        c.gridy = 1;
        this.add(end = new JLabel("00:00:00"), c);
        end.setFont(Fonts.spacemono.deriveFont(Font.PLAIN, 40f));
        setPreferredSize(new Dimension(500, getPreferredSize().height));
        setExam(exam);
    }

    private void delete(ActionEvent e) {
        getList().remove(exam);
    }

    private ExamList getList() {
        return ((ExamList) getParent().getParent().getParent());
    }

    private void edit(ActionEvent e) {
        new ExamEditor(exam, ExamEditor.EditDialogType.EDIT, SwingUtilities.getWindowAncestor(this))
                .setVisible(true);
        setExam(exam);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        long elaps = exam.startTime.until(LocalDateTime.now(), ChronoUnit.SECONDS);
        long total = exam.startTime.until(exam.endTime, ChronoUnit.SECONDS);
        if (elaps >= 0) {
            g.setColor(elaps >= total ? Color.GREEN.darker() : Color.WHITE.darker());
            g.fillRect(0, 0, (int) (getWidth() * elaps / total), 10);
        }
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
