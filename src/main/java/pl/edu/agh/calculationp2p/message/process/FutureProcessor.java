package pl.edu.agh.calculationp2p.message.process;

import pl.edu.agh.calculationp2p.state.future.Future;

import java.util.ArrayList;
import java.util.List;

public class FutureProcessor {

    private List<Future> futureList;

    public FutureProcessor(){
        this.futureList = new ArrayList<>();
    }

    protected void tryProcessAll(){

        List<Future> readyFutures = new ArrayList<>();
        for(Future future : this.futureList){
            if(future.isReady()){
                readyFutures.add(future);
            }
        }

        for(Future future : readyFutures){
            //TODO: runable.run()
            future.get();
            this.futureList.remove(future);
        }

    }
    public void addFutureProcess(Future condition, Runnable process){

    }

}
