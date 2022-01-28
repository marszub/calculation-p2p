package pl.edu.agh.calculationp2p.message.process.statemachine;

import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.message.MessageImpl;
import pl.edu.agh.calculationp2p.message.body.GetInit;
import pl.edu.agh.calculationp2p.message.process.MessageProcessor;
import pl.edu.agh.calculationp2p.network.router.Router;

public class StartState implements ProcessingState{
    private MessageProcessor messageProcessor;

    public StartState(){
        messageProcessor = null;
    }

    @Override
    public void setContext(MessageProcessor messageProcessor) {
        this.messageProcessor = messageProcessor;
    }

    @Override
    public void proceed() {

        Router router = messageProcessor.getContext().getRouter();
        router.setId(router.getUnknownId());
        router.createInterface(router.getMainServerId(), messageProcessor.getConfig().getServerAddress());


        Message message = new MessageImpl(router.getUnknownId(), router.getMainServerId(), new GetInit());
        router.send(message);

        messageProcessor.setState(new WaitState());
    }
}
