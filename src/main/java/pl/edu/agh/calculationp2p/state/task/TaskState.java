package pl.edu.agh.calculationp2p.state.task;

public enum TaskState {
    Free(2, "Free"),
    Reserved(1, "Reserved"),
    Calculated(0, "Calculated");

    private final int value;
    private final String name;

    TaskState(int i, String n) {
        value = i;
        name = n;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name;
    }

    public String firstLetter() {
        return name.substring(0, 1);
    }
}
