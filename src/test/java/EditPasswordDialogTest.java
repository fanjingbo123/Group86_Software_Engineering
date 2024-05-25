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

public class EditPasswordDialogTest {
    private EditPasswordDialog editPasswordDialog;
    private User testUser;

    @BeforeEach
    public void setUp() throws Exception {
        // 创建一个测试用户对象
        testUser = new User("testUser", HashGenerator.generateSHA256("originalPassword"), "childPassword");

        // 初始化 EditPasswordDialog 对象
        editPasswordDialog = new EditPasswordDialog(null, testUser);

        // 使用反射设置密码字段的初始值
        setField(editPasswordDialog, "originalPasswordField", "originalPassword");
        setField(editPasswordDialog, "newPasswordField", "newPassword");
        setField(editPasswordDialog, "newPasswordAgainField", "newPassword");
    }

    @Test
    public void testEditPasswordSuccess() throws Exception {
        // 模拟点击 "Edit" 按钮
        JButton editButton = (JButton) getField(editPasswordDialog, "editButton");
        for (ActionListener listener : editButton.getActionListeners()) {
            listener.actionPerformed(new ActionEvent(editButton, ActionEvent.ACTION_PERFORMED, null));
        }

        // 验证密码是否更新-
        assertEquals(HashGenerator.generateSHA256("newPassword"), testUser.getParent_password(), "Password should be updated to newPassword");

        // 确认对话框已关闭
        assertFalse(editPasswordDialog.isVisible(), "Dialog should be closed after editing password");
    }

    @Test
    public void testEditPasswordFailureIncorrectOriginalPassword() throws Exception {
        // 设置错误的原始密码
        setField(editPasswordDialog, "originalPasswordField", "wrongPassword");

        // 模拟点击 "Edit" 按钮
        JButton editButton = (JButton) getField(editPasswordDialog, "editButton");
        for (ActionListener listener : editButton.getActionListeners()) {
            listener.actionPerformed(new ActionEvent(editButton, ActionEvent.ACTION_PERFORMED, null));
        }

        // 验证密码是否未更新
        assertEquals(HashGenerator.generateSHA256("originalPassword"), testUser.getParent_password(), "Password should not be updated with incorrect original password");
    }

    @Test
    public void testEditPasswordFailureNewPasswordsDoNotMatch() throws Exception {
        // 设置不匹配的新密码
        setField(editPasswordDialog, "newPasswordField", "newPassword");
        setField(editPasswordDialog, "newPasswordAgainField", "differentPassword");

        // 模拟点击 "Edit" 按钮
        JButton editButton = (JButton) getField(editPasswordDialog, "editButton");
        for (ActionListener listener : editButton.getActionListeners()) {
            listener.actionPerformed(new ActionEvent(editButton, ActionEvent.ACTION_PERFORMED, null));
        }

        // 验证密码是否未更新
        assertEquals(HashGenerator.generateSHA256("originalPassword"), testUser.getParent_password(), "Password should not be updated when new passwords do not match");
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
}
