package pl.edu.agh.calculationp2p.network.utilities;

import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.message.process.MessageProcessContext;

public class DummyMessage2 implements Message {
    int receiver;
    int sender;
    String text;

    public DummyMessage2(String text)
    {
        this.text = text;
    }

    @Override
    public Message clone(int receiverId) {
        DummyMessage2 msg = new DummyMessage2(text);
        msg.setReceiver(receiverId);
        msg.setSender(sender);
        return msg;
    }

    @Override
    public String serialize() {
        return String.valueOf(receiver) + text;
    }

    @Override
    public void process(MessageProcessContext context) {
    }

    @Override
    public int getReceiver() {
        return receiver;
    }

    @Override
    public int getSender() {
        return sender;
    }

    public void setSender(int id)
    {
        sender = id;
    }

    public void setReceiver(int id)
    {
        receiver = id;
    }
}
