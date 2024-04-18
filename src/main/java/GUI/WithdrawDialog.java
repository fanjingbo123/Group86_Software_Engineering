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

public class WithdrawDialog extends JDialog {
    private JRadioButton currentAccountButton;
    private JRadioButton savingAccountButton;
    private ButtonGroup accountButtonGroup;
    private JTextField amountField;
    private JButton withdrawButton;
    private JButton cancelButton;
    private User currentUser;
    public MainBoard mainBoard;
    public WithdrawDialog(MainBoard owner, User currentUser) {
        super(owner, "Withdraw Funds", true);
        setSize(300, 200);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout()); // 使用边界布局
        this.mainBoard = owner;
        this.currentUser = currentUser;
        // 账户类型选择 - 使用单选按钮
        JPanel accountPanel = new JPanel();
        accountPanel.setBorder(BorderFactory.createTitledBorder("Select Account:"));
        currentAccountButton = new JRadioButton("Current");
        savingAccountButton = new JRadioButton("Saving");
        currentAccountButton.setSelected(true);  // 默认选择

        accountButtonGroup = new ButtonGroup();
        accountButtonGroup.add(currentAccountButton);
        accountButtonGroup.add(savingAccountButton);
        accountPanel.add(currentAccountButton);
        accountPanel.add(savingAccountButton);
        add(accountPanel, BorderLayout.NORTH);

        // 输入取款金额
        JPanel amountPanel = new JPanel(new FlowLayout());
        amountField = new JTextField(10);  // 设置文本框大小
        amountPanel.add(new JLabel("Amount:"));
        amountPanel.add(amountField);
        add(amountPanel, BorderLayout.CENTER);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        withdrawButton = new JButton("Withdraw");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(withdrawButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setupActions();
    }

    private void setupActions() {
        // 执行取款
        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentUser == null) {
                    JOptionPane.showMessageDialog(WithdrawDialog.this, "No user is currently logged in.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    String accountType = currentAccountButton.isSelected() ? "current" : "saving";
                    double amount = Double.parseDouble(amountField.getText());
                    if (amount <= 0) {
                        JOptionPane.showMessageDialog(WithdrawDialog.this, "Please enter a valid amount greater than 0", "Invalid Amount", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    performWithdraw(accountType, amount);
                    dispose(); // 关闭对话框
                    mainBoard.refreshTransactionRecordPage();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(WithdrawDialog.this, "Please enter a valid number for the amount", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        // 取消操作并关闭对话框
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void performWithdraw(String accountType, double amount) throws IOException {
        double accountBalance = 0;
        if (accountType.equals("current")) {
            accountBalance = currentUser.getCurrent();
        } else if (accountType.equals("saving")) {
            accountBalance = currentUser.getSaving();
        }

        // 检查取款金额是否超过账户余额
        if (amount > accountBalance) {
            JOptionPane.showMessageDialog(this, "Insufficient funds in your " + accountType + " account.", "Withdrawal Error", JOptionPane.ERROR_MESSAGE);
            return; // 退出方法，不继续执行取款
        }

        // 执行取款
        if (accountType.equals("current")) {
            currentUser.setCurrent(currentUser.getCurrent() - amount);
        } else if (accountType.equals("saving")) {
            currentUser.setSaving(currentUser.getSaving() - amount);
            currentUser.setCurrent(currentUser.getCurrent() + amount);
            recordTransaction(amount, false, "current", null);
        }

        // 记录交易
        recordTransaction(amount, true, accountType, null);  // 使用负数记录支出
        mainBoard.updateUserFile();
    }


    public void recordTransaction(double amount, boolean isExpense, String accountType, String time) throws IOException {
        if (time == null || time.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            time = sdf.format(new Date());
        }

        JSONObject transactionDetails = new JSONObject();
        transactionDetails.put("time", time);
        transactionDetails.put("amount", amount);
        transactionDetails.put("isExpense", isExpense);
        transactionDetails.put("accountType", accountType);

        updateTransactionFile(transactionDetails);
    }

    private void updateTransactionFile(JSONObject transactionDetails) throws IOException {
        String username = currentUser.getUser_name();
        String transactionFilePath = "src/main/java/UserData/" + username + "/" + username + "_transaction.json";
        JSONArray transactions;

        File transactionFile = new File(transactionFilePath);
        if (transactionFile.exists()) {
            String jsonText = new String(Files.readAllBytes(Paths.get(transactionFilePath)), StandardCharsets.UTF_8);
            transactions = JSON.parseArray(jsonText);
        } else {
            transactions = new JSONArray();
        }

        transactions.add(transactionDetails);

        try (FileWriter fileWriter = new FileWriter(transactionFile)) {
            fileWriter.write(transactions.toJSONString());
        }
    }

}
