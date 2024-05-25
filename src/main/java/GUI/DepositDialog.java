package GUI;
import utils.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * A dialog window for depositing funds into a user's account.
 * This class provides a graphical user interface for users to deposit funds, showing the current balance and updating account values after successful transactions.
 */
public class DepositDialog extends JDialog {
    private JTextField amountField;
    private JButton depositButton;
    private JButton cancelButton;
    private User currentUser;
    private MainBoard mainBoard;
    private JLabel currentBalanceLabel;

    /**
     * Constructs a new DepositDialog.
     *
     * @param owner the parent frame from which the dialog is displayed
     * @param currentUser the user who is currently logged in and performing the deposit
     */
    public DepositDialog(MainBoard owner, User currentUser) {
        super(owner, "Deposit Funds", true);
        setSize(300, 200);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        this.mainBoard = owner;
        this.currentUser = currentUser;

        // Display the current balance
        currentBalanceLabel = new JLabel("Current Balance: $" + currentUser.getCurrent());
        currentBalanceLabel.setHorizontalAlignment(JLabel.CENTER);
        add(currentBalanceLabel, BorderLayout.NORTH);

        // Panel for entering deposit amount
        JPanel amountPanel = new JPanel();
        amountField = new JTextField(10);  // Set text field size
        amountPanel.add(new JLabel("Amount:"));
        amountPanel.add(amountField);
        add(amountPanel, BorderLayout.CENTER);

        // Create and add buttons
        depositButton = new JButton("Deposit");
        cancelButton = new JButton("Cancel");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(depositButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setupActions();
    }

    /**
     * Sets up the actions for deposit and cancel buttons.
     */
    private void setupActions() {
        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double amount = Double.parseDouble(amountField.getText());
                    if (amount <= 0) {
                        JOptionPane.showMessageDialog(DepositDialog.this, "Please enter a valid amount greater than 0", "Invalid Amount", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (amount > currentUser.getCurrent()) {
                        JOptionPane.showMessageDialog(DepositDialog.this, "Deposit amount exceeds current account balance", "Invalid Operation", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    // Update saving and current account balances
                    currentUser.setSaving(currentUser.getSaving() + amount);
                    currentUser.setCurrent(currentUser.getCurrent() - amount);
                    // Log transactions
                    mainBoard.logTransaction(amount, false, "saving", null);
                    mainBoard.logTransaction(amount, true, "current", null);
                    // Check for bonuses
                    if (!currentUser.hasReceived200Bonus() && currentUser.getSaving() >= 200) {
                        currentUser.setSaving(currentUser.getSaving() + 20);
                        currentUser.setReceived200Bonus(true);
                        mainBoard.logTransaction(20, false, "saving", null);
                    }
                    if (!currentUser.hasReceived500Bonus() && currentUser.getSaving() >= 500) {
                        currentUser.setSaving(currentUser.getSaving() + 50);
                        currentUser.setReceived500Bonus(true);
                        mainBoard.logTransaction(50, false, "saving", null);
                    }
                    // Refresh transaction records and update user file
                    mainBoard.refreshTransactionRecordPage();
                    mainBoard.updateUserFile();
                    dispose(); // Close the window
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(DepositDialog.this, "Please enter a valid number for the amount", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(DepositDialog.this, "Error while processing deposit: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the window
            }
        });
    }
}
