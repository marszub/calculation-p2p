package pl.edu.agh.calculationp2p.network.utilities;

import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.message.process.MessageProcessContext;

public class DummyMessage implements Message {

    public DummyMessage()
    {
    }

    @Override
    public Message clone(int receiverId) {
        return null;
    }

    @Override
    public String serialize() {
        return "TESTOWA WIADOMOSC";
    }

    @Override
    public void process(MessageProcessContext context)
    {
    }
    @Override
    public int getReceiver()
    {
        return 1;
    }
    @Override
    public int getSender()
    {
        return 1;
    }
}
