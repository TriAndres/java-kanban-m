package ru.practicum.manage;

import org.junit.jupiter.api.Test;
import ru.practicum.model.Epic;
import ru.practicum.model.Subtask;
import ru.practicum.model.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.model.Status.*;

class InMemoryTaskManagerTest {
    private final TaskManage taskManage = Managers.getDefault();

    @Test
    void createTaskEpicSubtask() {
        long taskId = 1L;
        long epicId = 1L;
        long subtaskId = 1L;

        Task task = new Task(taskId, "Test-Task", "Test-Task-description", NEW);
        Epic epic = new Epic(epicId, "Test-Epic", "Test-Epic-description", NEW, new ArrayList<>());
        Subtask subtask = new Subtask(subtaskId,"Test-Subtask", "Test-Subtask-description", NEW ,epic.getId());

        taskManage.createTask(task);
        taskManage.createEpic(epic);
        taskManage.createSubtask(subtask);

        Task task1 = taskManage.getTaskById(taskId);
        Epic epic1 = taskManage.getEpicById(epicId);
        Subtask subtask1 = taskManage.getSubtaskById(subtaskId);

        assertEquals(task, task1);
        assertEquals(epic, epic1);
        assertEquals(epic.getSubtasks().getFirst(), subtask1);
        assertEquals(subtask, subtask1);

        final List<Task> taskList = taskManage.getTaskAll();
        final List<Epic> epicList = taskManage.getEpicAll();
        final List<Subtask> subtaskList = taskManage.getSubtaskAll();

        assertEquals(task, taskList.getFirst());
        assertEquals(epic, epicList.getFirst());
        assertEquals(epic.getSubtasks(), subtaskList);
        assertEquals(epic.getSubtasks(), taskManage.getListSubtaskIdEpic(epic1.getId()));
        assertEquals(subtask, subtaskList.getFirst());
    }

    @Test
    void deleteTaskEpicSubtask() {
        long taskId = 1L;
        long epicId = 1L;
        long subtaskId = 1L;

        Task task = new Task(taskId, "Test-Task", "Test-Task-description", NEW);
        Epic epic = new Epic(epicId, "Test-Epic", "Test-Epic-description", NEW, new ArrayList<>());
        Subtask subtask = new Subtask(subtaskId,"Test-Subtask", "Test-Subtask-description", NEW ,epic.getId());

        taskManage.createTask(task);
        taskManage.createEpic(epic);
        taskManage.createSubtask(subtask);

        Task task1 = taskManage.getTaskById(taskId);
        Epic epic1 = taskManage.getEpicById(epicId);
        Subtask subtask1 = taskManage.getSubtaskById(subtaskId);

        final List<Task> taskList = taskManage.getTaskAll();
        final List<Epic> epicList = taskManage.getEpicAll();
        final List<Subtask> subtaskList = taskManage.getSubtaskAll();

        assertEquals(task, taskList.getFirst());
        assertEquals(epic, epicList.getFirst());
        assertEquals(epic.getSubtasks().getFirst(), subtaskList.getFirst());
        assertEquals(epic.getSubtasks(), taskManage.getListSubtaskIdEpic(epic1.getId()));
        assertEquals(subtask, subtaskList.getFirst());

        taskManage.deleteTaskById(taskId);
        //taskManage.deleteEpicById(epicId);
        //taskManage.deleteSubtaskById(epicId);
        //taskManage.deleteTaskAll();
        taskManage.deleteEpicAll();
        taskManage.deleteSubtaskAll();



        assertNull(taskManage.getTaskById(taskId));
        assertNull(taskManage.getEpicById(epicId));
        assertNull(taskManage.getSubtaskById(subtaskId));
       //assertNull(epicList.getFirst());
        //assertNull(epicList.getFirst().getSubtasks().getFirst());
    }

    @Test
    void statusTaskEpicSubtask() {
        long taskId = 1L;
        long epicId = 1L;
        long subtaskId = 1L;

        Task task = new Task(taskId, "Test-Task", "Test-Task-description", NEW);
        Epic epic = new Epic(epicId, "Test-Epic", "Test-Epic-description", NEW, new ArrayList<>());
        Subtask subtask = new Subtask(subtaskId,"Test-Subtask", "Test-Subtask-description", NEW ,epic.getId());

        taskManage.createTask(task);
        taskManage.createEpic(epic);
        taskManage.createSubtask(subtask);

        Task task1 = taskManage.getTaskById(taskId);
        Epic epic1 = taskManage.getEpicById(epicId);
        Subtask subtask1 = taskManage.getSubtaskById(subtaskId);

        assertEquals(task.getStatus(), task1.getStatus());
        assertEquals(epic.getStatus(), epic1.getStatus());
        assertEquals(subtask.getStatus(), subtask1.getStatus());

        subtask1.setStatus(DONE);
        taskManage.updateSubtask(subtask1);

        Task task2 = taskManage.getTaskById(taskId);
        Epic epic2 = taskManage.getEpicById(epicId);
        Subtask subtask2 = taskManage.getSubtaskById(subtaskId);

        assertEquals(DONE, task2.getStatus());
        assertEquals(DONE, epic2.getStatus());
        assertEquals(DONE, subtask2.getStatus());

        subtask2.setStatus(NEW);
        taskManage.updateSubtask(subtask2);

        Task task3 = taskManage.getTaskById(taskId);
        Epic epic3 = taskManage.getEpicById(epicId);
        Subtask subtask3 = taskManage.getSubtaskById(subtaskId);

        assertEquals(NEW, task3.getStatus());
        assertEquals(NEW, epic3.getStatus());
        assertEquals(NEW, subtask3.getStatus());
    }
}