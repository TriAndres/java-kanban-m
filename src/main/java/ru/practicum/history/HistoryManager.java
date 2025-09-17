package ru.practicum.history;

import ru.practicum.model.Task;

import java.util.List;

public interface HistoryManager {
    void addHistory(Task task);
    void removeHistory(long id);
    List<Task> getHistoryMap();
}