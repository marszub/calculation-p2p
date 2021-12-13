package pl.edu.agh.calculationp2p.state.idle;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class IdleTest {
    @Test
    void wakesUpAfterTime(){
        Idle idle = new Idle();
        AtomicReference<Boolean> wasWoken = new AtomicReference<>(false);
        Thread sleeping = new Thread(() -> {
            try {
                idle.sleep(40);
                wasWoken.set(true);
            } catch (InterruptedException e) {
                throw new Error("Sleep interrupted");
            }
        });
        sleeping.start();

        assertDoesNotThrow(() -> sleeping.join(50));
        assertTrue(wasWoken.get());
    }

    @Test
    void wakesUpNotBeforeTime(){
        Idle idle = new Idle();
        AtomicReference<Boolean> wasWoken = new AtomicReference<>(false);
        Thread sleeping = new Thread(() -> {
            try {
                idle.sleep(50);
                wasWoken.set(true);
            } catch (InterruptedException e) {
                throw new Error("Sleep interrupted");
            }
        });
        sleeping.start();

        assertDoesNotThrow(() -> TimeUnit.MILLISECONDS.sleep(40));
        assertFalse(wasWoken.get());
    }

    @Test
    void wakesUpByWake(){
        Idle idle = new Idle();
        AtomicReference<Boolean> wasWoken = new AtomicReference<>(false);
        Thread sleeping = new Thread(() -> {
            try {
                idle.sleep();
                wasWoken.set(true);
            } catch (InterruptedException e) {
                throw new Error("Sleep interrupted");
            }
        });
        sleeping.start();
        idle.wake();

        assertDoesNotThrow(() -> sleeping.join(50));
        assertTrue(wasWoken.get());
    }

    @Test
    void wakesUpByWakeBeforeTime(){
        Idle idle = new Idle();
        AtomicReference<Boolean> wasWoken = new AtomicReference<>(false);
        Thread sleeping = new Thread(() -> {
            try {
                idle.sleep(200);
                wasWoken.set(true);
            } catch (InterruptedException e) {
                throw new Error("Sleep interrupted");
            }
        });
        sleeping.start();
        idle.wake();

        assertDoesNotThrow(() -> TimeUnit.MILLISECONDS.sleep(5));
        assertTrue(wasWoken.get());
    }
}