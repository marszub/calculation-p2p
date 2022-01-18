package pl.edu.agh.calculationp2p.message.utils;

import pl.edu.agh.calculationp2p.state.task.TaskState;

public record TaskStateMess(int taskId, TaskState state, Integer owner) {

    public String serialize() {
        return "{\"task_id\":" + this.taskId + ",\"state\":\"" + this.state + "\",\"owner\":" + this.owner + ",\"result\":\"null\"}";
    }

}
