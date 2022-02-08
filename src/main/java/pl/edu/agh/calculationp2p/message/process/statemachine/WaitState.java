package pl.edu.agh.calculationp2p.message.process.statemachine;

import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.message.MessageImpl;
import pl.edu.agh.calculationp2p.message.body.*;
import pl.edu.agh.calculationp2p.message.process.MessageProcessor;
import pl.edu.agh.calculationp2p.network.router.Router;
import pl.edu.agh.calculationp2p.state.Progress;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.task.TaskState;

import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class WaitState implements ProcessingState{
    private MessageProcessor messageProcessor;
    private final long startTime;
    private final List<Message> awaitingMessages;

    public WaitState(){
        messageProcessor = null;
        startTime = ZonedDateTime.now().toInstant().toEpochMilli();
        awaitingMessages = new LinkedList<>();
    }

    @Override
    public void setContext(MessageProcessor messageProcessor) {
        this.messageProcessor = messageProcessor;
    }

    /**
     * Performs steps:
     * 1. GiveInit came?
     *    YES:
     *    1. Delete temporary server interface
     *    2. For GiveInit create server interface with real id
     *    3. Process GiveInit
     *    4. Process awaiting messages and just received
     *    5. broadcast Hello
     *    6. Broadcast current progress (as Calculated/Reserve)
     *    7. Change state to UninitializedWorkState
     *    NO:
     *    1. Put all received messages on queue
     *    2. If time exceeded wait time, delete temporary server interface, set id to 1 and change state to Work
     *    3. Sleep
     * @throws InterruptedException
     */
    @Override
    public void proceed() throws InterruptedException {
        Router router = messageProcessor.getContext().getRouter();

        List<Message> newMessages = router.getMessage();
        List<Message> giveInitMessages = newMessages
                .stream()
                .filter(message -> message.getBody() instanceof GiveInit)
                .collect(Collectors.toList());

        newMessages.removeAll(giveInitMessages);

        if(giveInitMessages.size() > 0) {
            router.deleteInterface(router.getMainServerId());
            giveInitMessages.forEach(message -> {
                router.connectToInterface(message.getSender(), messageProcessor.getConfig().getServerAddress());
                message.process(messageProcessor.getContext());
            });
            awaitingMessages.forEach(message -> message.process(messageProcessor.getContext()));
            newMessages.forEach(message -> message.process(messageProcessor.getContext()));
            // TODO: no public flag
            router.sendHelloMessage(new MessageImpl(router.getId(), router.getBroadcastId(),
                    new Hello(messageProcessor.getConfig().getPublicFlag()?messageProcessor.getConfig().getMyAddress():null)));

            //Send our state

            Future<Progress> progressFuture = messageProcessor.getContext().getStateInformer().getProgress();
            messageProcessor.getContext().getFutureProcessor().addFutureProcess(progressFuture, () ->{
                progressFuture.get().getTasks().stream().filter(taskRecord -> taskRecord.getState() == TaskState.Reserved)
                        .forEach(taskRecord -> {
                            router.send(new MessageImpl(router.getId(), router.getBroadcastId(), new Reserve(taskRecord)));
                });

                progressFuture.get().getTasks().stream().filter(taskRecord -> taskRecord.getState() == TaskState.Calculated)
                        .forEach(taskRecord -> {
                            router.send(new MessageImpl(router.getId(), router.getBroadcastId(), new Calculated(taskRecord)));
                });
            });

            router.send(new MessageImpl(router.getId(),
                    messageProcessor.getContext().getRouter().getNodeRegister().getRandomNodeId(), new GetProgress()));
            messageProcessor.setState(new UninitializedWorkState());
            return;
        }

        awaitingMessages.addAll(newMessages);

        long waitTime = startTime + messageProcessor.getConfig().getMaxConnectingTime() - ZonedDateTime.now().toInstant().toEpochMilli();
        if(waitTime < 0){
            router.deleteInterface(router.getMainServerId());
            router.setId(1);
            messageProcessor.getContext().getStateUpdater().setNodeId(1);
            messageProcessor.setState(new WorkState());
        }
        messageProcessor.getIdle().sleep(waitTime);
    }
}
