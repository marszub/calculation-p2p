package pl.edu.agh.calculationp2p.message.process;

import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.message.MessageImpl;
import pl.edu.agh.calculationp2p.message.body.Calculated;
import pl.edu.agh.calculationp2p.message.body.Reserve;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.future.Observation;
import pl.edu.agh.calculationp2p.state.idle.Idle;
import pl.edu.agh.calculationp2p.state.proxy.StatusInformer;

import java.util.ArrayList;
import java.util.List;

public class StateObserver {

    private final StatusInformer informer;

    public StateObserver(StatusInformer informer){
        this.informer = informer;
    }

    public List<Message> getMessages(Idle idle, int myId){

        List<Message> result = new ArrayList<>();

        Future<Observation> reservedF = this.informer.observeReserved(idle);
        Future<Observation> calculatedF = this.informer.observeCalculated(idle);

        Future<Observation> currPointerToReserved = reservedF;
        while (currPointerToReserved.isReady()){
            //TODO: int taskId = currPointerToReserved.get().getTask();
            int taskId = 0;
            result.add(new MessageImpl(myId, -1, new Reserve(taskId)));
            currPointerToReserved = currPointerToReserved.get().getNextObservation();
        }

        Future<Observation> currPointerToCalculated = calculatedF;
        while (currPointerToCalculated.isReady()){
            //TODO: int taskId = currPointerToReserved.get().getTask();
            int taskId = 0;
            //TODO: null -> new TaskResult
            result.add(new MessageImpl(myId, -1, new Calculated(taskId, null)));
            currPointerToCalculated = currPointerToCalculated.get().getNextObservation();
        }
        return result;
    }
}
