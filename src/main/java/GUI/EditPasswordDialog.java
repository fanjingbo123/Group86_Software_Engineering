package GUI;

import com.alibaba.fastjson2.JSON;
import utils.HashGenerator;
import utils.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EditPasswordDialog extends JDialog {
    private JTextField originalPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField newPasswordAgainField;
    private User currentUser;

    public EditPasswordDialog(Frame parent, User currentUser) {
        super(parent, "Edit Password", true);
        setSize(300, 200);
        setLocationRelativeTo(parent);

        this.currentUser = currentUser;

        JPanel panel = new JPanel(new GridLayout(4, 2));

        JLabel originalPasswordLabel = new JLabel("Original Password:");
        originalPasswordField = new JTextField();
        JLabel newPasswordLabel = new JLabel("New Password:");
        newPasswordField = new JPasswordField();
        JLabel newPasswordAgainLabel = new JLabel("New Password Again:");
        newPasswordAgainField = new JPasswordField();

        JButton editButton = new JButton("Edit");
        JButton backButton = new JButton("Back");

        panel.add(originalPasswordLabel);
        panel.add(originalPasswordField);
        panel.add(newPasswordLabel);
        panel.add(newPasswordField);
        panel.add(newPasswordAgainLabel);
        panel.add(newPasswordAgainField);
        panel.add(editButton);
        panel.add(backButton);

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editPassword();
                dispose();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        add(panel);
    }

    private void editPassword() {
        String originalPassword = HashGenerator.generateSHA256(originalPasswordField.getText());
        String newPassword = new String(newPasswordField.getPassword());
        String newPasswordAgain = new String(newPasswordAgainField.getPassword());

        if (!originalPassword.equals(currentUser.getParent_password())) {
            JOptionPane.showMessageDialog(this, "Original password is incorrect.");
            return;
        }

        if (!newPassword.equals(newPasswordAgain)) {
            JOptionPane.showMessageDialog(this, "New passwords do not match.");
            return;
        }

        // Update user's password
        currentUser.setParent_password(HashGenerator.generateSHA256(newPassword));

        // Save updated user information to JSON file
        saveUserToFile();

        JOptionPane.showMessageDialog(this, "Password updated successfully.");
    }

    private void saveUserToFile() {
        String username = currentUser.getUser_name();
        String userFileName = "src/main/java/UserData/" + username + "/" + username + ".json";

        try {
            File userFile = new File(userFileName);
            if (!userFile.exists()) {
                userFile.createNewFile();
            }

            // 将用户对象转换为 JSON 字符串
            String userJson = JSON.toJSONString(currentUser);

            // 将 JSON 字符串写入用户文件
            try (FileWriter fileWriter = new FileWriter(userFileName)) {
                fileWriter.write(userJson);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
