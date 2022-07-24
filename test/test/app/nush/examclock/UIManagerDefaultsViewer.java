package test.app.nush.examclock;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.swing.TableComparatorChooser;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.Comparator;
import java.util.Map;

public class UIManagerDefaultsViewer {

    public static class UIEntry {
        final private String key;
        final private Object value;

        UIEntry(Map.Entry<Object, Object> e) {
            this.key = e.getKey().toString();
            this.value = e.getValue();
        }

        public String getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        public Class getValueClass() {
            if (value == null)
                return null; // ?!?!?!
            return value.getClass();
        }

        public String getClassName() {
            // doesn't handle arrays properly
            if (value == null)
                return "";

            return value.getClass().getName();
        }
    }

    public static class UIEntryRenderer extends DefaultTableCellRenderer {
        Color[] defaults = new Color[4];

        public UIEntryRenderer() {
            super();
            defaults[0] = UIManager.getColor("Table.background");
            defaults[1] = UIManager.getColor("Table.selectionBackground");
            defaults[2] = UIManager.getColor("Table.foreground");
            defaults[3] = UIManager.getColor("Table.selectionForeground");
        }

        public void setDefaultColors(Component cell, boolean isSelected) {
            cell.setBackground(defaults[isSelected ? 1 : 0]);
            cell.setForeground(defaults[isSelected ? 3 : 2]);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, column);
            if (table.convertColumnIndexToModel(column) == 1) // the value column
            {
                final EventTableModel tableModel = (EventTableModel) table.getModel();
                UIEntry e = (UIEntry) tableModel.getElementAt(row);
                JLabel l = (JLabel) cell;


                if (value instanceof Color) {
                    Color c = (Color) value;
                    cell.setBackground(c);
                    cell.setForeground(
                            c.getRed() + c.getGreen() + c.getBlue() >= 128 * 3
                                    ? Color.black : Color.white);
                    // choose either black or white depending on brightness

                    l.setText(String.format("Color 0x%08x (%d,%d,%d alpha=%d)",
                            c.getRGB(), c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()));
                    return cell;
                } else if (e.getKey().endsWith("ont"))
                // possible font, not always ".font"
                {
                    // fonts are weird, for some reason the value returned
                    // in the entry set of UIManager.getDefaults()
                    // is not the same type as the value "v" below                  
                    Object v = UIManager.get(e.getKey());
                    if (v instanceof javax.swing.plaf.FontUIResource) {
                        javax.swing.plaf.FontUIResource font =
                                (javax.swing.plaf.FontUIResource) v;
                        l.setText("Font " + font.getFontName() + " " + font.getSize());
                    }
                }
            }

            setDefaultColors(cell, isSelected);

            return cell;
        }
    }

    public static void main(String[] args) {
        FlatDarkLaf.setup();
        final EventList uiEntryList =
                GlazedLists.threadSafeList(new BasicEventList());

        for (Map.Entry<Object, Object> key : UIManager.getDefaults().entrySet()) {
            uiEntryList.add(new UIEntry(key));
        }

        final SortedList sortedUIEntryList = new SortedList(uiEntryList, null);

        // build a JTable
        String[] propertyNames = new String[]{"key", "value", "className"};
        String[] columnLabels = new String[]{"Key", "Value", "Class"};
        TableFormat tf = GlazedLists.tableFormat(UIEntry.class, propertyNames, columnLabels);
        EventTableModel etm = new EventTableModel(sortedUIEntryList, tf);

        JTable t = new JTable(etm);
        TableComparatorChooser tcc = new TableComparatorChooser(t,
                sortedUIEntryList, false);
        sortedUIEntryList.setComparator((Comparator) tcc.getComparatorsForColumn(0).get(0));
        // default to sort by the key

        t.setDefaultRenderer(Object.class, new UIEntryRenderer());

        JFrame f = new JFrame("UI Manager Defaults Viewer");
        // show the frame
        f.add(new JScrollPane(t));
        f.pack();
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

}