package pl.edu.agh.calculationp2p.message.process;

import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.message.MessageImpl;
import pl.edu.agh.calculationp2p.message.body.Calculated;
import pl.edu.agh.calculationp2p.message.body.Reserve;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.future.Observation;
import pl.edu.agh.calculationp2p.state.idle.Idle;
import pl.edu.agh.calculationp2p.state.idle.IdleInterrupter;
import pl.edu.agh.calculationp2p.state.proxy.StatusInformer;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;

import java.util.ArrayList;
import java.util.List;

public class StateObserver {

    private Future<Observation> reservedF;
    private Future<Observation> calculatedF;

    public StateObserver(StatusInformer informer, IdleInterrupter idle){
        reservedF = informer.observeReserved(idle);
        calculatedF = informer.observeCalculated(idle);
    }

    public List<Message> getMessages(int myId){

        List<Message> result = new ArrayList<>();

        while (reservedF.isReady()){
            TaskRecord task = reservedF.get().getTask();
            result.add(new MessageImpl(myId, -1, new Reserve(task)));
            reservedF = reservedF.get().getNextObservation();
        }

        while (calculatedF.isReady()){
            TaskRecord taskRecord = calculatedF.get().getTask();
            //TODO: calculated -> update
            result.add(new MessageImpl(myId, -1, new Calculated(taskRecord)));
            calculatedF = calculatedF.get().getNextObservation();
        }
        return result;
    }
}
