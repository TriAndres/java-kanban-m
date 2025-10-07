package ru.practicum.memory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.model.Epic;
import ru.practicum.model.Subtask;
import ru.practicum.model.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.practicum.model.Status.NEW;
import static ru.practicum.model.TaskType.*;

public abstract class TaskManagerTest<T extends TaskManage> {
    protected T manager;
    protected Task task1, task2;
    protected Epic epic1, epic2;
    protected Subtask subtask1, subtask2;

    @BeforeEach
    void setUp() {
        task1 = new Task(
                1L,
                TASK,
                "Задача",
                NEW,
                "Test-Task-description",
                "0",
                "0",
                "0",
                2L
        );

        epic1 = new Epic(
                        2L,
                        EPIC,
                        "Эпик",
                        NEW,
                "Test-Epic-description",
                        "0",
                        "0",
                        "0",
                1L
        );

        subtask1 = new Subtask(
                3L,
                SUBTASK,
                "Подзадача",
                NEW,
                "Test-Epic-description",
                "0",
                "0",
                "0",
                2L
        );
    }

    @Test
    void createTaskEpicSubtaskTest() {
        Task task = manager.createTask(task1);
        Epic epic = manager.createEpic(epic1);
        Subtask subtask = manager.createSubtask(subtask1);

        Task task0 = manager.getTaskById(task.getId());
        Epic epic0 = manager.getEpicById(epic.getId());
        Subtask subtask0 = manager.getSubtaskById(subtask.getId());

        assertEquals(task1, task0);
        assertEquals(epic1, epic0);
        assertEquals(subtask1, subtask0);
    }

}
