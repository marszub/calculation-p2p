package pl.edu.agh.calculationp2p.state.request.UIRequests;

import pl.edu.agh.calculationp2p.state.Servant;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.publisher.Pair;
import pl.edu.agh.calculationp2p.state.request.MethodRequest;

public class CalculatedNumberRequest implements MethodRequest {
    Future<Integer> future;

    public CalculatedNumberRequest(Future<Integer> future) {
        this.future = future;
    }

    @Override
    public void call(Servant servant) {
        Integer calculated = servant.getProgress().countCompleted();
        this.future.put(calculated);
    }
}
