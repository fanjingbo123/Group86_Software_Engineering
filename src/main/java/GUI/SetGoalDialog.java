package GUI;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson2.JSON;
import utils.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SetGoalDialog extends JDialog {
    private final JTextField goalContentField;
    private final JTextField goalValueField;
    private final JButton SetButton;
    private final JButton cancelButton;
    private User currentUser; // Assuming User class is used to manage user data
    private MainBoard mainBoard;

    public SetGoalDialog(Frame owner, User currentUser) {
        super(owner, "Set Goals", true);
        this.mainBoard = (MainBoard) owner;
        this.currentUser = currentUser;
        setSize(300, 200);
        setLocationRelativeTo(owner);
        this.currentUser = currentUser;

        setLayout(new GridLayout(3, 2));

        // Add fields and labels
        add(new JLabel("Goal Content:"));
        goalContentField = new JTextField();
        add(goalContentField);

        add(new JLabel("Goal Value:"));
        goalValueField = new JTextField();
        add(goalValueField);

        // Add buttons
        SetButton = new JButton("Set");
        SetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SetGoal();
            }
        });
        add(SetButton);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the dialog
            }
        });
        add(cancelButton);
    }

    private void SetGoal() {
        String goalContent = goalContentField.getText();
        double goalValue = 0.0;

        try {
            goalValue = Double.parseDouble(goalValueField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for the goal value.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return; // Exit the method and do not save the goal
        }

        // Update the currentUser object
        currentUser.setGoal_content(goalContent);
        currentUser.setGoal_value(goalValue);

        // Assuming updateUserFile() updates the user information in file or database
        updateUserFile();
        mainBoard.homepage();
        dispose(); // Close the dialog after saving

    }

    private void updateUserFile() {
        String username = currentUser.getUser_name();
        String jsonString = JSON.toJSONString(currentUser, String.valueOf(SerializerFeature.PrettyFormat));

        // 创建以 username 命名的包目录
        String packagePath = "src/main/java/UserData/" + username;
        File packageDirectory = new File(packagePath);
        if (!packageDirectory.exists()) {
            packageDirectory.mkdirs(); // 创建目录及其父目录
        }

        // 创建以 username 命名的 JSON 文件
        String jsonFilePath = packagePath + "/" + username + ".json";
        File jsonFile = new File(jsonFilePath);
        try (FileWriter file = new FileWriter(jsonFile)) {
            file.write(jsonString);
            JOptionPane.showMessageDialog(this, "Update Successful");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error while updating user data");
            e.printStackTrace();
        }
    }
}
