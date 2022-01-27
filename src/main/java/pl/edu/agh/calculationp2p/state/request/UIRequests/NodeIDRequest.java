package pl.edu.agh.calculationp2p.state.request.UIRequests;

import pl.edu.agh.calculationp2p.state.Servant;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.request.MethodRequest;

import java.util.Optional;

public class NodeIDRequest implements MethodRequest {
    Future<Integer> future;

    public NodeIDRequest(Future<Integer> future) {
        this.future = future;
    }

    @Override
    public void call(Servant servant) {
        Integer id = servant.getNodeId();
        this.future.put(id);
    }
}
