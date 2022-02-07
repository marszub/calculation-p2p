package pl.edu.agh.calculationp2p.message.process.statemachine;

import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.message.process.MessageProcessor;

import java.util.List;

public class WorkState implements ProcessingState{
    private MessageProcessor messageProcessor;

    public WorkState(){
        messageProcessor = null;
    }

    @Override
    public void setContext(MessageProcessor messageProcessor) {
        this.messageProcessor = messageProcessor;
    }

    /**
     * Performs steps:
     * 1. Send heart beat
     * 2. Process incoming messages
     * 3. Send updates of my calculation progress
     * 4. Delete inactive interfaces
     * 5. Process future tasks
     * 6. Sleep
     * @throws InterruptedException
     */
    @Override
    public void proceed() throws InterruptedException {
        messageProcessor.getHeartBeatEmitter().beat();
        List<Message> newMessages = messageProcessor.getContext().getRouter().getMessage();
        newMessages.forEach(message-> message.process(messageProcessor.getContext()));

        int routerId = messageProcessor.getContext().getRouter().getId();
        List<Message> toSend =  messageProcessor.getStateObserver().getMessages(routerId);

        toSend.forEach(message -> messageProcessor.getContext().getRouter().send(message));

        List<Integer> notResponding = messageProcessor.getContext().getNodeRegister().getOutdatedNodes();
        notResponding.forEach(id -> {
            messageProcessor.getContext().getRouter().deleteInterface(id);
            messageProcessor.getContext().getStateUpdater().clearNodeReservations(id);
        });

        messageProcessor.getContext().getFutureProcessor().tryProcessAll();

        messageProcessor.getIdle().sleep(messageProcessor.getHeartBeatEmitter().nextBeatTime());
    }
}
