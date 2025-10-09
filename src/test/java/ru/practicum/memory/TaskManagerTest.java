package ru.practicum.memory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.model.Epic;
import ru.practicum.model.Subtask;
import ru.practicum.model.Task;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.practicum.model.Status.*;
import static ru.practicum.model.TaskType.*;

public abstract class TaskManagerTest<T extends TaskManage> {
    protected T manager;
    protected Task task1;
    protected Epic epic1;
    protected Subtask subtask1, subtask15;

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
    void statusTaskEpicSubtask() {

        Task task = manager.createTask(
                new Task(
                        null,
                        TASK,
                        "Задача",
                        NEW,
                        "Test-Task-description",
                        "0",
                        "0",
                        "0",
                        null
                )
        );

        Epic epic = manager.createEpic(
                new Epic(
                        null,
                        EPIC,
                        "Эпик",
                        NEW,
                        "Test-Epic-description",
                        "0",
                        "0",
                        "0",
                        task.getId()
                )
        );

        task.setTaskId(epic.getId());

        Subtask subtask = manager.createSubtask(
                new Subtask(
                        null,
                        SUBTASK,
                        "Подзадача",
                        NEW,
                        "Test-Epic-description",
                        "0",
                        "0",
                        "0",
                        epic.getId()
                )
        );
        assertEquals(NEW, task.getStatus());
        assertEquals(NEW, epic.getStatus());
        assertEquals(NEW, subtask.getStatus());


        subtask.setStatus(DONE);
        manager.updateSubtask(subtask);

        Epic epic2 = manager.getEpicById(epic.getId());
        Task task2 = manager.getTaskById(task.getId());
        Subtask subtask2 = manager.getSubtaskById(subtask.getId());

        assertEquals(DONE, epic2.getStatus());
        assertEquals(DONE, task2.getStatus());
        assertEquals(DONE, subtask2.getStatus());

        Subtask subtask33 = manager.createSubtask(
                new Subtask(
                        null,
                        SUBTASK,
                        "Подзадача",
                        NEW,
                        "Test-Epic-description",
                        "0",
                        "0",
                        "0",
                        epic.getId()
                )
        );

        Task task3 = manager.getTaskById(task2.getId());
        Epic epic3 = manager.getEpicById(epic2.getId());
        Subtask subtask3 = manager.getSubtaskById(subtask2.getId());
        Subtask subtask4 = manager.getSubtaskById(subtask33.getId());

        assertEquals(IN_PROGRESS, task3.getStatus());
        assertEquals(IN_PROGRESS, epic3.getStatus());
        assertEquals(DONE, subtask3.getStatus());
        assertEquals(NEW, subtask4.getStatus());
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

    @Test
    void deleteByIdTaskEpicSubtask() {
        Task task = manager.createTask(task1);
        Epic epic = manager.createEpic(epic1);
        Subtask subtask = manager.createSubtask(subtask1);

        assertEquals(task1, task);
        assertEquals(epic1, epic);
        assertEquals(subtask1, subtask);

        manager.deleteTaskById(task.getId());
        manager.deleteEpicById(epic.getId());
        manager.deleteSubtaskById(subtask.getId());

        assertNull(manager.getTaskById(task.getId()));
        assertNull(manager.getEpicById(epic.getId()));
        assertNull(manager.getSubtaskById(subtask.getId()));
    }

    @Test
    void deleteAllTaskEpicSubtask() {
        Task task = manager.createTask(task1);
        Epic epic = manager.createEpic(epic1);
        Subtask subtask = manager.createSubtask(subtask1);

        assertEquals(task1, task);
        assertEquals(epic1, epic);
        assertEquals(subtask1, subtask);

        manager.deleteTaskAll();
        manager.deleteEpicAll();
        manager.deleteSubtaskAll();

        assertEquals(new ArrayList<>(), manager.getTaskAll());
        assertEquals(new ArrayList<>(),manager.getEpicAll());
        assertEquals(new ArrayList<>(),manager.getSubtaskAll());
    }

    @Test
    void updateTaskEpicSubtask() {
        Task task = manager.createTask(task1);
        Epic epic = manager.createEpic(epic1);
        Subtask subtask = manager.createSubtask(subtask1);

        task.setDescription("1111");
        epic.setDescription("2222");
        subtask.setDescription("3333");

        manager.updateTask(task);
        manager.updateEpic(epic);
        manager.updateSubtask(subtask);

        assertEquals(task, manager.getTaskById(task.getId()));
        assertEquals(epic, manager.getEpicById(epic.getId()));
        assertEquals(subtask, manager.getSubtaskById(subtask.getId()));
    }



}