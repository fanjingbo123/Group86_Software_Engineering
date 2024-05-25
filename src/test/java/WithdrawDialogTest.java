import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import utils.HashGenerator;
import utils.User;
import GUI.WithdrawDialog;

/**
 * Tests for the WithdrawDialog class.
 * These tests ensure that the withdrawal functionality works correctly under various conditions,
 * particularly focusing on handling invalid and zero withdrawal amounts.
 */
public class WithdrawDialogTest {
    private WithdrawDialog withdrawDialog;
    private User testUser;
    private String transactionFilePath;

    /**
     * Sets up the environment for each test.
     * Initializes a test user and a WithdrawDialog instance before each test.
     * It sets initial conditions such as the user's account balances and creates necessary files and directories.
     *
     * @throws Exception if any file operations or setting up the environment fail.
     */
    @BeforeEach
    public void setUp() throws Exception {
        testUser = new User("testUser", HashGenerator.generateSHA256("parentPassword"), "childPassword");
        testUser.setCurrent(500.0);  // Set initial balance for the current account.
        testUser.setSaving(100.0);   // Set initial balance for the savings account.

        String userDir = "src/main/java/UserData/testUser";
        transactionFilePath = userDir + "/testUser_transaction.json";
        new File(userDir).mkdirs();
        if (!Files.exists(Paths.get(transactionFilePath))) {
            Files.createFile(Paths.get(transactionFilePath));
        }

        withdrawDialog = new WithdrawDialog(null, testUser);
        setField(withdrawDialog, "amountField", "200");
    }

    /**
     * Tests the withdrawal functionality with an invalid (negative) withdrawal amount.
     * Asserts that the balances remain unchanged after an invalid withdrawal attempt.
     *
     * @throws Exception if reflection or other operations fail.
     */
    @Test
    public void testWithdrawFailureInvalidAmount() throws Exception {
        setField(withdrawDialog, "amountField", "-100");
        JButton withdrawButton = (JButton) getField(withdrawDialog, "withdrawButton");
        clickButton(withdrawButton);

        assertEquals(500.0, testUser.getCurrent(), "Current account should not be updated");
        assertEquals(100.0, testUser.getSaving(), "Saving account should not be updated");
    }

    /**
     * Tests the withdrawal functionality with a zero withdrawal amount.
     * Asserts that the balances remain unchanged when a zero withdrawal is attempted.
     *
     * @throws Exception if reflection or other operations fail.
     */
    @Test
    public void testWithdrawFailureZeroAmount() throws Exception {
        JRadioButton savingAccountButton = (JRadioButton) getField(withdrawDialog, "savingAccountButton");
        savingAccountButton.setSelected(true);

        setField(withdrawDialog, "amountField", "0");
        JButton withdrawButton = (JButton) getField(withdrawDialog, "withdrawButton");
        clickButton(withdrawButton);

        assertEquals(500.0, testUser.getCurrent(), "Current account should not be updated");
        assertEquals(100.0, testUser.getSaving(), "Saving account should not be updated");
    }

    /**
     * Tests the withdrawal functionality with a non-numeric withdrawal amount.
     * Asserts that the balances remain unchanged when a non-numeric withdrawal is attempted.
     *
     * @throws Exception if reflection or other operations fail.
     */
    @Test
    public void testWithdrawFailureNonNumericAmount() throws Exception {
        JRadioButton currentAccountButton = (JRadioButton) getField(withdrawDialog, "currentAccountButton");
        currentAccountButton.setSelected(true);

        setField(withdrawDialog, "amountField", "abc");
        JButton withdrawButton = (JButton) getField(withdrawDialog, "withdrawButton");
        clickButton(withdrawButton);

        assertEquals(500.0, testUser.getCurrent(), "Current account should not be updated");
        assertEquals(100.0, testUser.getSaving(), "Saving account should not be updated");
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
