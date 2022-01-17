package pl.edu.agh.calculationp2p.state.request;

import pl.edu.agh.calculationp2p.state.Servant;
import pl.edu.agh.calculationp2p.state.idle.IdleInterrupter;

public class CancelObservationRequest implements MethodRequest{
    IdleInterrupter interrupter;

    public CancelObservationRequest(IdleInterrupter interrupter) {
        this.interrupter = interrupter;
    }

    @Override
    public void call(Servant servant) {
        servant.getReservedPublisher().unsubscribe(interrupter);
        servant.getCalculatedPublisher().unsubscribe(interrupter);
    }
}
