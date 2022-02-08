package pl.edu.agh.calculationp2p.message.process;

import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.message.MessageImpl;
import pl.edu.agh.calculationp2p.message.body.Calculated;
import pl.edu.agh.calculationp2p.message.body.GetSynchronization;
import pl.edu.agh.calculationp2p.message.body.Reserve;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.future.Observation;
import pl.edu.agh.calculationp2p.state.idle.IdleInterrupter;
import pl.edu.agh.calculationp2p.state.proxy.StatusInformer;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StateObserver {
    private boolean sentFinalSync = false;
    private Future<Observation> reservedF;
    private Future<Observation> calculatedF;
    private final MessageProcessContext context;

    public StateObserver(MessageProcessContext context, IdleInterrupter idle){
        this.context = context;
        reservedF = context.getStateInformer().observeReserved(idle);
        calculatedF = context.getStateInformer().observeCalculated(idle);
    }

    public List<Message> getMessages(int myId){

        List<Message> result = new ArrayList<>();

        while (reservedF.isReady()){
            TaskRecord task = reservedF.get().getTask();
            if(task.getOwner()==myId)
                result.add(new MessageImpl(myId, -1, new Reserve(task)));
            reservedF = reservedF.get().getNextObservation();
            CheckNoFreeTasks();
        }

        while (calculatedF.isReady()){
            TaskRecord taskRecord = calculatedF.get().getTask();
            if(taskRecord.getOwner()==myId)
                result.add(new MessageImpl(myId, -1, new Calculated(taskRecord)));
            calculatedF = calculatedF.get().getNextObservation();
            CheckNoFreeTasks();
        }
        return result;
    }

    private void CheckNoFreeTasks(){
        Future<Optional<Integer>> noFreeFuture = context.getStateInformer().getFreeTask();
        context.getFutureProcessor().addFutureProcess(noFreeFuture, () ->{
            if(noFreeFuture.get().isEmpty() && !sentFinalSync){
                sentFinalSync = true;
                Future<List<TaskRecord>> reservedTasks = context.getStateInformer().getReservedTasks();
                context.getFutureProcessor().addFutureProcess(reservedTasks, () -> SendGetSynchronization(reservedTasks));
            }
        });
    }

    private void SendGetSynchronization(Future<List<TaskRecord>> reservedTasks) {
        List<Integer> reservingNodes = reservedTasks
                .get().stream()
                .map(TaskRecord::getOwner)
                .filter(owner -> owner != context.getRouter().getId())
                .distinct()
                .collect(Collectors.toList());

        reservingNodes.forEach(nodeID ->{
            List<Integer> tasksToCheck = reservedTasks
                    .get().stream()
                    .filter(taskRecord -> taskRecord.getOwner() == nodeID)
                    .map(TaskRecord::getTaskID)
                    .collect(Collectors.toList());

            context.getRouter().send(new MessageImpl(context.getRouter().getId(), nodeID, new GetSynchronization(tasksToCheck)));
        });
    }
}
