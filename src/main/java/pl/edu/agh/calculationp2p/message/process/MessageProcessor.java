package pl.edu.agh.calculationp2p.message.process;

import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.network.router.Router;
import pl.edu.agh.calculationp2p.state.idle.Idle;
import pl.edu.agh.calculationp2p.state.proxy.StateUpdater;
import pl.edu.agh.calculationp2p.state.proxy.StatusInformer;

import java.util.List;

public class MessageProcessor implements Runnable {

    private final MessageProcessContext context;
    private final HeartBeatEmiter heartBeatEmiter;
    private final StateObserver stateObserver;
    private final Idle idle;

    public MessageProcessor(Router router, StateUpdater stateUpdater, StatusInformer statusInformer){
        int validityTime = 10;
        int timePeriod = 10;

        this.context = new MessageProcessContextImpl();
        context.setRouter(router);
        context.setStateUpdater(stateUpdater);
        context.setStateInformer(statusInformer);
        context.setFutureProcessor(new FutureProcessor());
        context.setNodeRegister(new NodeRegister(validityTime));

        this.idle = new Idle();

        this.heartBeatEmiter = new HeartBeatEmiter(timePeriod, router);
        this.stateObserver = new StateObserver(statusInformer, idle);
    }

    @Override
    public void run() {

        int routerId = this.context.getRouter().getId();

        while(true){

            this.heartBeatEmiter.beat();
            List<Message> newMessages = context.getRouter().getMessage();
            newMessages.forEach(message-> message.process(context));

            List<Message> toSend =  this.stateObserver.getMessages(context.getRouter().getId());
            toSend.forEach(message -> context.getRouter().send(message));

            List<Integer> notResponding = context.getNodeRegister().getOutdatedNodes();
            notResponding.forEach(id -> context.getRouter().deleteInterface(id));

            context.getFutureProcessor().tryProcessAll();

            try {
                this.idle.sleep(this.heartBeatEmiter.nextBeatTime());
            } catch (InterruptedException e) {
                return;
            }

        }
    }

}
