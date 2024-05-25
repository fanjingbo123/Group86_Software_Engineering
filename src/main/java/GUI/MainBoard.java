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
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

/**
 * MainBoard serves as the primary JFrame for the Virtual Bank application, managing user interactions for login,
 * registration, task management, and financial transactions.
 */
public class MainBoard extends JFrame {
    public JTextField usernameField;
    public JPasswordField passwordField;
    private JButton signInButton;
    private JButton signUpButton;

    public User currentUser;
    public boolean isParent = false;

    /**
     * Constructs the main login window of the Virtual Bank application.
     * This constructor initializes the frame, sets up the layout, and adds all necessary components such as
     * title labels, image labels, and input fields for username and password.
     */
    public MainBoard() {
        // Frame initialization
        super("Login");
        setSize(550, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);
        setResizable(false);
        // Top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Bank", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        // Image panel
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.Y_AXIS)); // Use BoxLayout
        imagePanel.add(Box.createVerticalGlue()); // Add vertical filler

        ImageIcon icon = new ImageIcon(getClass().getResource("/image/bank.png"));
        JLabel imageLabel = new JLabel(icon);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Set image horizontally centered
        imagePanel.add(imageLabel);

        imagePanel.add(Box.createVerticalGlue()); // Add vertical filler to ensure image is vertically centered

        // Inputs panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        JPanel usernamePanel = new JPanel();
        usernamePanel.setLayout(new BoxLayout(usernamePanel, BoxLayout.X_AXIS));
        usernamePanel.add(new JLabel("Username:"));
        usernameField = new JTextField(15);
        Dimension textFieldSize = new Dimension(200, 30);
        usernameField.setPreferredSize(textFieldSize);
        usernameField.setMaximumSize(textFieldSize);
        usernameField.setMinimumSize(textFieldSize);
        usernamePanel.add(usernameField);

        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.X_AXIS));
        passwordPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField(15);
        passwordField.setPreferredSize(textFieldSize);
        passwordField.setMaximumSize(textFieldSize);
        passwordField.setMinimumSize(textFieldSize);
        passwordPanel.add(passwordField);

        // Add panels to the main panel
        inputPanel.add(Box.createVerticalStrut(20)); // Add gap
        inputPanel.add(usernamePanel);
        inputPanel.add(Box.createVerticalStrut(10)); // Add gap
        inputPanel.add(passwordPanel);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        signUpButton = new JButton("Sign Up");
        signInButton = new JButton("Log In");
        signUpButton.setPreferredSize(new Dimension(100, 25));
        signInButton.setPreferredSize(new Dimension(100, 25));

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

        buttonPanel.add(signUpButton);
        buttonPanel.add(signInButton);
        inputPanel.add(Box.createVerticalStrut(30)); // Add gap
        inputPanel.add(buttonPanel);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(imagePanel, BorderLayout.WEST);
        mainPanel.add(inputPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        // Display the window
        setVisible(true);
    }

    /**
     * Opens the sign-up panel allowing new users to register.
     */
    private void signUpPanel() {
        setTitle("Sign Up");
        getContentPane().removeAll();
        revalidate();
        setSize(550, 300);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 10, 5); // Set component spacing

        JLabel usernameLabel = new JLabel("Username:");
        JTextField signUpUsernameField = new JTextField(15);
        JLabel parentPasswordLabel = new JLabel("Parent Password:");
        JPasswordField parentSignUpPasswordField = new JPasswordField(15);
        JLabel confirmParentPassLabel = new JLabel("Confirm Parent Password:");
        JPasswordField confirmParentPassField = new JPasswordField(15);
        JLabel childPasswordLabel = new JLabel("Child Password:");
        JPasswordField childSignUpPasswordField = new JPasswordField(15);
        JLabel confirmChildPassLabel = new JLabel("Confirm Child Password:");
        JPasswordField confirmChildPassField = new JPasswordField(15);

        JButton confirmSignUpButton = new JButton("Sign Up");
        JButton cancelSignUpButton = new JButton("Cancel");

        // Add components to the panel using GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(signUpUsernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(parentPasswordLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(parentSignUpPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(confirmParentPassLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(confirmParentPassField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(childPasswordLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(childSignUpPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(confirmChildPassLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(confirmChildPassField, gbc);

        // Add buttons
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(confirmSignUpButton);
        buttonPanel.add(cancelSignUpButton);
        panel.add(buttonPanel, gbc);

        // Add action listeners
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
                } else if (!child_password.equals(confirmChildPass)) {
                    JOptionPane.showMessageDialog(MainBoard.this, "Child Passwords do not match, please try again.");
                    refresh();
                } else {
                    currentUser = new User(username, HashGenerator.generateSHA256(parent_password), HashGenerator.generateSHA256(child_password));
                    saveUserToFile();
                    homepage();
                }
            }

            public void refresh() {
                signUpUsernameField.setText("");
                parentSignUpPasswordField.setText("");
                confirmParentPassField.setText("");
                childSignUpPasswordField.setText("");
                confirmChildPassField.setText("");
            }
        });

        add(panel);
        revalidate();
        repaint();
        setVisible(true);
    }

    /**
     * Reverts the display back to the login screen.
     */
    private void backToLogin() {
        setTitle("Login");
        getContentPane().removeAll();
        revalidate();
        addComponentsToLoginPanel();
        setVisible(true);
    }

    /**
     * Saves the current user's data to a file.
     */
    private void saveUserToFile() {
        String username = currentUser.getUser_name();
        String jsonString = JSON.toJSONString(currentUser, String.valueOf(SerializerFeature.PrettyFormat));

        // Create directory named by username
        String packagePath = "src/main/java/UserData/" + username;
        File packageDirectory = new File(packagePath);
        if (!packageDirectory.exists()) {
            packageDirectory.mkdirs(); // Create directory and its parent directories
        }

        // Create JSON file named by username
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

    /**
     * Displays the home page after a user has logged in successfully.
     */
    public void homepage() {
        setTitle("User Dashboard");
        getContentPane().removeAll();
        revalidate();
        setSize(1200, 700);

        JPanel panel = new JPanel(new BorderLayout());

        // Top Panel
        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        JLabel usernameLabel = new JLabel("Username: " + currentUser.getUser_name());
        JLabel current = new JLabel("Current: " + currentUser.getCurrent());
        JLabel saving = new JLabel("Saving: " + currentUser.getSaving());
        JLabel credit = new JLabel("Credit_level: " + currentUser.getCredit_level());
        JLabel totalRewards = new JLabel("Total_rewards: " + currentUser.getTotal_reward());
        JLabel goals = new JLabel("Goals: " + currentUser.getGoal_content() + " " + currentUser.getGoal_value());
        JButton logoutButton = new JButton("Logout");
        JButton editButton = new JButton("Edit");
        JPanel ButtonPanel1 = new JPanel(new GridLayout(1, 1));
        JPanel ButtonPanel2 = new JPanel(new GridLayout(1, 2));
        ButtonPanel1.add(logoutButton);
        ButtonPanel2.add(logoutButton);
        ButtonPanel2.add(editButton);
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

        // Create an empty border for spacing
        Border emptyBorder = BorderFactory.createEmptyBorder(10, 0, 10, 0); // 上下间距为10

        // Add borders to panels for spacing
        topPanel.setBorder(emptyBorder);

        panel.add(topPanel, BorderLayout.NORTH);
        
        // Middle Panel
        String[] columnNames = {"Task Content", "Credit Level", "Rewards", "DDL", "Finish", "Delete"};

        Object[][] rowData = loadTasks();

        ArrayList<Task> taskArrayList = getTasks(rowData);

        Object[][] filteredData = new Object[taskArrayList.size()][8];
        for (int i = 0; i < taskArrayList.size(); i++) {
            filteredData[i][0] = taskArrayList.get(i).getTask_content();
            filteredData[i][1] = taskArrayList.get(i).getCredit_level();
            filteredData[i][2] = taskArrayList.get(i).getReward();
            filteredData[i][3] = taskArrayList.get(i).getDDL();
            filteredData[i][4] = taskArrayList.get(i).getTask_id();
            filteredData[i][5] = taskArrayList.get(i).isFlag();
        }

        JTable taskTable = new JTable(filteredData, columnNames);
        taskTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        taskTable.setRowHeight(30);

        // Create a renderer to center the text
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // Apply the renderer to each column
        for (int i = 0; i < taskTable.getColumnCount(); i++) {
            taskTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        taskTable.getColumn("Finish").setCellRenderer(new ButtonRenderer("Finish"));
        taskTable.getColumn("Delete").setCellRenderer(new ButtonRenderer("Delete"));

        // Set ButtonEditor for "Finish" and "Delete" columns
        taskTable.getColumn("Finish").setCellEditor(new ButtonEditor("Finish"));
        taskTable.getColumn("Delete").setCellEditor(new ButtonEditor("Delete"));

        ((ButtonEditor) taskTable.getColumn("Finish").getCellEditor()).setActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = taskTable.getSelectedRow();
                String task_id = (String) filteredData[row][4];

                if(currentUser.getCredit_level() >= (int)filteredData[row][1]){
                    double reward = updateTask(task_id);  // Assuming updateTask marks the task complete and returns the reward

                    // Update user's total rewards and current balance
                    currentUser.setTotal_reward(currentUser.getTotal_reward() + reward);
                    currentUser.setCurrent(currentUser.getCurrent() + reward);

                    int newCreditLevel = currentUser.getCredit_level() + (int)(currentUser.getTotal_reward() / 50) - (int)((currentUser.getTotal_reward() - reward) / 50);
                    if (newCreditLevel != currentUser.getCredit_level()) {
                        currentUser.setCredit_level(newCreditLevel);  // Update credit_level
                        JOptionPane.showMessageDialog(null, "Congratulations! Your credit level has been increased to " + newCreditLevel);
                    }
                    try {
                        logTransaction((double)filteredData[row][2], false, "current", null);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    // Update user data file or database entry
                    updateUserFile();
                    // Refresh the homepage to show updated data
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

    /**
     * Retrieves a list of tasks for the current user.
     *
     * @return an ArrayList of Task objects.
     */
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

    /**
     * Displays the transaction record page, which shows a table of all transactions related to the current user.
     * This page includes buttons for navigating back to the homepage, and for initiating withdrawal and deposit actions if the user has parental access.
     *
     * The transaction record includes details such as the time of the transaction, whether it was an expense or an income,
     * and the type of account affected by the transaction.
     */
    public void transactionRecordPage() {
        setTitle("Transaction Record");
        getContentPane().removeAll();
        revalidate();
        setSize(600, 400); // Set page size

        // Main layout panel
        JPanel panel = new JPanel(new BorderLayout());

        // Top panel for displaying title and account information
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        // Title label
        JLabel titleLabel = new JLabel("Transaction Record", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(titleLabel);

        // Account information labels
        JLabel current = new JLabel("Current: " + currentUser.getCurrent(), SwingConstants.CENTER);
        JLabel saving = new JLabel("Saving: " + currentUser.getSaving(), SwingConstants.CENTER);
        topPanel.add(current);
        topPanel.add(saving);

        // Add top panel to the main panel's north region
        panel.add(topPanel, BorderLayout.NORTH);

        // Create column names for the table
        String[] columnNames = {"Time", "Expense/Income", "Account Type"};

        // Read transaction data
        Object[][] data = loadTransactionData();

        // Create the table
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Bottom buttons
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
                homepage();
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


        panel.add(bottomPanel, BorderLayout.SOUTH);


        add(panel);
        setVisible(true);
    }

    /**
     * Refreshes the transaction record page to show updated information.
     */
    public void refreshTransactionRecordPage() {
        transactionRecordPage();
    }

    /**
     * Logs a transaction for the current user.
     *
     * @param amount       The amount of the transaction.
     * @param isExpense    Indicates whether the transaction is an expense.
     * @param accountType  The type of account affected by the transaction.
     * @param time         The time the transaction occurred.
     * @throws IOException If an I/O error occurs when writing to the file.
     */
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

    /**
     * Updates the transaction JSON file with a new transaction.
     *
     * @param transactionDetails Details of the transaction to log.
     * @throws IOException If an I/O error occurs when writing to the file.
     */
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
            transactions = new JSONArray(); // Ensure transactions is not null
        }

        transactions.add(transactionDetails);

        try (FileWriter fileWriter = new FileWriter(transactionFile)) {
            fileWriter.write(transactions.toJSONString());
        }
    }

    /**
     * Loads transaction data from the file.
     *
     * @return a 2D Object array containing the transaction data.
     */
    private Object[][] loadTransactionData() {
        String username = currentUser.getUser_name();
        String transactionFileName = "src/main/java/UserData/" + username + "/" + username + "_transaction.json";
        File transactionFile = new File(transactionFileName);

        if (!transactionFile.exists()) {
            return new Object[0][0]; // Return empty array if no transactions
        }

        try {

            String jsonText = new String(Files.readAllBytes(transactionFile.toPath()), StandardCharsets.UTF_8);
            JSONArray transactions = JSON.parseArray(jsonText);

            if (transactions == null) {
                transactions = new JSONArray(); // Ensure transactions is not null
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

    /**
     * Saves the current user's data to a file.
     * Serializes the user object to JSON and writes it to the user's data file, creating a new file if necessary.
     */
    public void updateUserFile() {
        String username = currentUser.getUser_name();
        String jsonString = JSON.toJSONString(currentUser, String.valueOf(SerializerFeature.PrettyFormat));

        // Create directory named by username
        String packagePath = "src/main/java/UserData/" + username;
        File packageDirectory = new File(packagePath);
        if (!packageDirectory.exists()) {
            packageDirectory.mkdirs();
        }

        // Create JSON file named by username
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

    /**
     * Update task information for the user, typically called after a task is completed or updated.
     *
     * @param taskId The ID of the task to update.
     * @return the reward amount associated with the task.
     */
    public double updateTask(String taskId) {

        String username = currentUser.getUser_name();
        String taskFileName = "src/main/java/UserData/" + username + "/" + username + "_task.json";

        Object[][] taskData = loadTasks();

        // Iterate task array to find the targeted task object
        for (int i = 0; i < taskData.length; i += 1) {
            String id = (String) taskData[i][6];
            if (taskId.equals(id)) {
                taskData[i][7] = (Object) true;

                JSONArray taskArray = new JSONArray();
                for (Object[] taskDatum : taskData) {
                    // Create new JSON object for new task
                    com.alibaba.fastjson.JSONObject newTaskJson = new com.alibaba.fastjson.JSONObject();
                    newTaskJson.put("task_id", taskDatum[6]);
                    newTaskJson.put("task_content", taskDatum[0]);
                    newTaskJson.put("credit_level", taskDatum[1]);
                    newTaskJson.put("reward", taskDatum[2]);
                    newTaskJson.put("DDL", taskDatum[3]);
                    newTaskJson.put("flag", taskDatum[7]);


                    taskArray.add(newTaskJson);
                }
                // Write updated JSON array back to task file
                try (FileWriter fileWriter = new FileWriter(taskFileName)) {
                    fileWriter.write(taskArray.toJSONString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                return (double)taskData[i][2];
            }
        }


        return 0;
    }

    /**
     * Loads the user's tasks from a file.
     * Parses the task data file and constructs a data model for managing tasks within the application.
     *
     * @return a two-dimensional array of Objects containing task data suitable for display in a JTable
     */
    private Object[][] loadTasks() {
        String username = currentUser.getUser_name();
        String taskFilePath = "src/main/java/UserData/" + username + "/" + username + "_task.json";

        try {
            // Test whether file is null
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
            // If JSON is null
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

    /**
     * Initializes the current user's data from JSON data read from the user file.
     * This method sets the user details including username, account balances, goals, and password hashes.
     *
     * @param userData JSON object containing user data.
     * @param storedParentPassword The hashed password for the parent's login.
     * @param storedChildPassword The hashed password for the child's login.
     */
    private void creatUser(JSONObject userData, String storedParentPassword, String storedChildPassword){
        currentUser = new User(userData.getString("user_name"), storedParentPassword, storedChildPassword);
        currentUser.setCurrent(userData.getDoubleValue("current"));
        currentUser.setSaving(userData.getDoubleValue("saving"));
        currentUser.setTotal_reward(userData.getDoubleValue("total_reward"));
        currentUser.setGoal_content(userData.getString("goal_content"));
        currentUser.setGoal_value(userData.getDoubleValue("goal_value"));
        currentUser.setCredit_level(userData.getIntValue("credit_level"));
    }

    /**
     * Processes the user login, authenticating the username and password against stored data.
     * If authentication succeeds, it sets the current user and transitions to the user homepage.
     * Otherwise, it displays an error message.
     */
    public void signIn() {
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

    /**
     * Adds components related to user login to the main panel.
     * This includes setup for user input fields, buttons for signing up and logging in, and layout management.
     */
    private void addComponentsToLoginPanel() {
        setSize(550, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);

        // Top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Bank", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        // Image panel
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.Y_AXIS));
        imagePanel.add(Box.createVerticalGlue());

        ImageIcon icon = new ImageIcon(getClass().getResource("/image/bank.png"));
        JLabel imageLabel = new JLabel(icon);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imagePanel.add(imageLabel);

        imagePanel.add(Box.createVerticalGlue());

        // Inputs panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        JPanel usernamePanel = new JPanel();
        usernamePanel.setLayout(new BoxLayout(usernamePanel, BoxLayout.X_AXIS));
        usernamePanel.add(new JLabel("Username:"));
        usernameField = new JTextField(15);
        Dimension textFieldSize = new Dimension(200, 30);
        usernameField.setPreferredSize(textFieldSize);
        usernameField.setMaximumSize(textFieldSize);
        usernameField.setMinimumSize(textFieldSize);
        usernamePanel.add(usernameField);

        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.X_AXIS));
        passwordPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField(15);
        passwordField.setPreferredSize(textFieldSize);
        passwordField.setMaximumSize(textFieldSize);
        passwordField.setMinimumSize(textFieldSize);
        passwordPanel.add(passwordField);

        // Add panels to the main board
        inputPanel.add(Box.createVerticalStrut(20)); // Add gaps
        inputPanel.add(usernamePanel);
        inputPanel.add(Box.createVerticalStrut(10)); // Add gaps
        inputPanel.add(passwordPanel);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        signUpButton = new JButton("Sign Up");
        signInButton = new JButton("Log In");
        signUpButton.setPreferredSize(new Dimension(100, 25));
        signInButton.setPreferredSize(new Dimension(100, 25));

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

        buttonPanel.add(signUpButton);
        buttonPanel.add(signInButton);
        inputPanel.add(Box.createVerticalStrut(30)); // Add gaps
        inputPanel.add(buttonPanel);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(imagePanel, BorderLayout.WEST);
        mainPanel.add(inputPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Main method to run the application. Initializes the main GUI frame on the event dispatch thread.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainBoard();
            }
        });
    }
}
