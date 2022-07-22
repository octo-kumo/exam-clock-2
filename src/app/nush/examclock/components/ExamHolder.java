package app.nush.examclock.components;

import app.nush.examclock.model.Exam;
import app.nush.examclock.utils.Fonts;
import app.nush.examclock.windows.ExamEditor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ExamHolder extends JPanel {
    public static final DateTimeFormatter PATTERN = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final JLabel name;
    private final JLabel desc;
    private final JLabel start;
    private final JLabel end;
    private Exam exam;

    public ExamHolder(Exam exam) {
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setComponentPopupMenu(new JPopupMenu() {{
            add(new JMenuItem("Edit") {{
                addActionListener(ExamHolder.this::edit);
            }});
            add(new JMenuItem("Delete") {{
                addActionListener(ExamHolder.this::delete);
            }});
            add(new JSeparator());
            add(new JMenuItem("Add") {{
                addActionListener(e -> getList().add(e));
            }});
            add(new JMenuItem("Sort") {{
                addActionListener(e -> getList().sort(e));
            }});
        }});
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1) ExamHolder.this.edit(new ActionEvent(e.getSource(), e.getID(), "edit"));
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(UIManager.getColor("control").darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(UIManager.getColor("control"));
            }
        });
        GridBagLayout gridBagLayout = new GridBagLayout();
        this.setLayout(gridBagLayout);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        this.add(name = new JLabel(), c);
        name.setFont(Fonts.montserrat.deriveFont(Font.BOLD, 50f));

        c.gridy = 1;
        this.add(desc = new JLabel(), c);
        desc.setFont(Fonts.opensans.deriveFont(Font.PLAIN, 18f));

        c.gridy = 2;
        c.gridwidth = 1;
        this.add(start = new JLabel("00:00:00", SwingConstants.CENTER), c);
        start.setFont(Fonts.spacemono.deriveFont(Font.PLAIN, 40f));
        c.gridx = 1;
        this.add(end = new JLabel("00:00:00", SwingConstants.CENTER), c);
        end.setFont(Fonts.spacemono.deriveFont(Font.PLAIN, 40f));
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
        g.drawLine(0, 0, getWidth(), 0);
        long elaps = exam.startTime.until(LocalDateTime.now(), ChronoUnit.SECONDS);
        long total = exam.startTime.until(exam.endTime, ChronoUnit.SECONDS);
        if (elaps >= 0 && total != 0) {
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
        setPreferredSize(null);
        setPreferredSize(new Dimension(500, getPreferredSize().height));
        setMaximumSize(getPreferredSize());
        return exam;
    }

    public Exam getExam() {
        return exam;
    }
}
