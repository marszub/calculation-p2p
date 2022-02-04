package pl.edu.agh.calculationp2p.message.process;

import pl.edu.agh.calculationp2p.network.router.Router;
import pl.edu.agh.calculationp2p.state.proxy.StateUpdater;
import pl.edu.agh.calculationp2p.state.proxy.StatusInformer;

public interface MessageProcessContext {
    Router getRouter();
    StateUpdater getStateUpdater();
    StatusInformer getStateInformer();
    NodeRegister getNodeRegister();
    FutureProcessor getFutureProcessor();
    MessageProcessor getMessageProcessor();

    void setMessageProcessor(MessageProcessor messageProcessor);
    void setRouter(Router router);
    void setStateUpdater(StateUpdater stateUpdater);
    void setStateInformer(StatusInformer stateInformer);
    void setNodeRegister(NodeRegister nodeRegister);
    void setFutureProcessor(FutureProcessor futureProcessor);
}
