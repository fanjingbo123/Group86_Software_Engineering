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

/**
 * A dialog window for editing the user's password.
 * This class provides a graphical user interface to change a user's password, ensuring the original password is correct and that the new password is entered identically twice.
 */
public class EditPasswordDialog extends JDialog {
    private JTextField originalPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField newPasswordAgainField;
    private User currentUser;
    public JButton editButton;
    public JButton backButton;

    /**
     * Constructs a new EditPasswordDialog.
     *
     * @param parent the parent frame from which the dialog is displayed
     * @param currentUser the user whose password is to be changed
     */
    public EditPasswordDialog(Frame parent, User currentUser) {
        super(parent, "Edit Password", true);
        setSize(300, 200);
        setLocationRelativeTo(parent);
        this.currentUser = currentUser;

        JPanel panel = new JPanel(new GridLayout(4, 2));

        panel.add(new JLabel("Original Password:"));
        originalPasswordField = new JTextField();
        panel.add(originalPasswordField);
        panel.add(new JLabel("New Password:"));
        newPasswordField = new JPasswordField();
        panel.add(newPasswordField);
        panel.add(new JLabel("New Password Again:"));
        newPasswordAgainField = new JPasswordField();
        panel.add(newPasswordAgainField);

        editButton = new JButton("Edit");
        backButton = new JButton("Back");
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

    /**
     * Handles the password editing process.
     * Validates the original password, checks if the new passwords match, and updates the user's password if all validations pass.
     */
    private void editPassword() {
        String originalPassword = HashGenerator.generateSHA256(originalPasswordField.getText());
        String newPassword = new String(newPasswordField.getPassword());
        String newPasswordAgain = new String(newPasswordAgainField.getPassword());

        if (!originalPassword.equals(currentUser.getParent_password())) {
            JOptionPane.showMessageDialog(this, "Original password is incorrect.");
            refresh();
        }
        else if (!newPassword.equals(newPasswordAgain)) {
            JOptionPane.showMessageDialog(this, "New passwords do not match.");
            refresh();
        }
        else if (newPassword.isEmpty() || newPasswordAgain.isEmpty()) {
            JOptionPane.showMessageDialog(this, "New passwords is empty");
            refresh();
        }

        // Update user's password
        currentUser.setParent_password(HashGenerator.generateSHA256(newPassword));

        // Save updated user information to JSON file
        saveUserToFile();

        JOptionPane.showMessageDialog(this, "Password updated successfully.");
    }

    /**
     * Saves the updated user information to a JSON file.
     * This method serializes the User object into JSON format and writes it to a file on disk.
     */
    private void saveUserToFile() {
        String username = currentUser.getUser_name();
        String userFileName = "src/main/java/UserData/" + username + "/" + username + ".json";

        try (FileWriter fileWriter = new FileWriter(new File(userFileName), false)) {
            fileWriter.write(JSON.toJSONString(currentUser));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error while saving user data: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
