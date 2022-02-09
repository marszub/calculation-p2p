package pl.edu.agh.calculationp2p.state.request;

import pl.edu.agh.calculationp2p.state.Servant;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.task.TaskState;

import java.util.List;
import java.util.stream.Collectors;

public class ClearReservationRequest implements MethodRequest {
    Integer nodeID;

    public ClearReservationRequest(Integer nodeID) {
        this.nodeID = nodeID;
    }

    @Override
    public void call(Servant servant) {
        List<TaskRecord> filtered = servant.getProgress().getTasks()
                .stream()
                .filter( e ->
                    e.getState() == TaskState.Reserved && e.getOwner() == nodeID
                ).collect(Collectors.toList());

       filtered.forEach( record -> {
            TaskRecord updated = new TaskRecord(record.getTaskID(), TaskState.Free, -1, record.getResult());
            servant.getProgress().update(updated);
            servant.lookAllPublishers(record, updated);
        });
    }
}
