package pl.edu.agh.calculationp2p.message.process;

import pl.edu.agh.calculationp2p.state.future.Future;

import java.util.ArrayList;
import java.util.List;

public class FutureProcessor {

    private final List<FutureRunnablePair> futureList;

    public FutureProcessor(){
        this.futureList = new ArrayList<>();
    }

    protected void tryProcessAll(){

        List<FutureRunnablePair> readyFutures = new ArrayList<>();
        for(FutureRunnablePair par : this.futureList){
            if(par.getFuture().isReady()){
                readyFutures.add(par);
            }
        }

        for(FutureRunnablePair p : readyFutures){
            p.getRunnable().run();
            p.getFuture().get();
            this.futureList.remove(p);
        }

    }
    public void addFutureProcess(Future condition, Runnable process){
        this.futureList.add(new FutureRunnablePair(condition, process));
    }

}

class FutureRunnablePair {
    Future future;
    Runnable runnable;
    FutureRunnablePair(Future future, Runnable runnable){
        this.future = future;
        this.runnable = runnable;
    }
    Future getFuture(){
        return future;
    }
    Runnable getRunnable(){
        return runnable;
    }
}