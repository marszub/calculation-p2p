package pl.edu.agh.calculationp2p.state.request.UIRequests;

import pl.edu.agh.calculationp2p.state.Servant;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.request.MethodRequest;

public class ProgressSizeRequest implements MethodRequest {
    Future<Integer> future;

    public ProgressSizeRequest(Future<Integer> future) {
        this.future = future;
    }

    @Override
    public void call(Servant servant) {
        Integer calculated = servant.getProgress().getTasks().size();
        this.future.put(calculated);
    }
}


