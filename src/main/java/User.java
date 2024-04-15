public class User {
    private String user_name;
    private String password;
    private double saving = 0;
    private double current = 0;
    private int credit_level = 1;
    private double total_reward = 0;
    private String goal_content = " ";
    private double goal_value = 0;

    public User(String user_name, String password) {
        this.user_name = user_name;
        this.password = password;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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