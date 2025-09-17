package ru.practicum.controller;

import ru.practicum.fileCSV.FileBackedTaskManager;
import ru.practicum.memory.TaskManage;
import ru.practicum.history.HistoryManager;
import ru.practicum.history.InMemoryHistoryManager;
import ru.practicum.memory.InMemoryTaskManager;

import java.io.File;

public class Managers {
    public static File file = new File("src\\main\\java\\ru\\practicum\\fileCSV\\test.csv");
    public static TaskManage getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManage detDefaultFile() {
        return new FileBackedTaskManager(file);
    }
}