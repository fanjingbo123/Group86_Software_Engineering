package GUI;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;

public class MainBoard extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton signInButton;
    private JButton signUpButton;

    private User currentUser;

    public MainBoard() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        signInButton = new JButton("Sign In");
        signUpButton = new JButton("Sign Up");

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(signInButton);
        panel.add(signUpButton);

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signUpPanel();
            }
        });

        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signIn();
            }
        });

        add(panel);
        setVisible(true);
    }

    private void signUpPanel() {
        setTitle("Sign Up");
        getContentPane().removeAll();
        revalidate();

        JPanel panel = new JPanel(new GridLayout(5, 2));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField signUpUsernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField signUpPasswordField = new JPasswordField();
        JLabel confirmPassLabel = new JLabel("Confirm Password:");
        JPasswordField confirmPassField = new JPasswordField();

        JButton confirmSignUpButton = new JButton("Sign Up");
        JButton cancelSignUpButton = new JButton("Cancel");

        panel.add(usernameLabel);
        panel.add(signUpUsernameField);
        panel.add(passwordLabel);
        panel.add(signUpPasswordField);
        panel.add(confirmPassLabel);
        panel.add(confirmPassField);
        panel.add(confirmSignUpButton);
        panel.add(cancelSignUpButton);

        cancelSignUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backToLogin();
            }
        });

        confirmSignUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = signUpUsernameField.getText();
                String password = new String(signUpPasswordField.getPassword());
                String confirmPass = new String(confirmPassField.getPassword());

                // Check if the username already exists
                File userDir = new File("src/main/java/UserData/" + username);
                if (userDir.exists() && userDir.isDirectory()) {
                    JOptionPane.showMessageDialog(MainBoard.this, "Username already exists, please choose another one.");
                    signUpUsernameField.setText(""); // Clear the username field
                    signUpPasswordField.setText(""); // Clear the password field
                    confirmPassField.setText(""); // Clear the confirm password field
                } else if (!password.equals(confirmPass)) {
                    JOptionPane.showMessageDialog(MainBoard.this, "Passwords do not match, please try again.");
                    signUpPasswordField.setText(""); // Clear the password field
                    confirmPassField.setText(""); // Clear the confirm password field
                } else {
                    // Proceed with sign up logic
                    // Sample logic, you should implement your own user creation logic
                    currentUser = new User(username, password);
                    saveUserToFile();
                    homepage();
                }
            }
        });

        add(panel);
        setVisible(true);
    }

    private void backToLogin() {
        setTitle("Login");
        getContentPane().removeAll();
        revalidate();
        addComponentsToLoginPanel();
        setVisible(true);
    }

    private void saveUserToFile() {
        String username = currentUser.getUser_name();
        String jsonString = JSON.toJSONString(currentUser, String.valueOf(SerializerFeature.PrettyFormat));

        // 创建以 username 命名的包目录
        String packagePath = "/Users/williamma/IdeaProjects/jsp/Group86_Software_Engineering/src/main/java/UserData/" + username;
        File packageDirectory = new File(packagePath);
        if (!packageDirectory.exists()) {
            packageDirectory.mkdirs(); // 创建目录及其父目录
        }

        // 创建以 username 命名的 JSON 文件
        String jsonFilePath = packagePath + "/" + username + ".json";
        File jsonFile = new File(jsonFilePath);
        File transactionFile = new File("src/main/java/UserData/" + username + "/" + username + "_transaction.json");
        File taskFile = new File("src/main/java/UserData/" + username + "/" + username + "_task.json");
        try (FileWriter file = new FileWriter(jsonFile)) {
            file.write(jsonString);
            transactionFile.createNewFile();
            taskFile.createNewFile();
            JOptionPane.showMessageDialog(this, "Sign Up Successful");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error while saving user data");
            e.printStackTrace();
        }
    }


    private void homepage() {
        setTitle("User Dashboard");
        getContentPane().removeAll();
        revalidate();
        setSize(600, 500);

        JPanel panel = new JPanel(new BorderLayout());

        // Top Panel
        JPanel topPanel = new JPanel(new GridLayout(3, 1));
        JLabel usernameLabel = new JLabel("Username: " + currentUser.getUser_name());
        JLabel current = new JLabel("Current: " + currentUser.getCurrent());
        JLabel saving = new JLabel("Saving: " + currentUser.getSaving());
        JLabel credit = new JLabel("Credit_level: " + currentUser.getCredit_level());
        JLabel totalRewards = new JLabel("Total_rewards: " + currentUser.getTotal_reward());
        JLabel goals = new JLabel("Goals: " + currentUser.getGoal_content() + " " + currentUser.getGoal_value());
        JButton logoutButton = new JButton("Logout");
        JButton editButton = new JButton("Edit");
        topPanel.add(usernameLabel);
        topPanel.add(current);
        topPanel.add(saving);
        topPanel.add(credit);
        topPanel.add(totalRewards);
        topPanel.add(goals);
        topPanel.add(logoutButton);
        topPanel.add(editButton);
        panel.add(topPanel, BorderLayout.NORTH);

        // Middle Panel
        String[] columnNames = {"Task Content", "Credit Level", "Rewards", "DDL", "Finish", "Delete"};

        Object[][] rowData = loadTasks();

        ArrayList<Task> taskArrayList = new ArrayList<>();
        for (Object[] rowDatum : rowData) {
            if (!(boolean) rowDatum[7]) {
                Task task = new Task(
                        (String) rowDatum[6],
                        (String) rowDatum[0],
                        (int) rowDatum[1],
                        (double) rowDatum[2],
                        (Date) rowDatum[3],
                        (boolean) rowDatum[7]
                );
                taskArrayList.add(task);
            }
        }

        Object[][] filteredData = new Object[taskArrayList.size()][8];
        for (int i = 0; i < taskArrayList.size(); i++){
            filteredData[i][0] = taskArrayList.get(i).getTask_content();
            filteredData[i][1] = taskArrayList.get(i).getCredit_level();
            filteredData[i][2] = taskArrayList.get(i).getReward();
            filteredData[i][3] = taskArrayList.get(i).getDDL();
            filteredData[i][4] = taskArrayList.get(i).getTask_id();
            filteredData[i][5] = taskArrayList.get(i).isFlag();
        }

        for(int i = 0 ; i < filteredData.length; i++){
            for (int j = 0; j < 6; j++) {
                System.out.print(filteredData[i][j]+" ");
            }
            System.out.println();
        }


        JTable taskTable = new JTable(filteredData, columnNames);
        taskTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        taskTable.getColumn("Finish").setCellRenderer(new ButtonRenderer("Finish"));
        taskTable.getColumn("Delete").setCellRenderer(new ButtonRenderer("Delete"));

        // 为"Finish"列设置ButtonEditor
        // 为"Finish"列设置ButtonEditor
        taskTable.getColumn("Finish").setCellEditor(new ButtonEditor("Finish"));
        taskTable.getColumn("Delete").setCellEditor(new ButtonEditor("Delete"));

        ((ButtonEditor) taskTable.getColumn("Finish").getCellEditor()).setActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = taskTable.getSelectedRow();
                String task_id = (String) filteredData[row][4];

                double reward = updateTask(task_id);
                updateUserFile();

                //刷新界面
                homepage();
            }
        });

        ((ButtonEditor) taskTable.getColumn("Delete").getCellEditor()).setActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = taskTable.getSelectedRow();
                String task_id = (String) filteredData[row][4];

                updateTask(task_id);
                homepage();
            }
        });

        for (String column: columnNames){
            taskTable.getColumn(column).setMinWidth(90);
        }


        JScrollPane scrollPane = new JScrollPane(taskTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel
        JPanel bottomPanel = new JPanel();
        JButton setTaskButton = new JButton("Set Task");
        JButton refreshTaskListButton = new JButton("Refresh Task List");
        JButton setGoalsButton = new JButton("Set Goals");
        JButton transactionButton = new JButton("Transaction");
        bottomPanel.add(setTaskButton);
        bottomPanel.add(refreshTaskListButton);
        bottomPanel.add(setGoalsButton);
        bottomPanel.add(transactionButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backToLogin();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditPasswordDialog editPasswordDialog = new EditPasswordDialog(MainBoard.this, currentUser);
                editPasswordDialog.setVisible(true);
            }
        });

        setTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TaskInputDialog taskDialog = new TaskInputDialog(MainBoard.this, currentUser);
                taskDialog.setVisible(true);
            }
        });

        refreshTaskListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                homepage();
            }
        });
        add(panel);
        setVisible(true);
    }

    private void updateUserFile() {
        String username = currentUser.getUser_name();
        String jsonString = JSON.toJSONString(currentUser, String.valueOf(SerializerFeature.PrettyFormat));

        // 创建以 username 命名的包目录
        String packagePath = "src/main/java/UserData/" + username;
        File packageDirectory = new File(packagePath);
        if (!packageDirectory.exists()) {
            packageDirectory.mkdirs(); // 创建目录及其父目录
        }

        // 创建以 username 命名的 JSON 文件
        String jsonFilePath = packagePath + "/" + username + ".json";
        File jsonFile = new File(jsonFilePath);
        try (FileWriter file = new FileWriter(jsonFile)) {
            file.write(jsonString);
            JOptionPane.showMessageDialog(this, "Update Successful");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error while updating user data");
            e.printStackTrace();
        }
    }

    public double updateTask(String taskId) {

        String username = currentUser.getUser_name();
        String taskFileName = "src/main/java/UserData/" + username + "/" + username + "_task.json";

        Object[][] taskData = loadTasks();

        // 遍历任务数组，查找对应的任务对象
        for (int i = 0; i < taskData.length; i += 1) {
            String id = (String) taskData[i][6];
            if (taskId.equals(id)) {
                taskData[i][7] = true;

                JSONArray taskArray = new JSONArray();
                for (Object[] taskDatum : taskData) {
                    // 创建新任务的 JSON 对象
                    com.alibaba.fastjson.JSONObject newTaskJson = new com.alibaba.fastjson.JSONObject();
                    newTaskJson.put("task_id", taskDatum[6]);
                    newTaskJson.put("task_content", taskDatum[0]);
                    newTaskJson.put("credit_level", taskDatum[1]);
                    newTaskJson.put("reward", taskDatum[2]);
                    newTaskJson.put("DDL", taskDatum[3]);
                    newTaskJson.put("flag", taskDatum[7]);

                    // 将新任务添加到 JSON 数组
                    taskArray.add(newTaskJson);
                }
                // 将更新后的 JSON 数组写回任务文件
                try (FileWriter fileWriter = new FileWriter(taskFileName)) {
                    fileWriter.write(taskArray.toJSONString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                return (double)taskData[i][2];
            }
        }

        // 如果未找到对应的任务，则返回 null
        return 0;
    }

    private Object[][] loadTasks() {
        String username = currentUser.getUser_name();
        String taskFilePath = "src/main/java/UserData/" + username + "/" + username + "_task.json";

        try {
            //判断有没有json 文件
            File taskFile = new File(taskFilePath);
            if (!taskFile.exists()) {
                return new Object[0][0];
            }


            StringBuilder taskJsonString = new StringBuilder();
            try (FileReader fileReader = new FileReader(taskFile)) {
                int character;
                while ((character = fileReader.read()) != -1) {
                    taskJsonString.append((char) character);
                }
            }
            //如果 json 是空的
            com.alibaba.fastjson2.JSONArray taskArray;
            if (taskJsonString.length() == 0) {
                return new Object[0][0];
            } else {
                taskArray = JSON.parseArray(taskJsonString.toString());
            }

            Task[] tasks = taskArray.toJavaObject(Task[].class);

            Object[][] rowData = new Object[tasks.length][8];
            for (int i = 0; i < tasks.length; i++) {
                rowData[i][0] = tasks[i].getTask_content();
                rowData[i][1] = tasks[i].getCredit_level();
                rowData[i][2] = tasks[i].getReward();
                rowData[i][3] = tasks[i].getDDL();
                rowData[i][6] = tasks[i].getTask_id();
                rowData[i][7] = tasks[i].isFlag();
            }

            return rowData;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Object[0][0];
    }

    private void signIn() {
        // Implement sign in logic here
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        File userDataDir = new File("src/main/java/UserData/" + username);
        if (userDataDir.exists() && userDataDir.isDirectory()) {
            File userFile = new File(userDataDir, username + ".json");
            if (userFile.exists() && userFile.isFile()) {
                try (FileReader reader = new FileReader(userFile)) {
                    JSONObject userData = JSON.parseObject(reader);
                    String storedPassword = userData.getString("password");
                    if (password.equals(storedPassword)) {
                        currentUser = new User(userData.getString("user_name"), storedPassword);
                        homepage();
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid Password");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this, "User Data Not Found");
            }
        } else {
            JOptionPane.showMessageDialog(this, "User Not Found");
        }
    }

    private void addComponentsToLoginPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        setSize(300, 200);

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        signInButton = new JButton("Sign In");
        signUpButton = new JButton("Sign Up");

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(signInButton);
        panel.add(signUpButton);

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signUpPanel();
            }
        });

        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signIn();
            }
        });

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainBoard();
            }
        });
    }
}
