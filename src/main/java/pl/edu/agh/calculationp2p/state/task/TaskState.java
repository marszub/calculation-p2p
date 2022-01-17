package pl.edu.agh.calculationp2p.state.task;

public enum TaskState {
    Free(2),
    Reserved(1),
    Calculated(0);
    private final int value;

    TaskState(int i) {
        value = i;
    }

    public int getValue() {
        return value;
    }
}
