package pl.edu.agh.calculationp2p.message.process;

import pl.edu.agh.calculationp2p.message.MessageImpl;
import pl.edu.agh.calculationp2p.message.body.HeartBeat;
import pl.edu.agh.calculationp2p.network.router.Router;

import java.time.ZonedDateTime;

public class HeartBeatEmiter {

    private final Router router;
    private final int timePeriod;
    private long lastBeatTime;

    protected HeartBeatEmiter(int timePeriod, Router router){
        this.router = router;
        this.timePeriod = timePeriod;
    }

    protected void beat(){
        if(ZonedDateTime.now().toInstant().toEpochMilli() - this.lastBeatTime >= this.timePeriod){
            router.send(new MessageImpl(router.getId(), -1, new HeartBeat()));
            this.lastBeatTime = ZonedDateTime.now().toInstant().toEpochMilli();
        }
    }

    protected int nextBeatTime(){
        long now = ZonedDateTime.now().toInstant().toEpochMilli();
        return (int) (this.timePeriod-(now - this.lastBeatTime));
    }

}
