package pl.edu.agh.calculationp2p.message.process.statemachine;

import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.message.MessageImpl;
import pl.edu.agh.calculationp2p.message.body.GetProgress;
import pl.edu.agh.calculationp2p.message.process.MessageProcessor;
import pl.edu.agh.calculationp2p.network.router.Router;

import java.time.ZonedDateTime;
import java.util.List;

public class UninitializedWorkState implements ProcessingState{
    private MessageProcessor messageProcessor;
    private long lastProgressAskTime;

    public UninitializedWorkState(){
        messageProcessor = null;
        lastProgressAskTime = ZonedDateTime.now().toInstant().toEpochMilli();
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
     * 6. Resend progress request
     * 7. Sleep
     * @throws InterruptedException
     */
    @Override
    public void proceed() throws InterruptedException {
        Router router = messageProcessor.getContext().getRouter();
        messageProcessor.getHeartBeatEmitter().beat();

        List<Message> newMessages = router.getMessage();
        newMessages.forEach(message-> message.process(messageProcessor.getContext()));

        int routerId = router.getId();
        List<Message> toSend =  messageProcessor.getStateObserver().getMessages(routerId);

        toSend.forEach(router::send);

        List<Integer> notResponding = messageProcessor.getContext()
                .getOutdatedNodesDeleter().getOutdatedNodes(router.getNodeRegister().getAllNodes());

        notResponding.forEach(id -> {
            router.deleteInterface(id);
            messageProcessor.getContext().getStateUpdater().clearNodeReservations(id);
        });

        messageProcessor.getContext().getFutureProcessor().tryProcessAll();

        int timeOfNextProgress = (int) (lastProgressAskTime + messageProcessor.getConfig().getGetProgressRetryTime()
                - ZonedDateTime.now().toInstant().toEpochMilli());
        if(timeOfNextProgress < 0){
            timeOfNextProgress = messageProcessor.getConfig().getGetProgressRetryTime();
            router.send(new MessageImpl(router.getId(), messageProcessor.getContext().getRouter().getNodeRegister().getRandomNodeId(), new GetProgress()));
        }
        int waitTime = Math.min(messageProcessor.getHeartBeatEmitter().nextBeatTime(), timeOfNextProgress);
        messageProcessor.getIdle().sleep(waitTime);
    }
}
