package pl.edu.agh.calculationp2p.message.process.statemachine;

import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.message.MessageImpl;
import pl.edu.agh.calculationp2p.message.body.GetProgress;
import pl.edu.agh.calculationp2p.message.body.GiveInit;
import pl.edu.agh.calculationp2p.message.body.HeartBeat;
import pl.edu.agh.calculationp2p.message.body.Hello;
import pl.edu.agh.calculationp2p.message.process.HeartBeatEmitter;
import pl.edu.agh.calculationp2p.message.process.MessageProcessor;
import pl.edu.agh.calculationp2p.message.process.NodeRegister;
import pl.edu.agh.calculationp2p.network.router.Router;

import java.net.InetSocketAddress;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
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
                router.createInterface(message.getSender(), messageProcessor.getConfig().getServerAddress());
                messageProcessor.getContext().getNodeRegister().addPublicNode(message.getSender(), messageProcessor.getConfig().getServerAddress());
                message.process(messageProcessor.getContext());
            });
            awaitingMessages.forEach(message -> message.process(messageProcessor.getContext()));
            newMessages.forEach(message -> message.process(messageProcessor.getContext()));
            //TODO: send our state
            messageProcessor.setState(new UninitializedWorkState());
            router.send(new MessageImpl(router.getId(), router.getBroadcastId(),
                    new Hello(messageProcessor.getConfig().getMyAddress())));
            router.send(new MessageImpl(router.getId(), getRandomNodeId(), new GetProgress()));
            return;
        }

        awaitingMessages.addAll(newMessages);

        long waitTime = startTime + messageProcessor.getConfig().getMaxConnectingTime() - ZonedDateTime.now().toInstant().toEpochMilli();
        messageProcessor.getIdle().sleep(waitTime);

        waitTime = startTime + messageProcessor.getConfig().getMaxConnectingTime() - ZonedDateTime.now().toInstant().toEpochMilli();
        if(waitTime < 0){
            router.deleteInterface(router.getMainServerId());
            router.setId(1);
            messageProcessor.getContext().getStateUpdater().setNodeId(1);
            messageProcessor.setState(new WorkState());
        }
    }

    private Integer getRandomNodeId(){
        NodeRegister nodeRegister = messageProcessor.getContext().getNodeRegister();
        List<Integer> nodes = nodeRegister.getPrivateNodes();
        nodes.addAll(nodeRegister.getPublicNodes().keySet().stream().toList());
        Random rand = new Random();
        return nodes.get(rand.nextInt(nodes.size()));
    }
}
