package pl.edu.agh.calculationp2p.message.process;

import pl.edu.agh.calculationp2p.network.router.Router;
import pl.edu.agh.calculationp2p.state.proxy.StateUpdater;
import pl.edu.agh.calculationp2p.state.proxy.StatusInformer;


public class MessageProcessContextImpl implements MessageProcessContext{

    private Router router;
    private StateUpdater stateUpdater;
    private StatusInformer stateInformer;
    private NodeRegister nodeRegister;
    private FutureProcessor futureProcessor;

    @Override
    public MessageProcessor getMessageProcessor() {
        return messageProcessor;
    }

    @Override
    public void setMessageProcessor(MessageProcessor messageProcessor) {
        this.messageProcessor = messageProcessor;
    }

    private MessageProcessor messageProcessor;

    public void MessageProcessContext(){}

    @Override
    public Router getRouter() {
        return this.router;
    }

    @Override
    public StateUpdater getStateUpdater() {
        return this.stateUpdater;
    }

    @Override
    public StatusInformer getStateInformer() {
        return this.stateInformer;
    }

    @Override
    public NodeRegister getNodeRegister() {
        return this.nodeRegister;
    }

    @Override
    public FutureProcessor getFutureProcessor() {
        return this.futureProcessor;
    }

    @Override
    public void setRouter(Router router){this.router = router;}
    @Override
    public void setStateUpdater(StateUpdater stateUpdater) {this.stateUpdater = stateUpdater;}
    @Override
    public void setStateInformer(StatusInformer stateInformer) {this.stateInformer = stateInformer;}
    @Override
    public void setNodeRegister(NodeRegister nodeRegister) {this.nodeRegister = nodeRegister;}
    @Override
    public void setFutureProcessor(FutureProcessor futureProcessor) {this.futureProcessor = futureProcessor;}

}
