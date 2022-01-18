package pl.edu.agh.calculationp2p.message.process;

import pl.edu.agh.calculationp2p.state.future.Future;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FutureProcessor {

    private final List<FutureRunnablePair> futureList;

    public FutureProcessor(){
        this.futureList = new ArrayList<>();
    }

    public void tryProcessAll(){

        List<FutureRunnablePair> readyFutures = futureList
                .stream()
                .filter(par -> par.getFuture().isReady())
                .collect(Collectors.toList());

        readyFutures.forEach(p -> {
            p.getRunnable().run();
            p.getFuture().get();
            this.futureList.remove(p);
        });

    }

    public void addFutureProcess(Future condition, Runnable process){
        futureList.add(new FutureRunnablePair(condition, process));
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