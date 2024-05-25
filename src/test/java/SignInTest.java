import static org.junit.jupiter.api.Assertions.*;

import GUI.MainBoard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import utils.HashGenerator;

public class SignInTest {
    private MainBoard mainBoard;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private File userDataDir;
    private String username = "testUser";
    private String parentPassword = "parentPassword";
    private String childPassword = "childPassword";

    @BeforeEach
    public void setUp() throws Exception {
        // 创建 MainBoard 对象
        mainBoard = new MainBoard();

        // 初始化必要的字段
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        mainBoard.usernameField = usernameField;
        mainBoard.passwordField = passwordField;

        // 创建测试所需的目录和文件
        userDataDir = new File("src/main/java/UserData/" + username);
        userDataDir.mkdirs();
        File userFile = new File(userDataDir, username + ".json");

        // 创建用户数据文件
        JSONObject userData = new JSONObject();
        userData.put("parent_password", HashGenerator.generateSHA256(parentPassword));
        userData.put("child_password", HashGenerator.generateSHA256(childPassword));
        userData.put("user_name", username); // 添加用户名到 JSON 数据
        try (FileWriter fileWriter = new FileWriter(userFile)) {
            fileWriter.write(userData.toJSONString());
        }
    }

    @AfterEach
    public void tearDown() {
        // 删除测试文件和目录
        deleteDirectory(new File("src/main/java/UserData/" + username));
    }

    private void deleteDirectory(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDirectory(f);
            }
        }
        file.delete();
    }

    @Test
    public void testSignInSuccessAsParent() {
        // 设置用户名和正确的父密码
        usernameField.setText(username);
        passwordField.setText(parentPassword);

        // 调用 signIn 方法
        mainBoard.signIn();

        // 验证 currentUser 不为 null
        assertNotNull(mainBoard.currentUser, "Current user should not be null");
        assertEquals(username, mainBoard.currentUser.getUser_name(), "User name should be 'testUser'");
        // 验证用户成功登录并且是父母身份
        assertTrue(mainBoard.isParent);
    }

    @Test
    public void testSignInSuccessAsChild() {
        // 设置用户名和正确的子密码
        usernameField.setText(username);
        passwordField.setText(childPassword);

        // 调用 signIn 方法
        mainBoard.signIn();

        // 验证 currentUser 不为 null
        assertNotNull(mainBoard.currentUser, "Current user should not be null");


        // 验证用户成功登录并且是孩子身份
        assertFalse(mainBoard.isParent);
    }

    @Test
    public void testSignInFailureInvalidPassword() {
        // 设置用户名和错误的密码
        usernameField.setText(username);
        passwordField.setText("wrongPassword");

        // 调用 signIn 方法
        mainBoard.signIn();

        // 验证 currentUser 为 null
        assertNull(mainBoard.currentUser, "Current user should be null");
    }

    @Test
    public void testSignInFailureUserNotFound() {
        // 设置不存在的用户名
        usernameField.setText("nonExistentUser");
        passwordField.setText("somePassword");

        // 调用 signIn 方法
        mainBoard.signIn();

        // 验证 currentUser 为 null
        assertNull(mainBoard.currentUser, "Current user should be null");
    }
}
