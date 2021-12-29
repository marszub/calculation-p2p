package pl.edu.agh.calculationp2p.network.utilities;

import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.message.process.MessageProcessContext;

public class DummyMessage implements Message {
    int receiver;
    int sender;
    String text = "";

    public DummyMessage() {}

    public DummyMessage(String string)
    {
        this.text = string;
    }

    @Override
    public Message clone(int receiverId) {
        DummyMessage msg = new DummyMessage();
        msg.setText(this.text);
        msg.setReceiver(receiverId);
        msg.setSender(this.sender);
        return msg;
    }

    @Override
    public String serialize() {
        return this.text;
    }

    @Override
    public void process(MessageProcessContext context)
    {
    }

    @Override
    public int getReceiver()
    {
        return receiver;
    }

    @Override
    public int getSender()
    {
        return sender;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public void setReceiver(int nodeId)
    {
        receiver = nodeId;
    }

    public void setSender(int nodeId)
    {
        sender = nodeId;
    }
}
