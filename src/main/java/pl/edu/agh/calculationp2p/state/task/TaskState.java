package pl.edu.agh.calculationp2p.state.task;

public enum TaskState {
    Free(2, "free"),
    Reserved(1, "reserved"),
    Calculated(0, "calculated");

    private final int value;
    private final String name;

    TaskState(int i, String n) {
        value = i;
        name = n;
    }

    public int getValue() {
        return value;
    }
    public String getName() {
        return name;
    }
}