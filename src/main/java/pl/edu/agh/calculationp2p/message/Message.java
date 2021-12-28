package pl.edu.agh.calculationp2p.message;

import pl.edu.agh.calculationp2p.message.process.MessageProcessContext;

public interface Message {
    Message clone(int receiverId);
    String serialize();
    void process(MessageProcessContext context);
    int getReceiver();
    int getSender();
}
