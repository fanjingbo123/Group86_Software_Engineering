package GUI;

import utils.User;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A dialog window for withdrawing funds from user accounts.
 * This class provides the functionality to withdraw funds from either a current or savings account.
 */
public class WithdrawDialog extends JDialog {
    private JRadioButton currentAccountButton;
    private JRadioButton savingAccountButton;
    private ButtonGroup accountButtonGroup;
    private JTextField amountField;
    private JButton withdrawButton;
    private JButton cancelButton;
    private User currentUser;
    public MainBoard mainBoard;

    /**
     * Constructs a new WithdrawDialog.
     *
     * @param owner the MainBoard frame that owns this dialog
     * @param currentUser the user who is performing the withdrawal
     */
    public WithdrawDialog(MainBoard owner, User currentUser) {
        super(owner, "Withdraw Funds", true);
        setSize(300, 200);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        this.mainBoard = owner;
        this.currentUser = currentUser;

        // Account selection panel
        JPanel accountPanel = new JPanel();
        accountPanel.setBorder(BorderFactory.createTitledBorder("Select Account:"));
        currentAccountButton = new JRadioButton("Current");
        savingAccountButton = new JRadioButton("Saving");
        currentAccountButton.setSelected(true);

        accountButtonGroup = new ButtonGroup();
        accountButtonGroup.add(currentAccountButton);
        accountButtonGroup.add(savingAccountButton);
        accountPanel.add(currentAccountButton);
        accountPanel.add(savingAccountButton);
        add(accountPanel, BorderLayout.NORTH);

        // Amount input panel
        JPanel amountPanel = new JPanel(new FlowLayout());
        amountField = new JTextField(10);
        amountPanel.add(new JLabel("Amount:"));
        amountPanel.add(amountField);
        add(amountPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        withdrawButton = new JButton("Withdraw");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(withdrawButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setupActions();
    }

    /**
     * Sets up the action listeners for the withdrawal and cancel buttons.
     */
    private void setupActions() {
        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String accountType = currentAccountButton.isSelected() ? "current" : "saving";
                    double amount = Double.parseDouble(amountField.getText());
                    if (amount <= 0) {
                        JOptionPane.showMessageDialog(WithdrawDialog.this, "Please enter a valid amount greater than 0", "Invalid Amount", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    performWithdraw(accountType, amount);
                    dispose();
                    mainBoard.refreshTransactionRecordPage();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(WithdrawDialog.this, "Please enter a valid number for the amount", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(WithdrawDialog.this, "Error in processing the withdrawal", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(e -> dispose());
    }

    /**
     * Performs the withdrawal operation and updates the user's account balance.
     *
     * @param accountType the type of account from which to withdraw
     * @param amount the amount to withdraw
     * @throws IOException if there is an error writing the transaction to the file
     */
    private void performWithdraw(String accountType, double amount) throws IOException {
        double accountBalance = accountType.equals("current") ? currentUser.getCurrent() : currentUser.getSaving();

        if (amount > accountBalance) {
            JOptionPane.showMessageDialog(this, "Insufficient funds in your " + accountType + " account.", "Withdrawal Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (accountType.equals("current")) {
            currentUser.setCurrent(currentUser.getCurrent() - amount);
        } else {
            currentUser.setSaving(currentUser.getSaving() - amount);
        }

        recordTransaction(amount, true, accountType, null);
        mainBoard.updateUserFile();
    }

    /**
     * Records a transaction in the user's transaction file.
     *
     * @param amount the amount of the transaction
     * @param isExpense whether the transaction is an expense (true if withdrawal)
     * @param accountType the type of account affected
     * @param time the time of the transaction
     * @throws IOException if there is an error writing the transaction to the file
     */
    public void recordTransaction(double amount, boolean isExpense, String accountType, String time) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        time = (time == null || time.isEmpty()) ? sdf.format(new Date()) : time;

        JSONObject transactionDetails = new JSONObject();
        transactionDetails.put("time", time);
        transactionDetails.put("amount", amount);
        transactionDetails.put("isExpense", isExpense);
        transactionDetails.put("accountType", accountType);

        updateTransactionFile(transactionDetails);
    }

    /**
     * Updates the transaction file with a new transaction.
     *
     * @param transactionDetails the details of the new transaction to add
     * @throws IOException if there is an error accessing the file
     */
    private void updateTransactionFile(JSONObject transactionDetails) throws IOException {
        String username = currentUser.getUser_name();
        String transactionFilePath = "src/main/java/UserData/" + username + "/" + username + "_transaction.json";
        JSONArray transactions;

        File transactionFile = new File(transactionFilePath);
        if (!transactionFile.exists()) {
            transactionFile.createNewFile();
        }
        String jsonText = new String(Files.readAllBytes(Paths.get(transactionFilePath)), StandardCharsets.UTF_8);
        transactions = JSON.parseArray(jsonText);

        transactions.add(transactionDetails);
        try (FileWriter fileWriter = new FileWriter(transactionFile)) {
            fileWriter.write(transactions.toJSONString());
        }
    }
}
