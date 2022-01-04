package pl.edu.agh.calculationp2p.message.body;

import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.message.MessageImpl;
import pl.edu.agh.calculationp2p.message.process.MessageProcessContext;


public class GetProgress implements Body{

    public GetProgress() {

    }

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
        //TODO
        Message messWithProgress = new MessageImpl(myId, sender, new GiveProgress(null));
        context.getRouter().send(messWithProgress);
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
}
