package pl.edu.agh.calculationp2p.state;

import pl.edu.agh.calculationp2p.state.task.TaskRecord;

public interface Publisher {
    void look(Thread changing, TaskRecord previous, TaskRecord current);
}
