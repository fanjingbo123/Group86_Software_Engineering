### Virtual Bank Application for Kids

Welcome to the Virtual Bank Application for Kids! This project aims to create a fun and educational platform for children to learn about the value of money and banking concepts. Using Agile methodologies, our team has developed an initial release that includes several key features designed to engage children in financial activities, encourage responsible saving and spending, and provide a tool for parents to support their children's financial education.

#### Key Features

- **Account Creation**:
  - Allows the creation of virtual bank accounts, including both current accounts and savings accounts. This feature enables kids to have separate accounts for spending and saving.
- **Balance Tracking**:
  - Displays the current balance of both the current and savings accounts, helping kids to keep track of their funds in real-time.
- **Deposit**:
  - Enables kids to deposit virtual money that they have earned from completing tasks or activities. This function supports financial transactions that simulate real banking experiences.
- **Withdrawal**:
  - Allows kids to withdraw virtual money from their accounts, simulating the process of accessing funds for spending.
- **Task Setting**:
  - Provides parents with the ability to set tasks or activities (e.g., house chores, exercises) that give kids opportunities to earn money. This feature fosters a sense of responsibility and work-reward balance.
- **Transactions**:
  - Kids and parents can check the transaction history, providing a detailed record of all deposits, withdrawals, and task-related earnings. This feature helps in understanding spending habits and financial tracking.
- **Savings Goals**:
  - Enables kids to set savings goals and track their progress towards these goals. This function teaches the importance of saving and helps kids to learn how to plan for future expenses.
- **Parental Controls**:
  - Includes features that allow parents to monitor and manage their children's accounts, ensuring a safe and educational environment.
- **Educational Content**:
  - Integrates tips and lessons about financial literacy, helping kids to understand complex financial concepts in an age-appropriate manner.

These features are designed to make the Virtual Bank Application for Kids an engaging and educational tool, providing both fun and practical learning experiences about money management. As the project evolves, additional functionalities and enhancements will be implemented based on user feedback and further iterations.

Our goal is to create a robust, user-friendly application that not only entertains but also educates children on the importance of financial responsibility.



### Running Environment

This section provides details about the environment required to compile and run the Group86 Software Engineering project.

#### Java Development Kit (JDK)

- **Version**: 1.8 (Java 8)
- **Download**: Java SE Development Kit 8
- **Configuration**: Ensure that the `JAVA_HOME` environment variable is set to the JDK 1.8 installation path.

#### Apache Maven

- **Version**: 3.6.0 or later
- **Download**: Apache Maven
- **Configuration**: Ensure that the `MAVEN_HOME` environment variable is set to the Maven installation path and that the `PATH` includes `$MAVEN_HOME/bin`.

#### Integrated Development Environment (IDE)

- **Recommended IDE**: IntelliJ IDEA

- Versions

  :

  - **IntelliJ IDEA Community Edition**: Free and open-source edition.
  - **IntelliJ IDEA Ultimate Edition**: Paid edition with additional features.

- **Download**: IntelliJ IDEA

#### Git

- **Version**: Any recent version of Git.
- **Download**: Git
- **Usage**: For cloning the repository and version control.

#### Other dependencies

The project uses several key dependencies which are managed by Maven. These dependencies include:

- Fastjson
  - **Group ID**: `com.alibaba`
  - **Artifact ID**: `fastjson`
  - **Version**: `2.0.31`
- JUnit Jupiter Engine
  - **Group ID**: `org.junit.jupiter`
  - **Artifact ID**: `junit-jupiter-engine`
  - **Version**: `5.8.1`
  - **Scope**: `test`
- JUnit Jupiter
  - **Group ID**: `org.junit.jupiter`
  - **Artifact ID**: `junit-jupiter`
  - **Version**: `RELEASE`
  - **Scope**: `test`
- Mockito
  - **Group ID**: `org.mockito`
  - **Artifact ID**: `mockito-core`
  - **Version**: `3.3.3`
  - **Scope**: `test`

### Running the Virtual Bank Application for Kids

You can choose between running the application using the terminal or using IntelliJ IDEA.

#### Method 1: Running from the Terminal

1. **Clone the Repository**: Open your terminal and clone the project repository using the following command:

   ```sh
   git clone https://github.com/fanjingbo123/Group86_Software_Engineering.git
   ```

2. **Navigate to the Project Directory**: Change your directory to the project directory:

   ```sh
   cd Group86_Software_Engineering
   ```

3. **Run the Application**: Navigate to the directory containing the `Group86_software_engineering.jar` file, and run the application using the following command:

   ```sh
   java -jar Group86_software_engineering.jar
   ```

#### Method 2: Running with IntelliJ IDEA

1. **Clone the Repository**: Open your terminal and clone the project repository using the following command:

   ```sh
   git clone https://github.com/fanjingbo123/Group86_Software_Engineering.git
   ```

2. **Open IntelliJ IDEA**: Launch IntelliJ IDEA on your system.

3. **Import the Project**:

   - Click on `File` -> `New` -> `Project from Existing Sources...`.
   - Navigate to the directory where you cloned the repository and select the `Group86_Software_Engineering` directory.
   - Click `OK`.

4. **Import Project from Maven**:

   - IntelliJ IDEA will detect the `pom.xml` file and prompt you to import the project from Maven.
   - Select `Import project from external model` and choose `Maven`.
   - Click `Next` and follow the prompts to complete the import process.

5. **Configure JDK**:

   - Go to `File` -> `Project Structure` -> `Project`.
   - Ensure that the `Project SDK` is set to the correct version of the JDK you have installed (e.g., Java 8).

6. **Load Dependencies**:

   - IntelliJ IDEA will automatically detect and load the necessary dependencies. If prompted, use IntelliJ's auto-import or quickfix features to resolve any missing dependencies.

7. **Run the Main Class**:

   - Locate the `MainBoard.java` file in the `src/main/java/GUI` directory.
   - Right-click on `MainBoard.java` and select `Run 'MainBoard.main()'`.

By following these steps, you should be able to run the Virtual Bank Application for Kids on your computer either from the terminal or using IntelliJ IDEA.



### Dependencies

This project uses several key dependencies to function correctly. These dependencies are managed using Maven and are specified in the `pom.xml` file.

- **Fastjson (com.alibaba:fastjson:2.0.31)**

  - Fastjson is a Java library that can be used to convert Java Objects into their JSON representation and vice versa. It is known for its fast performance and ease of use.
  - **Usage**: Parsing and serializing JSON data for user information, tasks, and transactions.

- **JUnit Jupiter Engine (org.junit.jupiter:junit-jupiter-engine:5.8.1)**

  - JUnit is a widely used testing framework for Java applications. JUnit Jupiter is the new programming model for JUnit 5.
  - **Usage**: Running unit tests to ensure the correctness of the application.

- **JUnit Jupiter (org.junit.jupiter:junit-jupiter)**

  

  - Provides support for writing tests and extensions in JUnit 5.
  - **Usage**: Writing and executing tests.

- **Mockito (org.mockito:mockito-core:3.3.3)**

  - Mockito is a mocking framework that allows you to create and configure mock objects for testing purposes.
  - **Usage**: Creating mock objects for unit testing to isolate the functionality of the code being tested.

#### Maven Plugin

- Maven Compiler Plugin (org.apache.maven.plugins

  )

  - The compiler plugin is used to compile the source code of the project.

  - Configuration:

    ```
    <configuration>
        <source>8</source>
        <target>8</target>
    </configuration>
    ```

  - **Usage**: Ensuring the project is compiled with Java 8.

### Maven Configuration

The dependencies and plugins are specified in the `pom.xml` file as follows:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>org.se</groupId>
  <artifactId>Group86_software_engineering</artifactId>
  <version>1.0-SNAPSHOT</version>
  <name>Archetype - Group86_software_engineering</name>
  <url>http://maven.apache.org</url>
  <dependencies>
    <dependency>
      <groupId>com.alibaba</groupId>
      <artifactId>fastjson</artifactId>
      <version>2.0.31</version>
    </dependency>
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-engine</artifactId>
      <version>5.8.1</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter</artifactId>
      <version>RELEASE</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.mockito</groupId>
      <artifactId>mockito-core</artifactId>
      <version>3.3.3</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <configuration>
          <source>8</source>
          <target>8</target>
        </configuration>
      </plugin>
    </plugins>
  </build>
</project>
```





### Software Structure

The software structure of the Group86 Software Engineering project is organized into several packages, each containing specific classes that serve distinct functionalities. Below is a detailed overview of each package and the corresponding classes:

#### 1. GUI Package

The GUI package contains classes responsible for the graphical user interface of the application. It includes various dialogs, buttons, and the main application board.

- **MainBoard.java**
  - This is the main class that extends `JFrame` and provides the primary user interface for login and dashboard.
  - Key Variables and Methods:
    - `usernameField`, `passwordField`: Input fields for user credentials.
    - `signInButton`, `signUpButton`: Buttons for login and sign-up actions.
    - `signIn()`: Authenticates the user based on the provided credentials.
    - `signUpPanel()`: Switches the UI to the sign-up panel.
    - `homepage()`: Loads the user dashboard after successful login.
    - `logTransaction()`: Logs financial transactions.
    - `saveUserToFile()`: Saves user data to a file.
- **TaskButton.java**
  - A specialized `JButton` that carries an additional task identifier.
  - Key Variables:
    - `task_id`: The identifier for the task associated with this button.
- **TaskInputDialog.java**
  - A dialog window for adding new tasks.
  - Key Variables and Methods:
    - `taskContentField`, `taskLevelComboBox`, `rewardField`: Fields for task details.
    - `addTaskToList()`: Adds a new task to the user's task list and saves it to a file.
    - `populateDateComboBoxes()`, `updateDayComboBox()`: Methods for date selection.
- **WithdrawDialog.java**
  - A dialog window for withdrawing funds from user accounts.
  - Key Variables and Methods:
    - `currentAccountButton`, `savingAccountButton`: Radio buttons for account selection.
    - `amountField`: Input field for the withdrawal amount.
    - `performWithdraw()`: Executes the withdrawal and updates the user's account balance.
    - `recordTransaction()`: Records the transaction details.

#### 2. UserData Package

This package contains user-specific data files organized by user directories.

- Example User Data Files
  - These JSON files store user data, tasks, and transaction records.
  - Key Files:
    - `1.json`, `1_task.json`, `1_transaction.json`: Example user data files.

#### 3. Utils Package

The utils package contains utility classes used throughout the application for various functionalities.

- **User.java**
  - Represents a user in the virtual bank application.
  - Key Variables and Methods:
    - `user_name`, `parent_password`, `child_password`: User credentials.
    - `saving`, `current`, `credit_level`, `total_reward`: Financial attributes.
    - `getters` and `setters`: Methods for accessing and modifying user attributes.
- **Task.java**
  - Represents a task with specific attributes.
  - Key Variables and Methods:
    - `task_id`, `task_content`, `credit_level`, `reward`, `DDL`, `flag`: Task details.
    - `getters` and `setters`: Methods for accessing and modifying task attributes.

#### 4. Test Package

This package contains unit tests for various components of the application to ensure functionality and reliability.

- **DepositDialogTest.java**
  - Tests for the `DepositDialog` class, ensuring correct handling of deposit actions.
  - Key Tests:
    - `testDepositFailureInvalidAmount()`:Verifies that invalid deposit amounts are handled correctly.
- **EditPasswordDialogTest.java**
  - Tests for the `EditPasswordDialog` class, ensuring correct password update functionality.
  - Key Tests:
    - `testEditPasswordSuccess()`: Verifies successful password change.
    - `testEditPasswordFailureIncorrectOriginalPassword()`:Verifies handling of incorrect original passwords.
- **SignInTest.java**
  - Tests for the sign-in functionality in the `MainBoard` class.
  - Key Tests:
    - `testSignInSuccessAsParent()`: Verifies successful login as a parent.
    - `testSignInSuccessAsChild()`: Verifies successful login as a child.
    - `testSignInFailureInvalidPassword()`: Verifies handling of incorrect passwords.
    - `testSignInFailureUserNotFound()`: Verifies handling of non-existent users.

#### 5. Resources

This directory contains additional resources such as images and configuration files.

- **Images**
  - `bank.png`: An image used in the main application interface.
- **Configuration Files**
  - `pom.xml`: Maven configuration file that defines project dependencies and build configuration.