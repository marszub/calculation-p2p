package pl.edu.agh.calculationp2p.state.future;

public class Future<ResponseType> {
    private boolean ready;
    private ResponseType response;

    public Future(){
        ready = false;
        response = null;
    }

    public boolean isReady(){
        return ready;
    }

    public ResponseType get(){
        return response;
    }

    public void put(ResponseType response){
        this.response = response;
        ready = true;
    }
    public void put(){
        ready = true;
    }
}
