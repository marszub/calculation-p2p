package pl.edu.agh.calculationp2p.message.body;

import pl.edu.agh.calculationp2p.calculation.TaskResult;
import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.message.MessageImpl;
import pl.edu.agh.calculationp2p.message.process.MessageProcessContext;
import pl.edu.agh.calculationp2p.network.router.Router;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.task.TaskState;

public class Calculated implements Body{
    public int getTaskId() {
        return taskId;
    }

    public TaskResult getTaskResult() {
        return taskResult;
    }

    private final int taskId;
    private final TaskResult taskResult;
    public Calculated(int taskId, TaskResult taskResult){
        this.taskId = taskId;
        this.taskResult = taskResult;
    }
    @Override
    public String serializeType() {
        return "\"calculated\"";
    }

    @Override
    public String serializeContent() {
        String result = "";
        result = result.concat("{\"task_id\":");
        result = result.concat(String.valueOf(this.taskId));
        result = result.concat(",\"result\":");
        result = taskResult==null?result.concat("null"):result.concat(taskResult.serialize());
        result = result.concat("}");
        return result;
    }

    @Override
    public void process(int sender, MessageProcessContext context) {
        Future<TaskRecord> calculateFuture = context.getStateUpdater().calculate(taskId, sender, taskResult);
        context.getFutureProcessor().addFutureProcess(calculateFuture, () -> {
            Router router = context.getRouter();
            router.send(new MessageImpl(router.getId(), sender, new Confirm(taskId, TaskState.Calculated, sender, calculateFuture.get())));
        });
    }

    @Override
    public Body clone() {
        //TODO: deep copy in taskResult
        return new Calculated(this.taskId, this.taskResult);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        Calculated message = (Calculated) o;
        //TODO: equals() in taskResult
        return message.getTaskId() == this.taskId;
        //&& message.getTaskResult() == this.taskResult;
    }



}
