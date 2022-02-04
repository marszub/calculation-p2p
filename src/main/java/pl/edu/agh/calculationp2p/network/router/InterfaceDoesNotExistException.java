package pl.edu.agh.calculationp2p.network.router;

public class InterfaceDoesNotExistException extends RuntimeException{
    public InterfaceDoesNotExistException(int ID)
    {
        super("ID " + ID + " does not exist");
    }
}
