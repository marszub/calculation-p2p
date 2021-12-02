package pl.edu.agh.calculationp2p.network.router;

public class InterfaceExistsException extends RuntimeException{
    public InterfaceExistsException(int ID)
    {
        super("ID " + String.valueOf(ID) + " already exists!");
    }
}
