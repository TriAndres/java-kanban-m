package ru.practicum.manage;

public class Managers {
    public static TaskManage getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}