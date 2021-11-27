package pl.edu.agh.calculationp2p.network.message;

public class Message {
    String payload;

    public Message(String payload){
        this.payload = payload;
    }
    public String getValue(){
        return this.payload;
    }
}
