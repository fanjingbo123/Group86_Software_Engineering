package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonEditor extends DefaultCellEditor {
    private JButton btn;
    private ActionListener defaultActionListener;

    public ButtonEditor(String buttonText) {
        super(new JTextField());
        this.setClickCountToStart(1);
        this.btn = new JButton(buttonText);
        this.defaultActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = -1;
                if (e.getSource() instanceof JButton) {
                    JButton button = (JButton) e.getSource();
                    JTable table = (JTable) SwingUtilities.getAncestorOfClass(JTable.class, button);
                    if (table != null) {
                        row = table.getSelectedRow();
                    }
                }
                System.out.println(buttonText + " button clicked in row: " + row);
            }
        };
        this.btn.addActionListener(defaultActionListener);
    }

    public void setActionListener(ActionListener actionListener) {
        this.btn.removeActionListener(defaultActionListener);
        this.btn.addActionListener(actionListener);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return btn;
    }
}
