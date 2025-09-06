package ru.practicum.manage;

import org.junit.jupiter.api.Test;
import ru.practicum.model.Status;
import ru.practicum.model.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class InMemoryHistoryManagerTest {
//    private final HistoryManager historyManager = Managers.getDefaultHistory();
//
//    @Test
//    void addHistory1() {
//        Task task = new Task(1L, "Test-Task", "Test-Task-description", Status.NEW);
//        historyManager.addHistory(task);
//        Integer size = 1;
//
//        final List<Task> taskList = historyManager.getHistory();
//        assertNotNull(taskList, "history not null");
//        assertEquals(task, taskList.getFirst(), "history true");
//        assertEquals(size, taskList.size(), "history size true");
//    }
//
//    @Test
//    void addHistoryMax10() {
//        int size = 15;
//        for (int i = 0; i < size; i++) {
//            historyManager.addHistory(new Task(
//                    Long.parseLong(String.valueOf(i)),
//                    "Test-Task",
//                    "Test-Task-description" + i,
//                    Status.NEW)
//            );
//        }
//        Integer sizeMax = 10;
//
//        final List<Task> taskList = historyManager.getHistory();
//        assertNotNull(taskList, "history not null");
//        assertEquals(sizeMax, taskList.size() , "history size true");
//    }
}