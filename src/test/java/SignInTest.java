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

/**
 * Tests for the SignIn functionality in the MainBoard class.
 * These tests verify the correct behavior of the sign-in process under various scenarios including successful login as a parent,
 * successful login as a child, failure due to incorrect password, and failure due to non-existent user.
 */
public class SignInTest {
    private MainBoard mainBoard;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private File userDataDir;
    private String username = "testUser";
    private String parentPassword = "parentPassword";
    private String childPassword = "childPassword";

    /**
     * Sets up the testing environment for each test.
     * Initializes a MainBoard instance, simulates the environment by setting up user data files,
     * and prepares user data in JSON format to mimic database records.
     *
     * @throws Exception if file operations or setting up the environment fails.
     */
    @BeforeEach
    public void setUp() throws Exception {
        mainBoard = new MainBoard();
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        mainBoard.usernameField = usernameField;
        mainBoard.passwordField = passwordField;

        userDataDir = new File("src/main/java/UserData/" + username);
        userDataDir.mkdirs();
        File userFile = new File(userDataDir, username + ".json");

        JSONObject userData = new JSONObject();
        userData.put("parent_password", HashGenerator.generateSHA256(parentPassword));
        userData.put("child_password", HashGenerator.generateSHA256(childPassword));
        userData.put("user_name", username);
        try (FileWriter fileWriter = new FileWriter(userFile)) {
            fileWriter.write(userData.toJSONString());
        }
    }

    /**
     * Cleans up the testing environment by deleting the user data directory and its contents after each test.
     */
    @AfterEach
    public void tearDown() {
        deleteDirectory(new File("src/main/java/UserData/" + username));
    }

    /**
     * Helper method to recursively delete directories and files.
     * @param file The directory or file to delete.
     */
    private void deleteDirectory(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDirectory(f);
            }
        }
        file.delete();
    }

    /**
     * Tests successful sign-in as a parent.
     * Verifies that the user is correctly logged in with parent privileges and that the user's details are correctly set.
     */
    @Test
    public void testSignInSuccessAsParent() {
        usernameField.setText(username);
        passwordField.setText(parentPassword);
        mainBoard.signIn();

        assertNotNull(mainBoard.currentUser, "Current user should not be null");
        assertEquals(username, mainBoard.currentUser.getUser_name(), "User name should be 'testUser'");
        assertTrue(mainBoard.isParent, "User should be logged in as a parent");
    }

    /**
     * Tests successful sign-in as a child.
     * Verifies that the user is correctly logged in with child privileges.
     */
    @Test
    public void testSignInSuccessAsChild() {
        usernameField.setText(username);
        passwordField.setText(childPassword);
        mainBoard.signIn();

        assertNotNull(mainBoard.currentUser, "Current user should not be null");
        assertFalse(mainBoard.isParent, "User should be logged in as a child");
    }

    /**
     * Tests sign-in failure due to incorrect password.
     * Verifies that the user is not logged in when an incorrect password is used.
     */
    @Test
    public void testSignInFailureInvalidPassword() {
        usernameField.setText(username);
        passwordField.setText("wrongPassword");
        mainBoard.signIn();

        assertNull(mainBoard.currentUser, "Current user should be null");
    }

    /**
     * Tests sign-in failure due to a non-existent username.
     * Verifies that the user is not logged in when a username that does not exist is used.
     */
    @Test
    public void testSignInFailureUserNotFound() {
        usernameField.setText("nonExistentUser");
        passwordField.setText("somePassword");
        mainBoard.signIn();

        assertNull(mainBoard.currentUser, "Current user should be null");
    }
}