package pl.edu.agh.calculationp2p.message.utils;

import pl.edu.agh.calculationp2p.state.task.TaskState;

public class TaskStateMess {

    private int taskId;
    private TaskState state;
    private Integer owner;

    public TaskStateMess(int taskId, TaskState state, Integer owner){
        this.taskId = taskId;
        this.state = state;
        this.owner = owner;
    }

    public String serialize(){
        return "{\"task_id\":"+this.taskId+",\"state\":"+this.state+",\"owner\":"+this.owner+",\"result\":\"null\"}";
    }

}
