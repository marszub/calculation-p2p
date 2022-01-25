package pl.edu.agh.calculationp2p.message.body;

import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.message.MessageImpl;
import pl.edu.agh.calculationp2p.message.process.MessageProcessContext;
import pl.edu.agh.calculationp2p.state.Progress;
import pl.edu.agh.calculationp2p.state.future.Future;

import java.util.Objects;


public class GetProgress implements Body{

    @Override
    public String serializeType() {
        return "\"get_progress\"";
    }

    @Override
    public String serializeContent() {
        return "{}";
    }

    @Override
    public void process(int sender, MessageProcessContext context) {
        int myId = context.getRouter().getId();
        Future<Progress> future = context.getStateInformer().getProgress();
        context.getFutureProcessor().addFutureProcess(future, ()->{
            Message messWithProgress = new MessageImpl(myId, sender, new GiveProgress(future.get()));
            context.getRouter().send(messWithProgress);
        });
    }

    @Override
    public Body clone() {
        return new GetProgress();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        return getClass() == o.getClass();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this);
    }
}
