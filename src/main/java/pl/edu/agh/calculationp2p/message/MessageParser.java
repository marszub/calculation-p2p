package pl.edu.agh.calculationp2p.message;

public class MessageParser {
    public MessageParser(){
    }
    public Message parse(String message){
        message.replaceAll("\\s+","");
        message.replaceAll("^[\n\r]", "").replaceAll("[\n\r]$", "");

        String[] splitSender = message.split("sender:");
        System.out.println(splitSender);
        return null;
    }
}

