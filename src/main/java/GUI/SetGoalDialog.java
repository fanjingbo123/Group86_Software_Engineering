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

/**
 * A dialog window for setting financial goals for the user.
 * This class provides a graphical user interface to input and save goals related to financial planning.
 */
public class SetGoalDialog extends JDialog {
    private final JTextField goalContentField;
    private final JTextField goalValueField;
    private final JButton SetButton;
    private final JButton cancelButton;
    private User currentUser; // Assuming User class is used to manage user data
    private MainBoard mainBoard;

    /**
     * Constructs a new SetGoalDialog.
     *
     * @param owner the parent frame from which the dialog is displayed
     * @param currentUser the user whose goals are to be set
     */
    public SetGoalDialog(Frame owner, User currentUser) {
        super(owner, "Set Goals", true);
        this.mainBoard = (MainBoard) owner;
        this.currentUser = currentUser;
        setSize(300, 200);
        setLocationRelativeTo(owner);
        setLayout(new GridLayout(3, 2));

        // Add fields and labels for goal content and value
        add(new JLabel("Goal Content:"));
        goalContentField = new JTextField();
        add(goalContentField);

        add(new JLabel("Goal Value:"));
        goalValueField = new JTextField();
        add(goalValueField);

        // Initialize and add buttons
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

    /**
     * Handles setting the goal by retrieving input values, validating them, and saving them.
     * This method updates the user's goal information and saves it to a file.
     */
    private void SetGoal() {
        String goalContent = goalContentField.getText();
        double goalValue = 0.0;

        try {
            goalValue = Double.parseDouble(goalValueField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for the goal value.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update the current user's goal information
        currentUser.setGoal_content(goalContent);
        currentUser.setGoal_value(goalValue);

        // Save updated user information
        updateUserFile();
        mainBoard.homepage(); // Assuming this method updates the main board or main interface
        dispose(); // Close the dialog after saving
    }

    /**
     * Updates the user file with the latest user data.
     * This method serializes the user data to JSON and writes it to a user-specific file.
     */
    private void updateUserFile() {
        String username = currentUser.getUser_name();
        String jsonString = JSON.toJSONString(currentUser, String.valueOf(SerializerFeature.PrettyFormat));

        String packagePath = "src/main/java/UserData/" + username;
        File packageDirectory = new File(packagePath);
        if (!packageDirectory.exists()) {
            packageDirectory.mkdirs(); // Create directory if it does not exist
        }

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
