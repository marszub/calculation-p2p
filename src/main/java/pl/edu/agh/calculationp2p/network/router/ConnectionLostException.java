package pl.edu.agh.calculationp2p.network.router;

public class ConnectionLostException extends RuntimeException{
    public ConnectionLostException()
    {
        super("Connection Lost!");
    }
}
