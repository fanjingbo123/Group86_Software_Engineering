package GUI;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import utils.Task;
import utils.User;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class TaskInputDialog extends JDialog {
    private User currentUser;

    private JTextField taskContentField;
    private JComboBox<String> taskLevelComboBox;
    private JComboBox<Double> rewardField;
    private JComboBox<Integer> yearComboBox;
    private JComboBox<Integer> monthComboBox;
    private JComboBox<Integer> dayComboBox;

    public TaskInputDialog(Frame parent, User currentUser) {
        super(parent, "Add Task", true);
        setSize(500, 250);
        setLocationRelativeTo(parent);

        this.currentUser = currentUser;

        JPanel panel = new JPanel(new GridLayout(6, 2));

        JLabel taskContentLabel = new JLabel("Task Content:");
        taskContentField = new JTextField();
        JLabel taskLevelLabel = new JLabel("Task Level:");
        String[] levels = {"1", "2", "3", "4", "5"};
        taskLevelComboBox = new JComboBox<>(levels);
        JLabel rewardLabel = new JLabel("Reward:");
        rewardField = new JComboBox<>();
        for (int i = 1; i <= 100; i++) {
            rewardField.addItem((double)i);
        }

        JLabel deadlineLabel = new JLabel("Deadline:");

        JPanel deadlinePanel = new JPanel(new GridLayout(1, 3));
        yearComboBox = new JComboBox<>();
        monthComboBox = new JComboBox<>();
        dayComboBox = new JComboBox<>();
        deadlinePanel.add(yearComboBox);
        deadlinePanel.add(monthComboBox);
        deadlinePanel.add(dayComboBox);

        JButton addToTaskListButton = new JButton("Add to List");
        JButton cancelButton = new JButton("Cancel");

        panel.add(taskContentLabel);
        panel.add(taskContentField);
        panel.add(taskLevelLabel);
        panel.add(taskLevelComboBox);
        panel.add(rewardLabel);
        panel.add(rewardField);
        panel.add(deadlineLabel);
        panel.add(deadlinePanel);
        panel.add(addToTaskListButton);
        panel.add(cancelButton);

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        add(panel);

        // Populate yearComboBox with current year and next 10 years
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear; i <= currentYear + 10; i++) {
            yearComboBox.addItem(i);
        }

        // Populate monthComboBox with months from 1 to 12
        for (int i = 1; i <= 12; i++) {
            monthComboBox.addItem(i);
        }

        // Populate dayComboBox based on selected month and year
        updateDayComboBox();
        monthComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDayComboBox();
            }
        });

        addToTaskListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTaskToList();
            }
        });
    }

    private void updateDayComboBox() {
        int year = (int) yearComboBox.getSelectedItem();
        int month = (int) monthComboBox.getSelectedItem();
        int daysInMonth = new GregorianCalendar(year, month - 1, 1).getActualMaximum(Calendar.DAY_OF_MONTH);
        dayComboBox.removeAllItems();
        for (int i = 1; i <= daysInMonth; i++) {
            dayComboBox.addItem(i);
        }
    }

    private void addTaskToList() {
        String taskContent = taskContentField.getText();
        int taskLevel = Integer.parseInt((String) taskLevelComboBox.getSelectedItem());
        double reward = (double) rewardField.getSelectedItem();
        int year = (int) yearComboBox.getSelectedItem();
        int month = (int) monthComboBox.getSelectedItem();
        int day = (int) dayComboBox.getSelectedItem();

        // Create a Calendar object for the selected date
        Calendar selectedDate = new GregorianCalendar(year, month - 1, day);

        // Get the current date
        Calendar currentDate = Calendar.getInstance();

        // Check if the selected date is in the future
        if (selectedDate.after(currentDate)) {
            Date deadline = selectedDate.getTime();

            // Generate unique task ID
            String taskId = UUID.randomUUID().toString();

            // Create Task object
            Task newTask = new Task(taskId, taskContent, taskLevel, reward, deadline, false);

            // Add Task object to user's task list JSON file
            saveTaskToFile(newTask);

            dispose(); // Close the dialog
        } else {
            JOptionPane.showMessageDialog(this, "Please select a future date for the deadline.");
        }
    }


    private void saveTaskToFile(Task newTask) {
        String username = currentUser.getUser_name();
        String taskFileName = "src/main/java/UserData/" + username + "/" + username + "_task.json";

        try {
            File taskFile = new File(taskFileName);
            if (!taskFile.exists()) {
                taskFile.createNewFile();
            }

            // 读取任务文件内容
            StringBuilder taskJsonString = new StringBuilder();
            try (FileReader fileReader = new FileReader(taskFile)) {
                int character;
                while ((character = fileReader.read()) != -1) {
                    taskJsonString.append((char) character);
                }
            }

            // 解析任务文件内容为 JSON 数组
            JSONArray taskArray;
            if (taskJsonString.length() == 0) {
                taskArray = new JSONArray();
            } else {
                taskArray = JSON.parseArray(taskJsonString.toString());
            }

            // 创建新任务的 JSON 对象
            JSONObject newTaskJson = new JSONObject();
            newTaskJson.put("task_id", newTask.getTask_id());
            newTaskJson.put("task_content", newTask.getTask_content());
            newTaskJson.put("credit_level", newTask.getCredit_level());
            newTaskJson.put("reward", newTask.getReward());
            newTaskJson.put("DDL", newTask.getDDL());
            newTaskJson.put("flag", newTask.isFlag());

            // 将新任务添加到 JSON 数组
            taskArray.add(newTaskJson);

            // 将更新后的 JSON 数组写回任务文件
            try (FileWriter fileWriter = new FileWriter(taskFileName)) {
                fileWriter.write(taskArray.toJSONString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}