package utils;
import java.util.Date;
public class Task {

    private String task_id;
    private String task_content;
    private int credit_level;
    private double reward;
    private Date DDL;
    private boolean flag = false;

    public Task(String task_id, String task_content, int credit_level, double reward, Date DDL, boolean flag) {
        this.task_id = task_id;
        this.task_content = task_content;
        this.credit_level = credit_level;
        this.reward = reward;
        this.DDL = DDL;
        this.flag = flag;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getTask_content() {
        return task_content;
    }

    public void setTask_content(String task_content) {
        this.task_content = task_content;
    }

    public int getCredit_level() {
        return credit_level;
    }

    public void setCredit_level(int credit_level) {
        this.credit_level = credit_level;
    }

    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public Date getDDL() {
        return DDL;
    }

    public void setDDL(Date DDL) {
        this.DDL = DDL;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
