package pl.edu.agh.calculationp2p.state.request.UIRequests;

import pl.edu.agh.calculationp2p.state.Servant;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.request.MethodRequest;

public class ReservedObserversRequest implements MethodRequest {
    Future<Integer> future;

    public ReservedObserversRequest(Future<Integer> future) {
        this.future = future;
    }

    @Override
    public void call(Servant servant) {
        Integer numberOfObservers = servant.getReservedPublisher().numberOfObservers();
        this.future.put(numberOfObservers);
    }
}


