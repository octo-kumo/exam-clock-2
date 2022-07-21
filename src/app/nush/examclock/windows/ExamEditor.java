package app.nush.examclock.windows;

import app.nush.examclock.components.inputs.*;
import app.nush.examclock.model.Exam;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.function.Supplier;

public class ExamEditor extends JDialog {
    public enum EditDialogType {EDIT, CREATE}

    private JPanel panel;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField examName;
    private JTextField examDesc;
    private DateField startDate;
    private DateField endDate;
    private TimeField startTime;
    private TimeField endTime;

    private JTextField duration;
    private final Exam exam;

    public ExamEditor(Exam exam, EditDialogType type, Window parent) {
        super(parent);
        this.exam = exam;
        setModal(true);
        setTitle((type == EditDialogType.EDIT ? "Edit" : "Create new") + " exam");
        setLocationRelativeTo(parent);
        createComponents();
        setContentPane(panel);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        panel.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        pack();
    }

    private void createComponents() {
        panel = new JPanel(new BorderLayout());
        GridBagConstraints c = new GridBagConstraints();
        JPanel form = new JPanel(new GridBagLayout());

        examName = new PlaceholderTextField(exam.name);
        examDesc = new PlaceholderTextField(exam.desc);
        examName.setColumns(20);
        examDesc.setColumns(20);
        startDate = new DateField(exam.startTime.toLocalDate());
        endDate = new DateField(exam.endTime.toLocalDate());
        startTime = new TimeField(exam.startTime.toLocalTime());
        endTime = new TimeField(exam.endTime.toLocalTime());

        duration = new TimeDiffField(
                () -> LocalDateTime.of(startDate.getDate(), startTime.getTime()),
                () -> LocalDateTime.of(endDate.getDate(), endTime.getTime()),
                end -> {
                    endDate.setDate(end.toLocalDate());
                    endTime.setTime(end.toLocalTime());
                },
                new Changeable[]{startDate, startTime, endDate, endTime});

        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 2, 0, 2);
        form.add(new JLabel("Name") {{
            setLabelFor(examName);
        }}, c);
        c.gridy = 1;
        form.add(new JLabel("Desc") {{
            setLabelFor(examDesc);
        }}, c);
        c.gridy = 2;
        form.add(new JLabel("Start") {{
            setLabelFor(startTime);
        }}, c);
        c.gridy = 3;
        form.add(new JLabel("End") {{
            setLabelFor(endTime);
        }}, c);
        c.gridy = 4;
        form.add(new JLabel("Duration") {{
            setLabelFor(duration);
        }}, c);

        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 2;
        c.weightx = 1;
        form.add(examName, c);
        c.gridy = 1;
        form.add(examDesc, c);

        c.gridy = 2;
        c.gridwidth = 1;
        form.add(startDate, c);
        c.gridx = 2;
        form.add(startTime, c);

        c.gridx = 1;
        c.gridy = 3;
        form.add(endDate, c);
        c.gridx = 2;
        form.add(endTime, c);

        c.gridx = 1;
        c.gridy = 4;
        form.add(duration, c);

        form.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(form, BorderLayout.CENTER);

        buttonOK = new JButton("OK");
        buttonCancel = new JButton("Cancel");
        panel.add(new JPanel(new FlowLayout(FlowLayout.RIGHT)) {{
            add(buttonOK);
            add(buttonCancel);
        }}, BorderLayout.SOUTH);
    }

    private void onOK() {
        exam.name = examName.getText();
        exam.desc = examDesc.getText();
        exam.startTime = LocalDateTime.of(startDate.getDate(), startTime.getTime());
        exam.endTime = LocalDateTime.of(endDate.getDate(), endTime.getTime());
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
