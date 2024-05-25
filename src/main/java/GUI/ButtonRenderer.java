package GUI;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * A custom button renderer for table cells that displays a button with specified text.
 * This class extends JButton and implements TableCellRenderer to customize how buttons are displayed within table cells.
 */
public class ButtonRenderer extends JButton implements TableCellRenderer {

    private String buttonText;

    /**
     * Constructs a ButtonRenderer with specified text for the button.
     *
     * @param buttonText The text to be displayed on the button within the table cell.
     */
    public ButtonRenderer(String buttonText) {
        this.buttonText = buttonText;
    }

    /**
     * Returns the button component configured for display in a table cell.
     *
     * @param table the JTable that is asking the renderer to draw; can be {@code null}
     * @param value the value of the cell to be rendered. It is up to the specific renderer to interpret and draw the value. For example, if {@code value} is the string "true", it could be rendered as a string or it could be rendered as a checkbox that is checked. {@code null} is a valid value
     * @param isSelected true if the cell is to be rendered with the selection highlighted; otherwise false
     * @param hasFocus if true, render cell appropriately. For example, put a special border on the cell, if the cell can be edited, render in the color used to indicate editing
     * @param row the row index of the cell being drawn. When drawing the header, the value of {@code row} is -1
     * @param column the column index of the cell being drawn
     * @return the component used for drawing the cell. This method is used to configure the renderer appropriately before drawing.
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return new JButton(this.buttonText);
    }
}
