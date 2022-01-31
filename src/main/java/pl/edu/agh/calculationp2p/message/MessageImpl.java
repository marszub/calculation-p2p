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
        return new MessageImpl(sender, receiverId, body.clone());
    }

    @Override
    public String serialize() {
        String s = "{\"header\":{\"sender\":";
        s = s.concat(String.valueOf(sender));
        s = s.concat(",\"receiver\":");
        s = s.concat(String.valueOf(receiver));
        s = s.concat(",\"message_type\":");
        s = s.concat(body.serializeType());
        s = s.concat("},\"body\":");
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
        return this.receiver;
    }
    @Override
    public int getSender()
    {
        return this.sender;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        Message message = (Message) o;
        return message.getReceiver() == this.receiver && message.getSender() == this.sender && message.getBody().equals(this.body);
    }
    @Override
    public Body getBody(){
        return this.body;
    }
}
