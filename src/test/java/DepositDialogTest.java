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

public class DepositDialogTest {
    private DepositDialog depositDialog;
    private User testUser;

    @BeforeEach
    public void setUp() throws Exception {
        // 创建一个测试用户对象
        testUser = new User("testUser", HashGenerator.generateSHA256("parentPassword"), "childPassword");
        testUser.setCurrent(500.0);  // 初始化current账户余额
        testUser.setSaving(100.0);   // 初始化saving账户余额

        // 初始化 DepositDialog 对象，不传递 MainBoard 对象
        depositDialog = new DepositDialog(null, testUser);

        // 使用反射设置存款金额字段的初始值
        setField(depositDialog, "amountField", "200");
    }


    @Test
    public void testDepositFailureInvalidAmount() throws Exception {
        // 设置无效的存款金额
        setField(depositDialog, "amountField", "-100");

        // 获取 depositButton 并模拟点击 "Deposit" 按钮
        JButton depositButton = (JButton) getField(depositDialog, "depositButton");
        clickButton(depositButton);

        // 验证存款是否未更新
        assertEquals(100.0, testUser.getSaving(), "Saving account should not be updated");
        assertEquals(500.0, testUser.getCurrent(), "Current account should not be updated");
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
