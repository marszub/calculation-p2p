package pl.edu.agh.calculationp2p.state.request;

import pl.edu.agh.calculationp2p.state.Servant;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.future.Observation;
import pl.edu.agh.calculationp2p.state.idle.IdleInterrupter;

public class ObserveCalculatedRequest implements MethodRequest{
    Future<Observation> future;
    IdleInterrupter interrupter;

    public ObserveCalculatedRequest(Future<Observation> future, IdleInterrupter interrupter) {
        this.future = future;
        this.interrupter = interrupter;
    }

    @Override
    public void call(Servant servant) {
        servant.getCalculatedPublisher().subscribe(future, interrupter);
    }

}
