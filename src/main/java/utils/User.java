package utils;

/**
 * Represents a user in the virtual bank application, holding details relevant to both parent and child users,
 * such as account balances, passwords, and rewards.
 */
public class User {
    private String user_name; // The unique identifier for the user
    private String parent_password; // The password for parental access controls
    private String child_password; // The password for child access controls
    private double saving; // The amount of money in the user's saving account
    private double current; // The amount of money in the user's current account
    private int credit_level; // A numeric representation of the user's creditworthiness or achievements
    private double total_reward; // The total monetary rewards accumulated by the user
    private String goal_content; // A description of the user's financial goal
    private double goal_value; // The numerical value goal for savings or spending
    private boolean hasReceived200Bonus; // Indicates whether the user has received a specific $200 bonus
    private boolean hasReceived500Bonus; // Indicates whether the user has received a specific $500 bonus

    /**
     * Constructs a new User with specified username and passwords for parent and child access.
     *
     * @param user_name         the username of the user
     * @param parent_password   the password for the parent's access to the account
     * @param child_password    the password for the child's access to the account
     */
    public User(String user_name, String parent_password, String child_password) {
        this.user_name = user_name;
        this.parent_password = parent_password;
        this.child_password = child_password;
    }

    /**
     * Returns the username of the user.
     *
     * @return the username of the user
     */
    public String getUser_name() {
        return user_name;
    }

    /**
     * Sets the username of the user.
     *
     * @param user_name the new username of the user
     */
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    /**
     * Returns the parent's password for account access.
     *
     * @return the parent's password
     */
    public String getParent_password() {
        return parent_password;
    }

    /**
     * Sets the parent's password for account access.
     *
     * @param parent_password the new parent's password
     */
    public void setParent_password(String parent_password) {
        this.parent_password = parent_password;
    }

    /**
     * Returns the child's password for account access.
     *
     * @return the child's password
     */
    public String getChild_password() {
        return child_password;
    }

    /**
     * Sets the child's password for account access.
     *
     * @param child_password the new child's password
     */
    public void setChild_password(String child_password) {
        this.child_password = child_password;
    }

    /**
     * Returns the current balance of the user's current account.
     *
     * @return the current account balance
     */
    public double getCurrent() {
        return current;
    }

    /**
     * Sets the current balance of the user's current account.
     *
     * @param current the new balance of the current account
     */
    public void setCurrent(double current) {
        this.current = current;
    }

    /**
     * Returns the balance of the user's saving account.
     *
     * @return the saving account balance
     */
    public double getSaving() {
        return saving;
    }

    /**
     * Sets the balance of the user's saving account.
     *
     * @param saving the new balance of the saving account
     */
    public void setSaving(double saving) {
        this.saving = saving;
    }

    /**
     * Returns the user's credit level.
     *
     * @return the credit level
     */
    public int getCredit_level() {
        return credit_level;
    }

    /**
     * Sets the user's credit level.
     *
     * @param credit_level the new credit level
     */
    public void setCredit_level(int credit_level) {
        this.credit_level = credit_level;
    }

    /**
     * Returns the total rewards accumulated by the user.
     *
     * @return the total rewards
     */
    public double getTotal_reward() {
        return total_reward;
    }

    /**
     * Sets the total rewards accumulated by the user.
     *
     * @param total_reward the new total rewards
     */
    public void setTotal_reward(double total_reward) {
        this.total_reward = total_reward;
    }

    /**
     * Returns the content of the user's financial goal.
     *
     * @return the goal content
     */
    public String getGoal_content() {
        return goal_content;
    }

    /**
     * Sets the content of the user's financial goal.
     *
     * @param goal_content the new goal content
     */
    public void setGoal_content(String goal_content) {
        this.goal_content = goal_content;
    }

    /**
     * Returns the value of the user's financial goal.
     *
     * @return the goal value
     */
    public double getGoal_value() {
        return goal_value;
    }

    /**
     * Sets the value of the user's financial goal.
     *
     * @param goal_value the new goal value
     */
    public void setGoal_value(double goal_value) {
        this.goal_value = goal_value;
    }

    /**
     * Checks if the user has received the $200 bonus.
     *
     * @return true if the $200 bonus has been received, otherwise false
     */
    public boolean hasReceived200Bonus() {
        return hasReceived200Bonus;
    }

    /**
     * Sets whether the user has received the $200 bonus.
     *
     * @param received the status of the $200 bonus
     */
    public void setReceived200Bonus(boolean received) {
        this.hasReceived200Bonus = received;
    }

    /**
     * Checks if the user has received the $500 bonus.
     *
     * @return true if the $500 bonus has been received, otherwise false
     */
    public boolean hasReceived500Bonus() {
        return hasReceived500Bonus;
    }

    /**
     * Sets whether the user has received the $500 bonus.
     *
     * @param received the status of the $500 bonus
     */
    public void setReceived500Bonus(boolean received) {
        this.hasReceived500Bonus = received;
    }
}
