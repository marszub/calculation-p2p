package pl.edu.agh.calculationp2p.message;

import pl.edu.agh.calculationp2p.message.body.Body;

public class MessageImpl {
    String payload;
    public MessageImpl(int sender, int receiver, Body body){
        this.payload = payload;
    }
    public String getValue(){
        return this.payload;
    }
}
