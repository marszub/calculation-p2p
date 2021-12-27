package pl.edu.agh.calculationp2p.message.process;

import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.state.proxy.StatusInformer;

import java.util.List;

public class StateObserver {

    private final StatusInformer informer;

    public StateObserver(StatusInformer informer){
        this.informer = informer;
    }

    public List<Message> getMessages(){
        //TODO: check all messages from status informer
        return null;
    }
}
