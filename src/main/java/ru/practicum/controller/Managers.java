package ru.practicum.controller;

import ru.practicum.fileCSV.FileBackedTaskManager;
import ru.practicum.history.HistoryManager;
import ru.practicum.history.inMemoryHistoryManager;
import ru.practicum.memory.InMemoryTaskManager;
import ru.practicum.memory.TaskManage;
import ru.practicum.prioritized.InMemoryTaskPrioritized;
import ru.practicum.prioritized.TaskPrioritized;

import java.io.File;
import java.util.Scanner;

import static ru.practicum.fileCSV.FileBackedTaskManager.loadFromFile;

public class Managers {
    public static File file = new File("src\\main\\java\\ru\\practicum\\fileCSV\\test.csv");
    public static TaskManage getDefault() {
        return new InMemoryTaskManager();
    }

    public static TaskManage detDefaultFile() {
        return loadFromFile(file);
    }

    public static HistoryManager getDefaultHistory() {
        return new inMemoryHistoryManager();
    }

    public static TaskPrioritized getDefaultTaskPrioritized() {
        return new InMemoryTaskPrioritized();
    }

    public static Scanner scanner(){
        return new Scanner(System.in);
    }
}