package pl.edu.agh.calculationp2p.network.router;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RoutingTableTest {
    @Test
    void checkIfInterfaceIsAddingItselfProperly()
    {
        RoutingTableImpl routingTable = new RoutingTableImpl();
        int id = 10;
        routingTable.addInterface(id);
        assertTrue(routingTable.getInterfaces().containsKey(id));
    }
}
