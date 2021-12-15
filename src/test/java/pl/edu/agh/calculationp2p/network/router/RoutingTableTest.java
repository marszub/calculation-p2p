package pl.edu.agh.calculationp2p.network.router;
import org.junit.jupiter.api.Test;
import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.network.utilities.DummyConnection;
import pl.edu.agh.calculationp2p.network.utilities.DummyMessage;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RoutingTableTest {
    @Test
    void checkIfInterfaceIsAddingItselfProperly()
    {
        RoutingTableImpl routingTable = new RoutingTableImpl();
        int id = 10;
        routingTable.addInterface(id);
        assertTrue(routingTable.interfaceListContains(id));
    }

    @Test
    void checkIfInterfacesDeletesProperly()
    {
        RoutingTableImpl routingTable = new RoutingTableImpl();
        int id = 10;
        routingTable.addInterface(id);
        routingTable.removeInterface(id);
        assertFalse(routingTable.interfaceListContains(id));
    }

    @Test
    void checkIfDummyConnectionTrySendsMessage()
    {
        RoutingTableImpl routingTable = new RoutingTableImpl();
        int id = 10;
        routingTable.addInterface(id);
        DummyConnection connection = new DummyConnection(true);
        routingTable.bind(id, connection);
        assertTrue(routingTable.trySend(id, new DummyMessage()));
        assertFalse(routingTable.trySend(id, new DummyMessage()));
    }

    @Test
    void checkIfMessageIsSent()
    {
        RoutingTableImpl routingTable = new RoutingTableImpl();
        int id = 10;
        Message notPassing = new DummyMessage();
        List<Message> list = new LinkedList<>();
        list.add(notPassing);
        addIdAndMakeMessageNotToBeSent(new DummyConnection(true), routingTable, id, list);

    }

    @Test
    void checkIfResendAllResendsAll()
    {
        RoutingTableImpl routingTable = new RoutingTableImpl();
        int id = 10;
        Message notPassing1 = new DummyMessage();
        Message notPassing2 = new DummyMessage();
        List<Message> list = new LinkedList<>();
        list.add(notPassing1);
        list.add(notPassing2);
        DummyConnection connection = new DummyConnection(true);
        addIdAndMakeMessageNotToBeSent(connection, routingTable, id, list);
        routingTable.resendAll();
        assertEquals(3 , connection.getList().size());
        assertEquals(notPassing1 , connection.getLastMessage());
    }

    @Test
    void checkIfResendAllResendsAllManyDummyConnections()
    {
        RoutingTableImpl routingTable = new RoutingTableImpl();
        int id1 = 10;
        int id2 = 40;
        Message notPassing1 = new DummyMessage();
        Message notPassing2 = new DummyMessage();
        Message notPassing3 = new DummyMessage();
        Message notPassing4= new DummyMessage();
        List<Message> list = new LinkedList<>();
        List<Message> list2 = new LinkedList<>();
        list.add(notPassing1);
        list.add(notPassing2);
        list2.add(notPassing3);
        list2.add(notPassing4);
        DummyConnection con1 = new DummyConnection(true);
        DummyConnection con2 = new DummyConnection(true);
        addIdAndMakeMessageNotToBeSent(con1, routingTable, id1, list);
        addIdAndMakeMessageNotToBeSent(con2, routingTable, id2, list2);
        routingTable.resendAll();
        assertEquals(3 , con1.getList().size());
        assertEquals(notPassing1 , con1.getLastMessage());
        assertEquals(3 , con2.getList().size());
        assertEquals(notPassing3 , con2.getLastMessage());
    }

    @Test
    void checkIfResendAllResendsAllManyMessages()
    {
        RoutingTableImpl routingTable = new RoutingTableImpl();
        int id1 = 10;
        Message notPassing1 = new DummyMessage();
        Message notPassing2 = new DummyMessage();
        Message notPassing3 = new DummyMessage();
        Message notPassing4= new DummyMessage();
        List<Message> list = new LinkedList<>();
        list.add(notPassing1);
        list.add(notPassing2);
        list.add(notPassing3);
        list.add(notPassing4);
        DummyConnection connection = new DummyConnection(true);
        addIdAndMakeMessageNotToBeSent(connection, routingTable, id1, list);
        routingTable.resendAll();
        connection.setResult(true);
        routingTable.resendAll();
        connection.setResult(true);
        routingTable.resendAll();
        assertEquals(7, connection.getList().size());
        assertEquals(notPassing2, connection.getList().removeLast());
        assertEquals(notPassing3, connection.getList().removeLast());
        assertEquals(notPassing1, connection.getList().removeLast());
    }

    @Test
    void checkDeletionOfNonExistingInterface()
    {
        RoutingTableImpl routingTable = new RoutingTableImpl();
        assertThrows(InterfaceDoesNotExistException.class, () -> {
            routingTable.removeInterface(1);
        });
    }

    @Test
    void checkSendingMessageToNonExistingInterface()
    {
        RoutingTableImpl routingTable = new RoutingTableImpl();
        assertThrows(InterfaceDoesNotExistException.class, () -> {
            routingTable.send(1, new DummyMessage());
        });
    }

    @Test
    void checkTrySendingMessageToNonExistingInterface()
    {
        RoutingTableImpl routingTable = new RoutingTableImpl();
        assertThrows(InterfaceDoesNotExistException.class, () -> {
            routingTable.trySend(1, new DummyMessage());
        });
    }

    @Test
    void checkBindingOfNonExistingInterface()
    {
        RoutingTableImpl routingTable = new RoutingTableImpl();
        assertThrows(InterfaceDoesNotExistException.class, () -> {
            routingTable.bind(1, new DummyConnection(false));
        });
    }

    @Test
    void checkCreationOfExistingInterface()
    {
        RoutingTableImpl routingTable = new RoutingTableImpl();
        routingTable.addInterface(1);
        assertThrows(InterfaceExistsException.class, () -> {
            routingTable.addInterface(1);
        });
    }

    @Test
    void checkInterfaceListContains()
    {
        RoutingTableImpl routingTable = new RoutingTableImpl();
        routingTable.addInterface(1);
        assertTrue(routingTable.interfaceListContains(1));
    }

    private void addIdAndMakeMessageNotToBeSent(DummyConnection connection, RoutingTableImpl routingTable, int id, List<Message> messageList)
    {
        routingTable.addInterface(id);
        routingTable.bind(id, connection);
        for(Message message : messageList)
        {
            routingTable.send(id, new DummyMessage());
            routingTable.send(id, message);
        }
    }
}
