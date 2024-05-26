package TestPackage;

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

/**
 * Tests for the EditPasswordDialog class.
 * These tests ensure that the password editing functionality correctly handles password changes,
 * incorrect original passwords, and non-matching new passwords.
 */
public class EditPasswordDialogTest {
    private EditPasswordDialog editPasswordDialog;
    private User testUser;
    private File userFile;

    /**
     * Sets up the environment for each test.
     * Initializes a test user, writes user data to a JSON file, and creates an instance of EditPasswordDialog.
     * It also prepares the input fields for the tests using reflection.
     *
     * @throws Exception if setting up the environment fails due to file operations or accessing private fields.
     */
    @BeforeEach
    public void setUp() throws Exception {
        testUser = new User("testUser", HashGenerator.generateSHA256("123originalPassword"), "123childPassword");

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

        editPasswordDialog = new EditPasswordDialog(null, testUser);
        setField(editPasswordDialog, "originalPasswordField", "123originalPassword");
        setField(editPasswordDialog, "newPasswordField", "123newPassword");
        setField(editPasswordDialog, "newPasswordAgainField", "123newPassword");
    }

    /**
     * Cleans up after each test by deleting the test user data file.
     */
    @AfterEach
    public void tearDown() {
        if (userFile.exists()) {
            userFile.delete();
        }
    }

    /**
     * Tests successful password change.
     * Verifies that the password is updated correctly when valid inputs are provided.
     */
    @Test
    public void testEditPasswordSuccess() throws Exception {
        JButton editButton = (JButton) getField(editPasswordDialog, "editButton");
        clickButton(editButton);

        assertEquals(HashGenerator.generateSHA256("123newPassword"), testUser.getParent_password(), "Password should be updated to newPassword");
        assertFalse(editPasswordDialog.isVisible(), "Dialog should be closed after editing password");
    }

    /**
     * Tests password change failure due to incorrect original password.
     * Verifies that the password remains unchanged when the original password input is incorrect.
     */
    @Test
    public void testEditPasswordFailureIncorrectOriginalPassword() throws Exception {
        setField(editPasswordDialog, "originalPasswordField", "123wrongPassword");

        JButton editButton = (JButton) getField(editPasswordDialog, "editButton");
        clickButton(editButton);

        assertEquals(HashGenerator.generateSHA256("123originalPassword"), testUser.getParent_password(), "Password should not be updated with incorrect original password");
    }

    /**
     * Tests password change failure due to non-matching new passwords.
     * Verifies that the password remains unchanged when the new passwords provided do not match.
     */
    @Test
    public void testEditPasswordFailureNewPasswordsDoNotMatch() throws Exception {
        setField(editPasswordDialog, "newPasswordField", "123newPassword");
        setField(editPasswordDialog, "newPasswordAgainField", "123differentPassword");

        JButton editButton = (JButton) getField(editPasswordDialog, "editButton");
        clickButton(editButton);

        assertEquals(HashGenerator.generateSHA256("123originalPassword"), testUser.getParent_password(), "Password should not be updated when new passwords do not match");
    }

    /**
     * Uses reflection to set a field on an object.
     * This method is used to manipulate private fields within the test target object for testing purposes.
     *
     * @param target The object on which to set the field.
     * @param fieldName The name of the field to set.
     * @param value The value to set in the field.
     * @throws Exception if the field is not accessible or does not exist.
     */
    private void setField(Object target, String fieldName, String value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        if (field.getType() == JTextField.class) {
            ((JTextField) field.get(target)).setText(value);
        } else if (field.getType() == JPasswordField.class) {
            ((JPasswordField) field.get(target)).setText(value);
        }
    }

    /**
     * Uses reflection to retrieve a field value from an object.
     * This method is critical for accessing private fields of the test target for verification purposes.
     *
     * @param target The object from which to retrieve the field.
     * @param fieldName The name of the field to retrieve.
     * @return The value of the field.
     * @throws Exception if the field is not accessible or does not exist.
     */
    private Object getField(Object target, String fieldName) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(target);
    }

    /**
     * Simulates a button click by invoking all action listeners associated with the button.
     * This method is used to test button functionality without a GUI.
     *
     * @param button The button to be clicked.
     */
    private void clickButton(JButton button) {
        for (ActionListener listener : button.getActionListeners()) {
            listener.actionPerformed(new ActionEvent(button, ActionEvent.ACTION_PERFORMED, null));
        }
    }
}
