package pl.edu.agh.calculationp2p.message.process;

import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.network.router.Router;
import pl.edu.agh.calculationp2p.state.Scheduler;
import pl.edu.agh.calculationp2p.state.idle.Idle;
import pl.edu.agh.calculationp2p.state.proxy.StateUpdater;
import pl.edu.agh.calculationp2p.state.proxy.StatusInformer;

import java.util.List;
import java.util.Optional;

public class MessageProcessor implements Runnable {

    private Router router;
    private MessageProcessContext messageProcessContext;
    private HeartBeatEmiter heartBeatEmiter;
    private FutureProcessor futureProcessor;
    private NodeRegister nodeRegister;
    private StateObserver stateObserver;
    private Idle idle;

    public void MessageProcessor(Router router, StateUpdater stateUpdater, StatusInformer statusInformer){
        this.router = router;
        this.idle = new Idle();

        this.heartBeatEmiter = new HeartBeatEmiter(10, router);
        this.futureProcessor = new FutureProcessor();
        this.nodeRegister = new NodeRegister(100);
        this.stateObserver = new StateObserver(statusInformer);
    }

    @Override
    public void run() {

        while(true){

            this.heartBeatEmiter.beat();
            //TODO: list
            List<Message> newMessages = this.router.getMessage();
            newMessages.forEach(message->{
                message.process(messageProcessContext);
            });

            List<Message> toSend =  this.stateObserver.getMessages();
            for(Message message : toSend){
                this.router.send(message);
            }

            List<Integer> notResponding = this.nodeRegister.getOutdatedNodes();
            notResponding.forEach(id -> this.router.deleteInterface(id));
            futureProcessor.tryProcessAll();
            // TODO: zrob cos z tymi do ktorych nie ma dostepu

            try {
                this.idle.sleep(this.heartBeatEmiter.nextBeatTime());
            } catch (InterruptedException e) {
                // TODO finish
            }

        }
    }

}
