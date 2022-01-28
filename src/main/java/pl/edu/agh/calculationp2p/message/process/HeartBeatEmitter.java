package pl.edu.agh.calculationp2p.message.process;

import pl.edu.agh.calculationp2p.message.MessageImpl;
import pl.edu.agh.calculationp2p.message.body.HeartBeat;
import pl.edu.agh.calculationp2p.network.router.Router;

import java.time.ZonedDateTime;

public class HeartBeatEmitter {


    private final Router router;
    private final int timePeriod;
    private long lastBeatTime;
    public HeartBeatEmitter(int timePeriod, Router router){
        this.router = router;
        this.timePeriod = timePeriod;
        this.lastBeatTime = now();
    }

    public boolean beat(){
        if(now() - this.lastBeatTime >= this.timePeriod){
            router.send(new MessageImpl(router.getId(), -1, new HeartBeat()));
            this.lastBeatTime = now();
            return true;
        }
        return false;
    }

    public int nextBeatTime(){
        long now = now();
        return (int) (this.timePeriod-(now - this.lastBeatTime));
    }

    private long now(){
        return ZonedDateTime.now().toInstant().toEpochMilli();
    }
}
