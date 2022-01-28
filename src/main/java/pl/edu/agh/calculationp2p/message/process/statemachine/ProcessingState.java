package pl.edu.agh.calculationp2p.message.process.statemachine;

import pl.edu.agh.calculationp2p.message.process.MessageProcessor;

/**
 * A state in MessageProcessor state machine.
 */
public interface ProcessingState {
    /**
     * Sets context for proceed() method. MessageProcessor must be set before the first proceed() call.
     * @param messageProcessor a context used in proceed() method
     */
    void setContext(MessageProcessor messageProcessor);

    /**
     * Performs state actions and can change state of the context
     */
    void proceed() throws InterruptedException;
}
