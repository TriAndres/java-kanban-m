package ru.practicum.manage;

import ru.practicum.model.Epic;
import ru.practicum.model.Subtask;
import ru.practicum.model.Task;

import java.util.List;

public interface TaskManage {
    List<Task> getTaskAll();
    List<Epic> getEpicAll();
    List<Subtask> getSubtaskAll();
    Task createTask(Task task);
    Epic createEpic(Epic epic);
    Subtask createSubtask(Subtask subtask);
    void updateTask(Task task);
    void updateEpic(Epic epic);
    void updateSubtask(Subtask subtask);
    Task getTaskById(Long id);
    Epic getEpicById(Long id);
    Subtask getSubtaskById(Long id);
    void deleteTaskById(Long id);
    void deleteEpicById(Long id);
    void deleteSubtaskById(Long id);
    void deleteTaskAll();
    void deleteEpicAll();
    void deleteSubtaskAll();
    List<Task> getHistory();

    List<Subtask> getListSubtaskIdEpic(Long id);
}