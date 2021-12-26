package pl.edu.agh.calculationp2p.message.process;

import pl.edu.agh.calculationp2p.network.router.Router;
import pl.edu.agh.calculationp2p.state.proxy.StateUpdater;
import pl.edu.agh.calculationp2p.state.proxy.StatusInformer;

public interface MessageProcessContext {
    public Router getRouter();
    public StateUpdater getStateUpdater();
    public StatusInformer getStateInformer();
    public NodeRegister getNodeRegister();
    public FutureProcessor getFutureProcessor();

}
