package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Provides a button editor for table cells, allowing actions to be performed when the button is clicked.
 * This class extends DefaultCellEditor, using a JButton for the editing component.
 */
public class ButtonEditor extends DefaultCellEditor {
    private JButton btn;
    private ActionListener defaultActionListener;

    /**
     * Creates a ButtonEditor with a specific button text.
     * @param buttonText The text to display on the button.
     */
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

    /**
     * Sets a new ActionListener for the button, replacing the default action listener.
     * @param actionListener The new ActionListener to set.
     */
    public void setActionListener(ActionListener actionListener) {
        this.btn.removeActionListener(defaultActionListener);
        this.btn.addActionListener(actionListener);
    }

    /**
     * Overrides the getTableCellEditorComponent method to return the button as the component.
     * @param table The JTable that is asking the editor to edit; can be null.
     * @param value The value of the cell to be edited; it is up to the specific editor to interpret and draw the value.
     * @param isSelected True if the cell is to be rendered with highlighting.
     * @param row The row of the cell being edited.
     * @param column The column of the cell being edited.
     * @return The component for editing.
     */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return btn;
    }
}
