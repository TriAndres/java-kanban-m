package ru.practicum.manage;

import org.junit.jupiter.api.Test;
import ru.practicum.model.Status;
import ru.practicum.model.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Test
    void addHistory1() {
        Task task1 = new Task(1L, "Test-Task", "Test-Task-description", Status.NEW);
        Task task2 = new Task(2L, "Test-Task", "Test-Task-description", Status.NEW);

        historyManager.addHistory(task1);
        historyManager.addHistory(task2);
        Integer size = 2;

        final List<Task> taskList = historyManager.getHistory();
        assertNotNull(taskList, "history not null");
        assertEquals(task1, taskList.getFirst(), "history true");
        assertEquals(task2, taskList.getLast(), "history true");
        assertEquals(size, taskList.size(), "history size true");
    }

    @Test
    void notRepeat() {
        Task task1 = new Task(1L, "Test-Task", "Test-Task-description", Status.NEW);
        Task task2 = new Task(2L, "Test-Task", "Test-Task-description", Status.NEW);
        Task task3 = new Task(3L, "Test-Task", "Test-Task-description", Status.NEW);

        historyManager.addHistory(task1);
        historyManager.addHistory(task2);
        historyManager.addHistory(task3);
        historyManager.addHistory(task1);
        historyManager.addHistory(task2);
        historyManager.addHistory(task3);
        Integer size = 3;

        final List<Task> taskList = historyManager.getHistory();
        assertNotNull(taskList, "history not null");
        assertEquals(size, taskList.size(), "history size true");
    }

    @Test
    void orderAdd() {
        Task task1 = new Task(1L, "Test-Task", "Test-Task-description", Status.NEW);
        Task task2 = new Task(2L, "Test-Task", "Test-Task-description", Status.NEW);
        Task task3 = new Task(3L, "Test-Task", "Test-Task-description", Status.NEW);

        historyManager.addHistory(task1);
        historyManager.addHistory(task2);
        historyManager.addHistory(task3);

        final List<Task> taskList = historyManager.getHistory();
        assertEquals(task1, taskList.get(0),"orderAdd");
        assertEquals(task2, taskList.get(1),"orderAdd");
        assertEquals(task3, taskList.get(2),"orderAdd");
    }

}