package pl.edu.agh.calculationp2p.state.request.UIRequests;

import pl.edu.agh.calculationp2p.state.Servant;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.request.MethodRequest;

public class TaskObserversRequest implements MethodRequest {
    Future<Integer> future;

    public TaskObserversRequest(Future<Integer> future) {
        this.future = future;
    }

    @Override
    public void call(Servant servant) {
        Integer numberOfObservers = servant.getTaskPublisher().numberOfObservers();
        this.future.put(numberOfObservers);
    }
}
