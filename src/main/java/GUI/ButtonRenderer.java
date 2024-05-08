package GUI;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ButtonRenderer extends JButton implements TableCellRenderer {

    String buttonText;
    public ButtonRenderer(String buttonText) {
        this.buttonText = buttonText;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return new JButton(this.buttonText);
    }
}

