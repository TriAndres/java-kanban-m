package ru.practicum.manage;

import ru.practicum.model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private final ArrayList<Task> taskArrayList;

    public InMemoryHistoryManager() {
        this.taskArrayList = new ArrayList<>();
    }

    @Override
    public void addHistory(Task task) {
        if (task != null) {
            taskArrayList.add(task);
            if (taskArrayList.size() > 10) {
                taskArrayList.removeFirst();
            }
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return taskArrayList;
    }
}