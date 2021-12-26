package pl.edu.agh.calculationp2p.message.body;

import pl.edu.agh.calculationp2p.calculation.TaskResult;
import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.message.MessageImpl;
import pl.edu.agh.calculationp2p.message.process.MessageProcessContext;
import pl.edu.agh.calculationp2p.network.router.Router;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;

public class Calculated implements Body{
    private int taskId;
    private TaskResult taskResult;
    public Calculated(int taskId, TaskResult taskResult){
        this.taskId = taskId;
        this.taskResult = taskResult;
    }
    @Override
    public String serializeType() {
        throw new UnsupportedOperationException("Will be implemented");
    }

    @Override
    public String serializeContent() {
        return null;
    }

    @Override
    public void process(int sender, MessageProcessContext context) {
        Future<TaskRecord> f = context.getStateUpdater().calculate(taskId, sender, taskResult);
        context.getFutureProcessor().addFutureProcess(f, () -> {
            Router router = context.getRouter();
            router.send((Message) new MessageImpl(router.getId(), sender, new Confirm(f.get())));
        });
    }

}
