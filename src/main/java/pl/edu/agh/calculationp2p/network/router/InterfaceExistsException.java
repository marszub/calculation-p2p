package pl.edu.agh.calculationp2p.network.router;

public class InterfaceExistsException extends RuntimeException{
    public InterfaceExistsException(int ID)
    {
        super("ID " + ID + " already exists!");
    }
}
