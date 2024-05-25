package utils;

import java.util.Date;

/**
 * Represents a task with specific attributes including content, credit level, reward, deadline, and a completion flag.
 * This class is used to model tasks within an application that tracks task performance, rewards, and deadlines.
 */
public class Task {

    private String task_id; // Unique identifier for the task
    private String task_content; // Description or content of the task
    private int credit_level; // Numerical level indicating the difficulty or importance of the task
    private double reward; // Reward amount for completing the task
    private Date DDL; // Deadline for completing the task
    private boolean flag; // Boolean flag to indicate whether the task is completed or not

    /**
     * Constructs a new Task with specified details.
     *
     * @param task_id The unique identifier for the task.
     * @param task_content The content or description of the task.
     * @param credit_level The difficulty or importance level of the task.
     * @param reward The monetary or point reward for completing the task.
     * @param DDL The deadline by which the task needs to be completed.
     * @param flag The completion status of the task (true if completed).
     */
    public Task(String task_id, String task_content, int credit_level, double reward, Date DDL, boolean flag) {
        this.task_id = task_id;
        this.task_content = task_content;
        this.credit_level = credit_level;
        this.reward = reward;
        this.DDL = DDL;
        this.flag = flag;
    }

    /**
     * Gets the task identifier.
     *
     * @return The unique identifier for the task.
     */
    public String getTask_id() {
        return task_id;
    }

    /**
     * Sets the task identifier.
     *
     * @param task_id The unique identifier for the task.
     */
    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    /**
     * Gets the task content.
     *
     * @return The content or description of the task.
     */
    public String getTask_content() {
        return task_content;
    }

    /**
     * Sets the task content.
     *
     * @param task_content The content or description of the task.
     */
    public void setTask_content(String task_content) {
        this.task_content = task_content;
    }

    /**
     * Gets the credit level of the task.
     *
     * @return The difficulty or importance level of the task.
     */
    public int getCredit_level() {
        return credit_level;
    }

    /**
     * Sets the credit level of the task.
     *
     * @param credit_level The difficulty or importance level of the task.
     */
    public void setCredit_level(int credit_level) {
        this.credit_level = credit_level;
    }

    /**
     * Gets the reward for completing the task.
     *
     * @return The monetary or point reward for the task.
     */
    public double getReward() {
        return reward;
    }

    /**
     * Sets the reward for completing the task.
     *
     * @param reward The monetary or point reward for the task.
     */
    public void setReward(double reward) {
        this.reward = reward;
    }

    /**
     * Gets the deadline for completing the task.
     *
     * @return The deadline as a Date object.
     */
    public Date getDDL() {
        return DDL;
    }

    /**
     * Sets the deadline for completing the task.
     *
     * @param DDL The deadline as a Date object.
     */
    public void setDDL(Date DDL) {
        this.DDL = DDL;
    }

    /**
     * Returns whether the task is completed.
     *
     * @return true if the task is completed, otherwise false.
     */
    public boolean isFlag() {
        return flag;
    }

    /**
     * Sets the completion flag of the task.
     *
     * @param flag true if the task is completed, otherwise false.
     */
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
