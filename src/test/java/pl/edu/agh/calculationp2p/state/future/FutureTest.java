package pl.edu.agh.calculationp2p.state.future;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FutureTest {

    @Test
    void shouldNotBeReadyOnCreate() {
        Future<Integer> future = new Future<>();
        assertFalse(future.isReady());
    }

    @Test
    void shouldBeReadyAfterPut(){
        Future<Integer> future = new Future<>();
        future.put(0);
        assertTrue(future.isReady());
    }

    @Test
    void shouldReturnPutValue(){
        Future<Integer> future = new Future<>();
        Integer resource = 354;
        future.put(resource);
        assertEquals(future.get(), resource);
    }
}