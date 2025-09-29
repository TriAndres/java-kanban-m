package ru.practicum.prioritized;

import ru.practicum.model.Task;

import java.util.List;

public interface TaskPrioritized {
    void prioritizedRemove(Task task);
    void prioritizedAdd(Task task);
    List<Task> getPrioritizedTaskList();
}