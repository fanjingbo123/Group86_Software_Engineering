package utils;

public class User {
    private String user_name;
    private String parent_password;
    private String child_password;
    private double saving = 0;
    private double current = 0;
    private int credit_level = 1;
    private double total_reward = 0;
    private String goal_content = " ";
    private double goal_value = 0;

    public User(String user_name, String parent_password, String child_password) {
        this.user_name = user_name;
        this.parent_password = parent_password;
        this.child_password = child_password;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getParent_password() {
        return parent_password;
    }

    public void setParent_password(String parent_password) {
        this.parent_password = parent_password;
    }

    public String getChild_password() {
        return child_password;
    }

    public void setChild_password(String child_password) {
        this.child_password = child_password;
    }

    public double getCurrent() {
        return current;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    public double getSaving() {
        return saving;
    }

    public void setSaving(double saving) {
        this.saving = saving;
    }

    public int getCredit_level() {
        return credit_level;
    }

    public void setCredit_level(int credit_level) {
        this.credit_level = credit_level;
    }

    public double getTotal_reward() {
        return total_reward;
    }

    public void setTotal_reward(double total_reward) {
        this.total_reward = total_reward;
    }

    public String getGoal_content() {
        return goal_content;
    }

    public void setGoal_content(String goal_content) {
        this.goal_content = goal_content;
    }

    public double getGoal_value() {
        return goal_value;
    }

    public void setGoal_value(double goal_value) {
        this.goal_value = goal_value;
    }
}