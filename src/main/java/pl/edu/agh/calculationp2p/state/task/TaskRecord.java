package pl.edu.agh.calculationp2p.state.task;


import pl.edu.agh.calculationp2p.calculation.TaskResult;

public class TaskRecord {
    private final int taskID;
    private final TaskState state;
    private final int owner;
    private final TaskResult result;

    public TaskRecord(int task, TaskState state, int owner, TaskResult result) {
        this.taskID = task;
        this.state = state;
        this.owner = owner;
        this.result = result;
    }

    public TaskRecord(){
        this.taskID = -1;
        this.state = null;
        this.owner = -1;
        this.result = null;
    }

    public TaskRecord(TaskRecord taskRecord) {
        this.taskID = taskRecord.getTaskID();
        this.state = taskRecord.getState();
        this.owner = taskRecord.getOwner();
        this.result = taskRecord.getResult();
    }

    public String serialize() {
        return result.serialize();
    }

    public int getTaskID() {
        return taskID;
    }

    public TaskState getState() {
        return state;
    }

    public int getOwner() {
        return owner;
    }

    public TaskResult getResult() {
        return result;
    }

    public boolean hasHigherPriority(TaskRecord toCompare) {
        TaskState taskState = toCompare.getState();
        if(this.state.getValue() < taskState.getValue()){
            return true;
        }
       if(this.owner < toCompare.getOwner()){
           return true;
       }
       return false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o == this)
            return true;
        if (getClass() != o.getClass())
            return false;
        TaskRecord other = (TaskRecord) o;

        return this.getTaskID() == other.getTaskID() && this.getState() == other.getState()
                && this.getOwner() == other.getOwner() && this.getResult() == other.getResult();

    }
}
