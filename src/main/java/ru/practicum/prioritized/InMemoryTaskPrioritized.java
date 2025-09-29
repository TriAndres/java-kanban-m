package ru.practicum.prioritized;

import ru.practicum.model.Task;

import java.util.*;

public class InMemoryTaskPrioritized implements TaskPrioritized {
    private final Set<Task> prioritized = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    @Override
    public void prioritizedRemove(Task task) {
        prioritized.removeIf(oldTask -> Objects.equals(oldTask.getId(), task.getId()));
    }

    @Override
    public void prioritizedAdd(Task task) {
        if (task != null) {
            prioritized.add(task);
        }
    }

    @Override
    public List<Task> getPrioritizedTaskList() {
        return List.copyOf(prioritized);
    }
}
