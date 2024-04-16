package GUI;

import javax.swing.*;
import java.awt.event.ActionListener;

public class TaskButton extends JButton {
    public String task_id;

    public TaskButton(String text, String task_id) {
        super(text);
        this.task_id = task_id;
    }
}
