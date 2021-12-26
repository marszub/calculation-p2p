package pl.edu.agh.calculationp2p.message.process;

import pl.edu.agh.calculationp2p.network.router.Router;
import pl.edu.agh.calculationp2p.state.proxy.StateUpdater;
import pl.edu.agh.calculationp2p.state.proxy.StatusInformer;


public class MessageProcessContextImpl implements MessageProcessContext{

    private final Router router = null;
    private final StateUpdater stateUpdater = null;
    private final StatusInformer stateInformer = null;
    private final NodeRegister nodeRegister = null;
    private final FutureProcessor futureProcessor = null;

    public void MessageProcessContext(){

    }

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
}
