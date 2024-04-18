package GUI;
import utils.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class DepositDialog extends JDialog {
    private JTextField amountField;
    private JButton depositButton;
    private JButton cancelButton;
    private User currentUser;
    private MainBoard mainBoard;
    private JLabel currentBalanceLabel;

    public DepositDialog(MainBoard owner, User currentUser) {
        super(owner, "Deposit Funds", true);
        setSize(300, 200);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        this.mainBoard = owner;
        this.currentUser = currentUser;

        // 显示当前余额
        currentBalanceLabel = new JLabel("Current Balance: $" + currentUser.getCurrent());
        currentBalanceLabel.setHorizontalAlignment(JLabel.CENTER);
        add(currentBalanceLabel, BorderLayout.NORTH);

        // 输入存款金额
        JPanel amountPanel = new JPanel();
        amountField = new JTextField(10);  // 设置文本框大小
        amountPanel.add(new JLabel("Amount:"));
        amountPanel.add(amountField);
        add(amountPanel, BorderLayout.CENTER);

        // 创建并添加按钮
        depositButton = new JButton("Deposit");
        cancelButton = new JButton("Cancel");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(depositButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setupActions();
    }

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
                    currentUser.setSaving(currentUser.getSaving() + amount); // 更新用户的当前余额
                    currentUser.setCurrent(currentUser.getCurrent() - amount);
                    mainBoard.logTransaction(amount, false, "saving", null);
                    mainBoard.logTransaction(amount, true, "current", null);
                    mainBoard.refreshTransactionRecordPage(); // 刷新交易记录页面
                    mainBoard.updateUserFile();
                    dispose(); // 关闭窗口
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
                dispose(); // 关闭窗口
            }
        });
    }
}
