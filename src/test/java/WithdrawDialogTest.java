import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import utils.HashGenerator;
import utils.User;
import GUI.WithdrawDialog;

public class WithdrawDialogTest {
    private WithdrawDialog withdrawDialog;
    private User testUser;
    private String transactionFilePath;

    @BeforeEach
    public void setUp() throws Exception {
        // 创建一个测试用户对象
        testUser = new User("testUser", HashGenerator.generateSHA256("parentPassword"), "childPassword");
        testUser.setCurrent(500.0);  // 初始化current账户余额
        testUser.setSaving(100.0);   // 初始化saving账户余额

        // 创建测试所需的目录和文件
        String userDir = "src/main/java/UserData/testUser";
        transactionFilePath = userDir + "/testUser_transaction.json";
        new File(userDir).mkdirs();
        if (!Files.exists(Paths.get(transactionFilePath))) {
            Files.createFile(Paths.get(transactionFilePath));
        }

        // 初始化 WithdrawDialog 对象，不传递 MainBoard 对象
        withdrawDialog = new WithdrawDialog(null, testUser);

        // 使用反射设置取款金额字段的初始值
        setField(withdrawDialog, "amountField", "200");
    }

    @Test
    public void testWithdrawFailureInvalidAmount() throws Exception {
        // 设置无效的取款金额
        setField(withdrawDialog, "amountField", "-100");

        // 获取 withdrawButton 并模拟点击 "Withdraw" 按钮
        JButton withdrawButton = (JButton) getField(withdrawDialog, "withdrawButton");
        clickButton(withdrawButton);

        // 验证取款是否未更新
        assertEquals(500.0, testUser.getCurrent(), "Current account should not be updated");
        assertEquals(100.0, testUser.getSaving(), "Saving account should not be updated");
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

    private void clickButton(JButton button) {
        for (ActionListener listener : button.getActionListeners()) {
            listener.actionPerformed(new ActionEvent(button, ActionEvent.ACTION_PERFORMED, null));
        }
    }
}
