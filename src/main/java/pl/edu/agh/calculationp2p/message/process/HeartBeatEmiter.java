package pl.edu.agh.calculationp2p.message.process;

import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.message.MessageImpl;
import pl.edu.agh.calculationp2p.message.body.HeartBeat;
import pl.edu.agh.calculationp2p.network.router.Router;

public class HeartBeatEmiter {

    private final Router router;
    private final int timePeriod;

    protected HeartBeatEmiter(int timePeriod, Router router){
        this.router = router;
        this.timePeriod = timePeriod;
    }

    protected void beat(){
        // TODO: check time
        router.send((Message) new MessageImpl(router.getId(), -1, new HeartBeat()));

    }

    protected int nextBeatTime(){
        //TODO:
        return this.timePeriod;
    }

}
