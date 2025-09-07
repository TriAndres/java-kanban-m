package ru.practicum.manage;

import org.junit.jupiter.api.Test;
import ru.practicum.model.Epic;
import ru.practicum.model.Subtask;
import ru.practicum.model.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.practicum.model.Status.*;

class InMemoryTaskManagerTest {
    private  final TaskManage taskManage = Managers.getDefault();

    @Test
    void testTask() {
        final long taskId = 1L;
        final long epicId = 2L;
        final long subtaskId = 3L;

        final Task task = new Task(taskId, "Test-Task", "Test-Task-description", NEW);
        final Epic epic = new Epic(epicId, "Test-Epic", "Test-Epic-description", NEW, new ArrayList<>(),task.getId());
        final Subtask subtask = new Subtask(subtaskId,"Test-Subtask", "Test-Subtask-description", NEW ,epic.getId());

        taskManage.createTask(task);
        taskManage.createEpic(epic);
        taskManage.createSubtask(subtask);

        final Task task1 = taskManage.getTaskById(taskId);
        final Epic epic1 = taskManage.getEpicById(epicId);
        final Subtask subtask1 = taskManage.getSubtaskById(subtaskId);

        assertEquals(task, task1);
        assertEquals(epic, epic1);
        assertEquals(subtask, subtask1);
        assertEquals(epic.getSubtasks().getFirst(), subtask1);

        /// //////////////////////////////////////////////////

        subtask1.setStatus(DONE);
        taskManage.updateSubtask(subtask1);

        Task task2 = taskManage.getTaskById(taskId);
        Epic epic2 = taskManage.getEpicById(epicId);
        Subtask subtask2 = taskManage.getSubtaskById(subtaskId);

        assertEquals(DONE, task2.getStatus(), "status DONE");
        assertEquals(DONE, epic2.getStatus(), "status DONE");
        assertEquals(DONE, subtask2.getStatus(), "status DONE");

        subtask2.setStatus(NEW);
        taskManage.updateSubtask(subtask2);

        Task task3 = taskManage.getTaskById(taskId);
        Epic epic3 = taskManage.getEpicById(epicId);
        Subtask subtask3 = taskManage.getSubtaskById(subtaskId);

        assertEquals(NEW, task3.getStatus(), "status NEW");
        assertEquals(NEW, epic3.getStatus(), "status NEW");
        assertEquals(NEW, subtask3.getStatus(), "status NEW");

        /// //////////////////////////////////////////////////

        final List<Task> taskList = taskManage.getTaskAll();
        final List<Epic> epicList = taskManage.getEpicAll();
        final List<Subtask> subtaskList = taskManage.getSubtaskAll();

        assertEquals(task, taskList.getFirst());
        assertEquals(epic, epicList.getFirst());
        assertEquals(subtask, subtaskList.getFirst());

        assertEquals(epic.getSubtasks().getFirst(), subtaskList.getFirst());
        assertEquals(epic.getSubtasks(), taskManage.getListSubtaskIdEpic(epicId));

        taskManage.deleteTaskById(taskId);
        taskManage.deleteEpicById(epicId);
        taskManage.deleteSubtaskById(epicId);


        assertNull(taskManage.getTaskById(taskId));
        assertNull(taskManage.getEpicById(epicId));
        assertNull(taskManage.getSubtaskById(subtaskId));


        assertEquals(new ArrayList<>(), taskManage.getTaskAll());
        assertEquals(new ArrayList<>(), taskManage.getEpicAll());
        assertEquals(new ArrayList<>(), taskManage.getSubtaskAll());
    }
}