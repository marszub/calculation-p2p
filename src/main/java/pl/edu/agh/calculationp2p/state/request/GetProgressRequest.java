package pl.edu.agh.calculationp2p.state.request;

import pl.edu.agh.calculationp2p.state.Progress;
import pl.edu.agh.calculationp2p.state.Servant;
import pl.edu.agh.calculationp2p.state.future.Future;

public class GetProgressRequest implements MethodRequest {
    Future<Progress> future;

    public GetProgressRequest(Future<Progress> future) {
        this.future = future;
    }
    @Override
    public void call(Servant servant){
        future.put(servant.getProgress());
    }
}
