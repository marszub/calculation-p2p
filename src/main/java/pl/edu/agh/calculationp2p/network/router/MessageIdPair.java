package pl.edu.agh.calculationp2p.network.router;

import pl.edu.agh.calculationp2p.message.Message;

public record MessageIdPair(Message message, int id) {

    public int getId()
    {
        return id;
    }

    public Message getMessage()
    {
        return message;
    }
}
