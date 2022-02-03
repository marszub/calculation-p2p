package pl.edu.agh.calculationp2p.message.process.statemachine;

import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.message.MessageImpl;
import pl.edu.agh.calculationp2p.message.body.GetProgress;
import pl.edu.agh.calculationp2p.message.process.MessageProcessor;
import pl.edu.agh.calculationp2p.message.process.NodeRegister;
import pl.edu.agh.calculationp2p.network.router.Router;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;

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

    @Override
    public void proceed() throws InterruptedException {
        Router router = messageProcessor.getContext().getRouter();
        messageProcessor.getHeartBeatEmitter().beat();

        List<Message> newMessages = router.getMessage();
        newMessages.forEach(message-> message.process(messageProcessor.getContext()));

        int routerId = router.getId();
        List<Message> toSend =  messageProcessor.getStateObserver().getMessages(routerId);

        toSend.forEach(router::send);

        List<Integer> notResponding = messageProcessor.getContext().getNodeRegister().getOutdatedNodes();
        if(notResponding.size() > 0){
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.debug("Deleting outdated interfaces: " + notResponding);
        }
        notResponding.forEach(router::deleteInterface);

        messageProcessor.getContext().getFutureProcessor().tryProcessAll();

        int timeOfNextProgress = (int) (lastProgressAskTime + messageProcessor.getConfig().getGetProgressRetryTime()
                - ZonedDateTime.now().toInstant().toEpochMilli());
        if(timeOfNextProgress < 0){
            timeOfNextProgress = messageProcessor.getConfig().getGetProgressRetryTime();
            router.send(new MessageImpl(router.getId(), getRandomNodeId(), new GetProgress()));
        }
        int waitTime = Math.min(messageProcessor.getHeartBeatEmitter().nextBeatTime(), timeOfNextProgress);
        messageProcessor.getIdle().sleep(waitTime);
    }

    private Integer getRandomNodeId(){
        NodeRegister nodeRegister = messageProcessor.getContext().getNodeRegister();
        List<Integer> nodes = nodeRegister.getPrivateNodes();
        nodes.addAll(nodeRegister.getPublicNodes().keySet().stream().toList());
        Random rand = new Random();
        return nodes.get(rand.nextInt(nodes.size())); // TODO: move to NodeRegister
    }
}
