package GUI;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import utils.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

public class MainBoard extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton signInButton;
    private JButton signUpButton;

    private User currentUser;
    private boolean isParent = false;

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
        setSize(330, 250);

        JPanel panel = new JPanel(new GridLayout(6, 2));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField signUpUsernameField = new JTextField();
        JLabel parentPasswordLabel = new JLabel("Parent Password:");
        JPasswordField parentSignUpPasswordField = new JPasswordField();
        JLabel confirmParentPassLabel = new JLabel("Confirm Parent Password:");
        JPasswordField confirmParentPassField = new JPasswordField();
        JLabel childPasswordLabel = new JLabel("Child Password:");
        JPasswordField childSignUpPasswordField = new JPasswordField();
        JLabel confirmChildPassLabel = new JLabel("Confirm Child Password:");
        JPasswordField confirmChildPassField = new JPasswordField();

        JButton confirmSignUpButton = new JButton("Sign Up");
        JButton cancelSignUpButton = new JButton("Cancel");

        panel.add(usernameLabel);
        panel.add(signUpUsernameField);
        panel.add(parentPasswordLabel);
        panel.add(parentSignUpPasswordField);
        panel.add(confirmParentPassLabel);
        panel.add(confirmParentPassField);

        panel.add(childPasswordLabel);
        panel.add(childSignUpPasswordField);
        panel.add(confirmChildPassLabel);
        panel.add(confirmChildPassField);

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
                String parent_password = new String(parentSignUpPasswordField.getPassword());
                String confirmParentPass = new String(confirmParentPassField.getPassword());
                String child_password = new String(childSignUpPasswordField.getPassword());
                String confirmChildPass = new String(confirmChildPassField.getPassword());

                // Check if the username already exists
                File userDir = new File("src/main/java/UserData/" + username);
                if (userDir.exists() && userDir.isDirectory()) {
                    JOptionPane.showMessageDialog(MainBoard.this, "Username already exists, please choose another one.");
                    refresh();
                } else if (parent_password.isEmpty() || confirmParentPass.isEmpty() || child_password.isEmpty() || confirmChildPass.isEmpty()) {
                    JOptionPane.showMessageDialog(MainBoard.this, "Passwords can't be null or empty.");
                    refresh();
                } else if (parent_password.equals(child_password)) {
                    JOptionPane.showMessageDialog(MainBoard.this, "Passwords can't be the same.");
                    refresh();
                } else if (!parent_password.equals(confirmParentPass)) {
                    JOptionPane.showMessageDialog(MainBoard.this, "Parent Passwords do not match, please try again.");
                    refresh();
                }
                else if (!child_password.equals(confirmChildPass)) {
                    JOptionPane.showMessageDialog(MainBoard.this, "Child Passwords do not match, please try again.");
                    refresh();
                }
                else {
                    currentUser = new User(username, HashGenerator.generateSHA256(parent_password), HashGenerator.generateSHA256(child_password));
                    saveUserToFile();
                    homepage();
                }
            }

            public void refresh(){
                signUpUsernameField.setText("");
                parentSignUpPasswordField.setText("");
                confirmParentPassField.setText("");
                childSignUpPasswordField.setText("");
                confirmChildPassField.setText("");
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
        String packagePath = "src/main/java/UserData/" + username;
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

    public void homepage() {
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
        if(isParent){
            topPanel.add(editButton);
        }
        panel.add(topPanel, BorderLayout.NORTH);

        // Middle Panel
        String[] columnNames = {"Task Content", "Credit Level", "Rewards", "DDL", "Finish", "Delete"};

        Object[][] rowData = loadTasks();

        ArrayList<Task> taskArrayList = getTasks(rowData);

        Object[][] filteredData = new Object[taskArrayList.size()][8];
        for (int i = 0; i < taskArrayList.size(); i++){
            filteredData[i][0] = taskArrayList.get(i).getTask_content();
            filteredData[i][1] = (Object) taskArrayList.get(i).getCredit_level();
            filteredData[i][2] = (Object) taskArrayList.get(i).getReward();
            filteredData[i][3] = taskArrayList.get(i).getDDL();
            filteredData[i][4] = taskArrayList.get(i).getTask_id();
            filteredData[i][5] = (Object) taskArrayList.get(i).isFlag();
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

                if(currentUser.getCredit_level() >= (int)filteredData[row][1]){
                    double reward = updateTask(task_id);  // 假设 updateTask 标记任务完成并返回奖励

                    // 更新用户的总奖励和当前余额
                    currentUser.setTotal_reward(currentUser.getTotal_reward() + reward);
                    currentUser.setCurrent(currentUser.getCurrent() + reward);

                    int newCreditLevel = currentUser.getCredit_level() + (int)(currentUser.getTotal_reward() / 50) - (int)((currentUser.getTotal_reward() - reward) / 50);
                    if (newCreditLevel != currentUser.getCredit_level()) {
                        currentUser.setCredit_level(newCreditLevel);  // 更新 credit_level
                        JOptionPane.showMessageDialog(null, "Congratulations! Your credit level has been increased to " + newCreditLevel);
                    }
                    try {
                        logTransaction((double)filteredData[row][2], false, "current", null);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    // 更新用户数据文件或数据库条目
                    updateUserFile();
                    // 刷新主界面以显示更新的数据
                    homepage();
                }else {
                    JOptionPane.showMessageDialog(null, "Your credit level is not high enough to complete this task.", "Task Completion Denied", JOptionPane.ERROR_MESSAGE);
                }
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
        if (isParent) {
            bottomPanel.add(setTaskButton);
        }
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

        setGoalsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetGoalDialog goalDialog = new SetGoalDialog(MainBoard.this, currentUser);
                goalDialog.setVisible(true);
            }
        });

        transactionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transactionRecordPage();
            }
        });
    }

    private static ArrayList<Task> getTasks(Object[][] rowData) {
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
        return taskArrayList;
    }

    public void transactionRecordPage() {
        setTitle("Transaction Record");
        getContentPane().removeAll();
        revalidate();
        setSize(600, 400); // 设置页面大小

        // 总体布局面板
        JPanel panel = new JPanel(new BorderLayout());

        // 顶部面板，用于显示标题和账户信息
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        // 标题标签
        JLabel titleLabel = new JLabel("Transaction Record", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(titleLabel);

        // 账户信息标签
        JLabel current = new JLabel("Current: " + currentUser.getCurrent(), SwingConstants.CENTER);
        JLabel saving = new JLabel("Saving: " + currentUser.getSaving(), SwingConstants.CENTER);
        topPanel.add(current);
        topPanel.add(saving);

        // 将顶部面板添加到主面板的北部
        panel.add(topPanel, BorderLayout.NORTH);

        // 创建表格的列名
        String[] columnNames = {"Time", "Expense/Income", "Account Type"};

        // 读取交易数据
        Object[][] data = loadTransactionData();

        // 创建表格
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table); // 让表格可滚动
        panel.add(scrollPane, BorderLayout.CENTER); // 添加到中间

        // 底部的按钮
        JPanel bottomPanel = new JPanel();
        JButton backButton = new JButton("Back");
        JButton withDrawButton = new JButton("Withdraw");
        JButton depositButton = new JButton("Deposit");

        if (isParent) {
            bottomPanel.add(withDrawButton);
            bottomPanel.add(depositButton);
        }
        bottomPanel.add(backButton);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                homepage(); // 返回主页面
            }
        });
        withDrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WithdrawDialog withdrawDialog = new WithdrawDialog(MainBoard.this, currentUser);
                withdrawDialog.setVisible(true);
            }
        });
        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DepositDialog depositDialog = new DepositDialog(MainBoard.this, currentUser);
                depositDialog.setVisible(true);
            }
        });

        // 把底部面板添加到主面板的南部
        panel.add(bottomPanel, BorderLayout.SOUTH);

        // 把主面板添加到frame
        add(panel);
        setVisible(true); // 显示页面
    }

    public void refreshTransactionRecordPage() {
        transactionRecordPage();  // 假设这是重载交易记录的现有方法
    }

    public void logTransaction(double amount, boolean isExpense, String accountType, String time) throws IOException {
        if (time == null || time.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            time = sdf.format(new Date());
        }

        JSONObject transactionDetails = new JSONObject();
        transactionDetails.put("time", time);
        transactionDetails.put("amount", Optional.of(amount));
        transactionDetails.put("isExpense", Optional.of(isExpense));
        transactionDetails.put("accountType", accountType);

        updateTransaction(transactionDetails);
    }

    private void updateTransaction(JSONObject transactionDetails) throws IOException {
        String username = currentUser.getUser_name();
        String transactionFilePath = "src/main/java/UserData/" + username + "/" + username + "_transaction.json";
        JSONArray transactions = null;

        File transactionFile = new File(transactionFilePath);
        if (transactionFile.exists() && transactionFile.length() != 0) {
            String jsonText = new String(Files.readAllBytes(Paths.get(transactionFilePath)), StandardCharsets.UTF_8);
            transactions = JSON.parseArray(jsonText);
        }

        if (transactions == null) {
            transactions = new JSONArray(); // 确保transactions不为null
        }

        transactions.add(transactionDetails);

        try (FileWriter fileWriter = new FileWriter(transactionFile)) {
            fileWriter.write(transactions.toJSONString());
        }
    }

    private Object[][] loadTransactionData() {
        String username = currentUser.getUser_name();
        String transactionFileName = "src/main/java/UserData/" + username + "/" + username + "_transaction.json";
        File transactionFile = new File(transactionFileName);

        if (!transactionFile.exists()) {
            return new Object[0][0]; // 没有交易记录，返回空数组
        }

        try {
            // 读取文件内容
            String jsonText = new String(Files.readAllBytes(transactionFile.toPath()), StandardCharsets.UTF_8);
            JSONArray transactions = JSON.parseArray(jsonText);

            if (transactions == null) {
                transactions = new JSONArray(); // 确保transactions不为null
            }

            Object[][] data = new Object[transactions.size()][3];
            for (int i = 0; i < transactions.size(); i++) {
                JSONObject transaction = transactions.getJSONObject(i);
                data[i][0] = transaction.getString("time");
                data[i][1] = transaction.getBoolean("isExpense") ? "-" + transaction.getDouble("amount") : "+" + transaction.getDouble("amount");
                data[i][2] = transaction.getString("accountType");
            }
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return new Object[0][0];
        }
    }

    public void updateUserFile() {
        String username = currentUser.getUser_name();
        String jsonString = JSON.toJSONString(currentUser, String.valueOf(SerializerFeature.PrettyFormat));

        // 创建以 username 命名的包目录
        String packagePath = "src/main/java/UserData/" + username;
        File packageDirectory = new File(packagePath);
        if (!packageDirectory.exists()) {
            packageDirectory.mkdirs();
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
                taskData[i][7] = (Object) true;

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
                rowData[i][1] = (Object) tasks[i].getCredit_level();
                rowData[i][2] = (Object) tasks[i].getReward();
                rowData[i][3] = tasks[i].getDDL();
                rowData[i][6] = tasks[i].getTask_id();
                rowData[i][7] = (Object) tasks[i].isFlag();
            }

            return rowData;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Object[0][0];
    }

    private void creatUser(JSONObject userData, String storedParentPassword, String storedChildPassword){
        currentUser = new User(userData.getString("user_name"), storedParentPassword, storedChildPassword);
        currentUser.setCurrent(userData.getDoubleValue("current"));
        currentUser.setSaving(userData.getDoubleValue("saving"));
        currentUser.setTotal_reward(userData.getDoubleValue("total_reward"));
        currentUser.setGoal_content(userData.getString("goal_content"));
        currentUser.setGoal_value(userData.getDoubleValue("goal_value"));
        currentUser.setCredit_level(userData.getIntValue("credit_level"));
    }

    private void signIn() {
        String username = usernameField.getText();
        String password = HashGenerator.generateSHA256(new String(passwordField.getPassword()));

        File userDataDir = new File("src/main/java/UserData/" + username);
        if (userDataDir.exists() && userDataDir.isDirectory()) {
            File userFile = new File(userDataDir, username + ".json");
            if (userFile.exists() && userFile.isFile()) {
                try (FileReader reader = new FileReader(userFile)) {
                    JSONObject userData = JSON.parseObject(reader);
                    String storedParentPassword = userData.getString("parent_password");
                    String storedChildPassword = userData.getString("child_password");
                    if (password.equals(storedParentPassword)) {
                        isParent = true;
                        creatUser(userData, storedParentPassword, storedChildPassword);
                        homepage();
                    } else if (password.equals(storedChildPassword)) {
                        isParent = false;
                        creatUser(userData, storedParentPassword, storedChildPassword);
                        homepage();
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid Password");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            else {
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
