import static org.junit.jupiter.api.Assertions.*;

import GUI.EditPasswordDialog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import utils.HashGenerator;
import utils.User;

/**
 * Tests for the EditPasswordDialog class.
 * These tests ensure that the password editing functionality properly handles various scenarios,
 * including successful password change, incorrect original password, and non-matching new passwords.
 */
public class EditPasswordDialogTest {
    private EditPasswordDialog editPasswordDialog;
    private User testUser;

    /**
     * Sets up the testing environment before each test.
     * Initializes a test user and an instance of EditPasswordDialog with controlled inputs for testing.
     *
     * @throws Exception if reflection fails to access fields or other operations throw exceptions.
     */
    @BeforeEach
    public void setUp() throws Exception {
        testUser = new User("testUser", HashGenerator.generateSHA256("originalPassword"), "childPassword");
        editPasswordDialog = new EditPasswordDialog(null, testUser);
        setField(editPasswordDialog, "originalPasswordField", "originalPassword");
        setField(editPasswordDialog, "newPasswordField", "newPassword");
        setField(editPasswordDialog, "newPasswordAgainField", "newPassword");
    }

    /**
     * Tests that the password is successfully updated when valid original and new passwords are provided.
     * Asserts that the user's password is updated and that the dialog is closed after the operation.
     *
     * @throws Exception if reflection or other operations fail.
     */
    @Test
    public void testEditPasswordSuccess() throws Exception {
        JButton editButton = (JButton) getField(editPasswordDialog, "editButton");
        clickButton(editButton);
        assertEquals(HashGenerator.generateSHA256("newPassword"), testUser.getParent_password(), "Password should be updated to newPassword");
        assertFalse(editPasswordDialog.isVisible(), "Dialog should be closed after editing password");
    }

    /**
     * Tests that the password update fails when an incorrect original password is provided.
     * Asserts that the password remains unchanged.
     *
     * @throws Exception if reflection or other operations fail.
     */
    @Test
    public void testEditPasswordFailureIncorrectOriginalPassword() throws Exception {
        setField(editPasswordDialog, "originalPasswordField", "wrongPassword");
        JButton editButton = (JButton) getField(editPasswordDialog, "editButton");
        clickButton(editButton);
        assertEquals(HashGenerator.generateSHA256("originalPassword"), testUser.getParent_password(), "Password should not be updated with incorrect original password");
    }

    /**
     * Tests that the password update fails when the new passwords provided do not match.
     * Asserts that the password remains unchanged.
     *
     * @throws Exception if reflection or other operations fail.
     */
    @Test
    public void testEditPasswordFailureNewPasswordsDoNotMatch() throws Exception {
        setField(editPasswordDialog, "newPasswordField", "newPassword");
        setField(editPasswordDialog, "newPasswordAgainField", "differentPassword");
        JButton editButton = (JButton) getField(editPasswordDialog, "editButton");
        clickButton(editButton);
        assertEquals(HashGenerator.generateSHA256("originalPassword"), testUser.getParent_password(), "Password should not be updated when new passwords do not match");
    }

    /**
     * Uses reflection to set a field on an object.
     * This method is used to set values on private fields within the test target object.
     *
     * @param target The object on which to set the field.
     * @param fieldName The name of the field to set.
     * @param value The value to set.
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
