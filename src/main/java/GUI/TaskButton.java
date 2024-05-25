package GUI;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * A specialized JButton that carries an additional task identifier.
 * This button is used in contexts where a button needs to be associated with a specific task,
 * allowing for easier handling of task-related actions in the GUI.
 */
public class TaskButton extends JButton {
    public String task_id;  // The identifier for the task associated with this button

    /**
     * Constructs a TaskButton with specified text and task identifier.
     *
     * @param text The text to display on the button.
     * @param task_id The unique identifier for the task associated with this button.
     */
    public TaskButton(String text, String task_id) {
        super(text);  // Calls the parent class constructor to set the button text
        this.task_id = task_id;  // Sets the task identifier
    }
}
