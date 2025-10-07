package ru.practicum.history;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.controller.Managers;
import ru.practicum.model.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.practicum.model.Status.NEW;
import static ru.practicum.model.TaskType.TASK;

class InMemoryHistoryManagerTest {
    private  HistoryManager historyManager;

    @BeforeEach
    public void createTest() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void addHistory1() {
        Task task1 = new Task(
                1L,
                TASK,
                "Test-Task",
                NEW,
                "Test-Task-description",
                "0",
                "0",
                "0",
                0L);
        Task task2 = new Task(
                2L,
                TASK,
                "Test-Task",
                NEW,
                "Test-Task-description",
                "0",
                "0",
                "0",
                0L);

        historyManager.addHistory(task1);
        historyManager.addHistory(task2);
        Integer size = 2;

        final List<Task> taskList = historyManager.getHistoryMap();
        assertNotNull(taskList, "history not null");
        assertEquals(size, taskList.size(), "history size true");
    }

//    @Test
//    void notRepeat() {
//        Task task1 = new Task(1L, TASK,"Test-Task", NEW,"Test-Task-description",0L);
//        Task task2 = new Task(2L,  TASK,"Test-Task", NEW,"Test-Task-description",task1.getId());
//        Task task3 = new Task(3L, TASK,"Test-Task", NEW,"Test-Task-description",task2.getId());
//
//        historyManager.addHistory(task1);
//        historyManager.addHistory(task2);
//        historyManager.addHistory(task3);
//        historyManager.addHistory(task1);
//        historyManager.addHistory(task2);
//        historyManager.addHistory(task3);
//        Integer size = 3;
//
//        final List<Task> taskList = historyManager.getHistoryMap();
//        assertNotNull(taskList, "history not null");
//        assertEquals(size, taskList.size(), "history size true");
//    }
//
//    @Test
//    void orderAdd() {
//        Task task1 = new Task(1L, TASK,"Test-Task", NEW,"Test-Task-description",0L);
//        Task task2 = new Task(2L, TASK,"Test-Task", NEW,"Test-Task-description", task1.getId());
//        Task task3 = new Task(3L, TASK,"Test-Task", NEW,"Test-Task-description",task2.getId());
//
//        historyManager.addHistory(task1);
//        historyManager.addHistory(task2);
//        historyManager.addHistory(task3);
//
//        final List<Task> taskList = historyManager.getHistoryMap();
//        assertEquals(task1, taskList.get(0),"orderAdd");
//        assertEquals(task2, taskList.get(1),"orderAdd");
//        assertEquals(task3, taskList.get(2),"orderAdd");
//    }

}