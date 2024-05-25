import static org.junit.jupiter.api.Assertions.*;

import GUI.EditPasswordDialog;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;

import com.alibaba.fastjson.JSONObject;
import utils.HashGenerator;
import utils.User;

public class EditPasswordDialogTest {
    private EditPasswordDialog editPasswordDialog;
    private User testUser;
    private File userFile;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a test user object
        testUser = new User("testUser", HashGenerator.generateSHA256("originalPassword"), "childPassword");

        // Create the test user data file
        String userDir = "src/main/java/UserData/testUser";
        new File(userDir).mkdirs();
        userFile = new File(userDir, "testUser.json");

        JSONObject userData = new JSONObject();
        userData.put("parent_password", testUser.getParent_password());
        userData.put("child_password", testUser.getChild_password());
        userData.put("username", testUser.getUser_name());

        try (FileWriter fileWriter = new FileWriter(userFile)) {
            fileWriter.write(userData.toJSONString());
        }

        // Initialize EditPasswordDialog object
        editPasswordDialog = new EditPasswordDialog(null, testUser);

        // Use reflection to set the initial values of the password fields
        setField(editPasswordDialog, "originalPasswordField", "originalPassword");
        setField(editPasswordDialog, "newPasswordField", "newPassword");
        setField(editPasswordDialog, "newPasswordAgainField", "newPassword");
    }

    @AfterEach
    public void tearDown() {
        // Delete the test user data file
        if (userFile.exists()) {
            userFile.delete();
        }
    }

    @Test
    public void testEditPasswordSuccess() throws Exception {
        // Simulate clicking the "Edit" button
        JButton editButton = (JButton) getField(editPasswordDialog, "editButton");
        for (ActionListener listener : editButton.getActionListeners()) {
            listener.actionPerformed(new ActionEvent(editButton, ActionEvent.ACTION_PERFORMED, null));
        }

        // Verify that the password has been updated
        assertEquals(HashGenerator.generateSHA256("newPassword"), testUser.getParent_password(), "Password should be updated to newPassword");

        // Confirm that the dialog has been closed
        assertFalse(editPasswordDialog.isVisible(), "Dialog should be closed after editing password");
    }

    @Test
    public void testEditPasswordFailureIncorrectOriginalPassword() throws Exception {
        // Set incorrect original password
        setField(editPasswordDialog, "originalPasswordField", "wrongPassword");

        // Simulate clicking the "Edit" button
        JButton editButton = (JButton) getField(editPasswordDialog, "editButton");
        for (ActionListener listener : editButton.getActionListeners()) {
            listener.actionPerformed(new ActionEvent(editButton, ActionEvent.ACTION_PERFORMED, null));
        }

        // Verify that the password has not been updated
        assertEquals(HashGenerator.generateSHA256("originalPassword"), testUser.getParent_password(), "Password should not be updated with incorrect original password");
    }

    @Test
    public void testEditPasswordFailureNewPasswordsDoNotMatch() throws Exception {
        // Set non-matching new passwords
        setField(editPasswordDialog, "newPasswordField", "newPassword");
        setField(editPasswordDialog, "newPasswordAgainField", "differentPassword");

        // Simulate clicking the "Edit" button
        JButton editButton = (JButton) getField(editPasswordDialog, "editButton");
        for (ActionListener listener : editButton.getActionListeners()) {
            listener.actionPerformed(new ActionEvent(editButton, ActionEvent.ACTION_PERFORMED, null));
        }

        // Verify that the password has not been updated
        assertEquals(HashGenerator.generateSHA256("originalPassword"), testUser.getParent_password(), "Password should not be updated when new passwords do not match");
    }

    private void setField(Object target, String fieldName, String value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        if (field.getType() == JTextField.class) {
            ((JTextField) field.get(target)).setText(value);
        } else if (field.getType() == JPasswordField.class) {
            ((JPasswordField) field.get(target)).setText(value);
        }
    }

    private Object getField(Object target, String fieldName) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(target);
    }
}
