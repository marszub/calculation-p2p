package pl.edu.agh.calculationp2p.network.utilities;

import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.message.MessageParser;

import java.util.HashMap;

public class DummyMessageParser implements MessageParser {
    HashMap<String, Message> messageMap = new HashMap<>();
    @Override
    public Message parse(String message) {
         if(messageMap.containsKey(message))
             return messageMap.get(message);
         DummyMessage msg = new DummyMessage();
         msg.setText(message);
         return msg;
    }

    public void addParse(String serialize, Message message)
    {
        messageMap.put(serialize, message);
    }
}
