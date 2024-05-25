import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import utils.HashGenerator;
import utils.User;
import GUI.DepositDialog;

/**
 * Unit tests for the DepositDialog class.
 * These tests verify that the deposit functionality works correctly under various conditions,
 * particularly focusing on the handling of valid and invalid deposit amounts.
 */
public class DepositDialogTest {
    private DepositDialog depositDialog;
    private User testUser;

    /**
     * Sets up the environment for each test.
     * This method initializes a test user and a DepositDialog instance before each test.
     * It sets initial conditions like the user's account balances and the default amount field in the deposit dialog.
     *
     * @throws Exception if any reflection-based operations fail.
     */
    @BeforeEach
    public void setUp() throws Exception {
        // Create a test user with known credentials and balances.
        testUser = new User("testUser", HashGenerator.generateSHA256("parentPassword"), "childPassword");
        testUser.setCurrent(500.0);  // Set initial balance for the current account.
        testUser.setSaving(100.0);   // Set initial balance for the savings account.

        // Initialize DepositDialog object without a parent MainBoard.
        depositDialog = new DepositDialog(null, testUser);

        // Set the initial deposit amount using reflection to bypass GUI interaction.
        setField(depositDialog, "amountField", "200");
    }

    /**
     * Tests the deposit functionality with an invalid deposit amount to ensure that no changes are made to the user's account balances.
     * The test checks that the saving and current accounts are not incorrectly updated after an invalid deposit attempt.
     *
     * @throws Exception if any reflection-based operations fail.
     */
    @Test
    public void testDepositFailureInvalidAmount() throws Exception {
        // Set an invalid deposit amount.
        setField(depositDialog, "amountField", "-100");

        // Simulate clicking the deposit button.
        JButton depositButton = (JButton) getField(depositDialog, "depositButton");
        clickButton(depositButton);

        // Assert that the balances remain unchanged.
        assertEquals(100.0, testUser.getSaving(), "Saving account should not be updated");
        assertEquals(500.0, testUser.getCurrent(), "Current account should not be updated");
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
