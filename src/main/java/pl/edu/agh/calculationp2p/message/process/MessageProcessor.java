package pl.edu.agh.calculationp2p.message.process;

import pl.edu.agh.calculationp2p.AppConfig;
import pl.edu.agh.calculationp2p.message.process.statemachine.ProcessingState;
import pl.edu.agh.calculationp2p.network.router.Router;
import pl.edu.agh.calculationp2p.state.idle.Idle;
import pl.edu.agh.calculationp2p.state.proxy.StateUpdater;
import pl.edu.agh.calculationp2p.state.proxy.StatusInformer;

public class MessageProcessor implements Runnable {

    private final MessageProcessContext context;
    private final HeartBeatEmitter heartBeatEmitter;
    private final StateObserver stateObserver;

    public Idle getIdle() {
        return idle;
    }

    private final Idle idle;

    public AppConfig getConfig() {
        return config;
    }

    private final AppConfig config;

    private ProcessingState state;

    public MessageProcessor(Router router,
                            StateUpdater stateUpdater,
                            StatusInformer statusInformer,
                            Idle idle,
                            AppConfig config,
                            ProcessingState initialState){
        this.config = config;

        int validityTime = config.getHeartBeatLifetime();
        int timePeriod = config.getHeartBeatPeriod();

        this.context = new MessageProcessContextImpl();
        context.setRouter(router);
        context.setStateUpdater(stateUpdater);
        context.setStateInformer(statusInformer);
        context.setFutureProcessor(new FutureProcessor());
        context.setOutdatedNodesDeleter(new OutdatedNodesDeleter(validityTime));
        context.setMessageProcessor(this);

        this.setState(initialState);
        this.idle = idle;

        this.heartBeatEmitter = new HeartBeatEmitter(timePeriod, router);
        this.stateObserver = new StateObserver(context, idle);
    }


    public void setState(ProcessingState state) {
        this.state = state;
        this.state.setContext(this);
    }

    public MessageProcessContext getContext() {
        return context;
    }

    public HeartBeatEmitter getHeartBeatEmitter() {
        return heartBeatEmitter;
    }

    public StateObserver getStateObserver() {
        return stateObserver;
    }


    @Override
    public void run() {
        while(true){
            try{
                state.proceed();
            }catch (InterruptedException e){
                return;
            }
        }
    }
}
