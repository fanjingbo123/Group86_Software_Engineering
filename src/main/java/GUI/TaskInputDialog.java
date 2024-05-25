package GUI;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import utils.Task;
import utils.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

/**
 * A dialog window for adding new tasks.
 * This class provides a graphical user interface for users to input new tasks with specific details such as content, level, reward, and deadline.
 */
public class TaskInputDialog extends JDialog {
    private User currentUser;

    private JTextField taskContentField;
    private JComboBox<String> taskLevelComboBox;
    private JComboBox<Double> rewardField;
    private JComboBox<Integer> yearComboBox;
    private JComboBox<Integer> monthComboBox;
    private JComboBox<Integer> dayComboBox;

    /**
     * Constructs a new TaskInputDialog.
     *
     * @param parent the parent frame from which the dialog is displayed
     * @param currentUser the user who is adding the task
     */
    public TaskInputDialog(Frame parent, User currentUser) {
        super(parent, "Add Task", true);
        setSize(500, 250);
        setLocationRelativeTo(parent);

        this.currentUser = currentUser;

        JPanel panel = new JPanel(new GridLayout(6, 2));

        // Initialize and add components for task input
        panel.add(new JLabel("Task Content:"));
        taskContentField = new JTextField();
        panel.add(taskContentField);

        panel.add(new JLabel("Task Level:"));
        String[] levels = {"1", "2", "3", "4", "5"};
        taskLevelComboBox = new JComboBox<>(levels);
        panel.add(taskLevelComboBox);

        panel.add(new JLabel("Reward:"));
        rewardField = new JComboBox<>();
        for (int i = 1; i <= 100; i++) {
            rewardField.addItem((double)i);
        }
        panel.add(rewardField);

        panel.add(new JLabel("Deadline:"));
        JPanel deadlinePanel = new JPanel(new GridLayout(1, 3));
        yearComboBox = new JComboBox<>();
        monthComboBox = new JComboBox<>();
        dayComboBox = new JComboBox<>();
        deadlinePanel.add(yearComboBox);
        deadlinePanel.add(monthComboBox);
        deadlinePanel.add(dayComboBox);
        panel.add(deadlinePanel);

        JButton addToTaskListButton = new JButton("Add to List");
        JButton cancelButton = new JButton("Cancel");
        panel.add(addToTaskListButton);
        panel.add(cancelButton);

        cancelButton.addActionListener(e -> dispose());

        add(panel);

        // Populate year, month, and day combo boxes with appropriate values
        populateDateComboBoxes();

        addToTaskListButton.addActionListener(e -> addTaskToList());
    }

    /**
     * Populates year, month, and day combo boxes based on current date.
     */
    private void populateDateComboBoxes() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear; i <= currentYear + 10; i++) {
            yearComboBox.addItem(i);
        }

        for (int i = 1; i <= 12; i++) {
            monthComboBox.addItem(i);
        }

        monthComboBox.addActionListener(e -> updateDayComboBox());
    }

    /**
     * Updates the day combo box to reflect the correct number of days based on the selected year and month.
     */
    private void updateDayComboBox() {
        int year = (int) yearComboBox.getSelectedItem();
        int month = (int) monthComboBox.getSelectedItem();
        int daysInMonth = new GregorianCalendar(year, month - 1, 1).getActualMaximum(Calendar.DAY_OF_MONTH);
        dayComboBox.removeAllItems();
        for (int i = 1; i <= daysInMonth; i++) {
            dayComboBox.addItem(i);
        }
    }

    /**
     * Handles the action of adding a new task to the list.
     * Validates the input data, creates a new Task object, and saves it to a file.
     */
    private void addTaskToList() {
        String taskContent = taskContentField.getText();
        int taskLevel = Integer.parseInt((String) taskLevelComboBox.getSelectedItem());
        double reward = (double) rewardField.getSelectedItem();
        int year = (int) yearComboBox.getSelectedItem();
        int month = (int) monthComboBox.getSelectedItem();
        int day = (int) dayComboBox.getSelectedItem();

        Calendar selectedDate = new GregorianCalendar(year, month - 1, day);
        Calendar currentDate = Calendar.getInstance();

        if (selectedDate.after(currentDate)) {
            Date deadline = selectedDate.getTime();
            String taskId = UUID.randomUUID().toString();
            Task newTask = new Task(taskId, taskContent, taskLevel, reward, deadline, false);
            saveTaskToFile(newTask);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a future date for the deadline.");
        }
    }

    /**
     * Saves the new task to a JSON file associated with the current user.
     *
     * @param newTask the new Task object to be saved
     */
    private void saveTaskToFile(Task newTask) {
        String username = currentUser.getUser_name();
        String taskFileName = "src/main/java/UserData/" + username + "/" + username + "_task.json";

        try {
            File taskFile = new File(taskFileName);
            if (!taskFile.exists()) {
                taskFile.createNewFile();
            }

            StringBuilder taskJsonString = new StringBuilder();
            try (FileReader fileReader = new FileReader(taskFile)) {
                int character;
                while ((character = fileReader.read()) != -1) {
                    taskJsonString.append((char) character);
                }
            }

            JSONArray taskArray = (taskJsonString.length() == 0) ? new JSONArray() : JSON.parseArray(taskJsonString.toString());
            JSONObject newTaskJson = new JSONObject();
            newTaskJson.put("task_id", newTask.getTask_id());
            newTaskJson.put("task_content", newTask.getTask_content());
            newTaskJson.put("credit_level", newTask.getCredit_level());
            newTaskJson.put("reward", newTask.getReward());
            newTaskJson.put("DDL", newTask.getDDL());
            newTaskJson.put("flag", newTask.isFlag());

            taskArray.add(newTaskJson);

            try (FileWriter fileWriter = new FileWriter(taskFileName)) {
                fileWriter.write(taskArray.toJSONString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
