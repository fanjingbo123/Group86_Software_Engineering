package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonEditor extends DefaultCellEditor {
    private JButton btn;
    String buttonText;
    public ButtonEditor(String buttonText) {
        super(new JTextField());
        this.setClickCountToStart(1);
        this.buttonText = buttonText;
        btn = new JButton(this.buttonText);
        btn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("按钮事件触发----");

            }
        });

    }
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

        return btn;
    }
}
