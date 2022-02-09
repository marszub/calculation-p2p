package pl.edu.agh.calculationp2p.message.body;

import io.vertx.core.net.impl.pool.Task;
import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.message.MessageImpl;
import pl.edu.agh.calculationp2p.message.process.MessageProcessContext;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GetSynchronization implements Body{

    private final List<Integer> taskIdList;

    public GetSynchronization(List<Integer> taskIdList) {
        this.taskIdList = taskIdList;
    }

    public List<Integer> getTaskIdList() {
        return taskIdList;
    }

    @Override
    public String serializeType() {
        return "\"get_synchronization\"";
    }

    @Override
    public String serializeContent() {
        String result = "";
        result = result.concat("{\"tasks\":[");
        for(int i=0;i<this.taskIdList.size();i++){
            result = result.concat("{\"task_id\":"+this.taskIdList.get(i)+"}");
            if(i<this.taskIdList.size()-1){
                result = result.concat(",");
            }
        }
        result = result.concat("]}");
        return result;
    }

    @Override
    public void process(int sender, MessageProcessContext context) {

        int myId = context.getRouter().getId();

        List<Future<TaskRecord>> syncList = taskIdList
                                                .stream()
                                                .map(taskId -> context.getStateInformer().getTaskProgress(taskId))
                                                .collect(Collectors.toList());

        context.getFutureProcessor().addFutureProcess(syncList.get(syncList.size()-1), createRunnable(syncList, context, myId, sender));
    }

    private Runnable createRunnable(List<Future<TaskRecord>> syncList, MessageProcessContext context, int myId, int sender)
    {
        return () -> {
            List<Future<TaskRecord>> list = syncList.stream()
                    .filter(sync -> !sync.isReady())
                    .collect(Collectors.toList());

            if(list.size() > 0) {
                context.getFutureProcessor().addFutureProcess(list.get(0), createRunnable(syncList, context, myId, sender));
                return;
            }

            Message messWithStateOfTasks = new MessageImpl(myId, sender, new GiveSynchronization(
                    syncList
                            .stream()
                            .map(Future::get)
                            .collect(Collectors.toList())
            ));
            context.getRouter().send(messWithStateOfTasks);
        };
    }

    @Override
    public Body clone() {
        return new GetSynchronization(new ArrayList<>(this.taskIdList));
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
        GetSynchronization message = (GetSynchronization) o;
        return message.getTaskIdList().equals(this.taskIdList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskIdList);
    }

}
