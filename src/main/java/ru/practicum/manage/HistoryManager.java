package ru.practicum.manage;

import ru.practicum.model.Task;

import java.util.ArrayList;

public interface HistoryManager {
    void addHistory(Task task);
    void removeHistory(long id);
    ArrayList<Task> getHistory();
}