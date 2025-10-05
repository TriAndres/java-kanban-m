package ru.practicum.memory;

import ru.practicum.model.Epic;
import ru.practicum.model.Subtask;
import ru.practicum.model.Task;

import java.util.List;

public interface TaskManage {
    List<String> getTaskAll();

    List<String> getEpicAll();

    List<String> getSubtaskAll();

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

    List<Subtask> getListSubtaskIdEpic(Long id);

    List<Task> getHistory();

    default void addHistory(long id) {
    }

    default void removeHistory(long id) {
    }

    default void statusEpic(Epic epic) {
    }

    default void prioritizedRemove(Task task) {

    }

    default void prioritizedAdd(Task task) {

    }

    List<Task> getPrioritizedTaskList();

     default boolean isPrioritizedValidation(Task task) {
         return false;
     }
}