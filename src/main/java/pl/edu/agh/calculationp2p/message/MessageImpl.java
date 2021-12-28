package pl.edu.agh.calculationp2p.message;

import pl.edu.agh.calculationp2p.message.body.Body;
import pl.edu.agh.calculationp2p.message.process.MessageProcessContext;

public class MessageImpl implements Message{

    private final int sender;
    private final int receiver;
    private final Body body;

    public MessageImpl(int sender, int receiver, Body body){
        this.sender = sender;
        this.receiver = receiver;
        this.body = body;
    }

    @Override
    public Message clone(int receiverId) {
        return new MessageImpl(sender, receiverId, body);
    }

    @Override
    public String serialize() {
        String s = "{'header':{'sender':";
        s = s.concat(String.valueOf(sender));
        s = s.concat(",'receiver':");
        s = s.concat(String.valueOf(receiver));
        s = s.concat(",'message_type':");
        s = s.concat(body.serializeType());
        s = s.concat("},'body':");
        s = s.concat(body.serializeContent());
        s = s.concat("}");
        return s;
    }

    @Override
    public void process(MessageProcessContext context) {
        this.body.process(this.sender, context);
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
