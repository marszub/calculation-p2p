package pl.edu.agh.calculationp2p.state.request;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import pl.edu.agh.calculationp2p.state.Servant;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class GetReservedTasksRequest implements MethodRequest {
    Future<List<TaskRecord>> future;

    public GetReservedTasksRequest(Future<List<TaskRecord>> future) {
        this.future = future;
    }

    @Override
    public void call(Servant servant) {
        List<TaskRecord> reservedTasks = servant.getProgress().getReservedTasksList();
        future.put(reservedTasks);
    }
}