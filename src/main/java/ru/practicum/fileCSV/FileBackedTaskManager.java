package ru.practicum.fileCSV;

import ru.practicum.exception.ManagerSaveException;
import ru.practicum.memory.TaskManage;
import ru.practicum.model.Epic;
import ru.practicum.model.Subtask;
import ru.practicum.model.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static ru.practicum.controller.Managers.getDefault;

public class FileBackedTaskManager implements TaskManage {
    private final TaskManage inMemory;
    private final File file;

    public FileBackedTaskManager(File file) {
        this.inMemory = getDefault();
        this.file = file;
    }

    public void save() {
        CSV csv = new CSV();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {

            writer.write("ID_TYPE,id,type,name,status,description,duration,startTime,endTime,taskId\n");

            if (!inMemory.getTaskAll().isEmpty()) {
                for (String task : inMemory.getTaskAll()) {
                    writer.write("ID_TASK,");
                    writer.write(csv.toString(task));
                }
            }

            if (!inMemory.getEpicAll().isEmpty()) {
                for (String epic : inMemory.getEpicAll()) {
                    writer.write("ID_EPIC,");
                    writer.write(csv.toString(epic));
                }
            }

            if (!inMemory.getSubtaskAll().isEmpty()) {
                for (String subtask : inMemory.getSubtaskAll()) {
                    writer.write("ID_SUBTASK,");
                    writer.write(csv.toString(subtask));
                }
            }

            writer.write("ID_HISTORY,");
            if (inMemory.getHistory() != null) {
                for (Task task : inMemory.getHistory()) {
                    writer.write(csv.historyToString(task));
                }
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при записи.");
        }
    }

    @Override
    public List<String> getTaskAll() {
        List<String> list = inMemory.getTaskAll();
        save();
        return list;
    }

    @Override
    public List<String> getEpicAll() {
        List<String> list = inMemory.getEpicAll();
        save();
        return list;
    }

    @Override
    public List<String> getSubtaskAll() {
        List<String> list = inMemory.getSubtaskAll();
        save();
        return list;
    }

    @Override
    public Task createTask(Task task) {
        Task task1 = inMemory.createTask(task);
        save();
        return task1;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic epic1 = inMemory.createEpic(epic);
        save();
        return epic1;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        Subtask subtask1 = inMemory.createSubtask(subtask);
        save();
        return subtask1;
    }

    @Override
    public void updateTask(Task task) {
        inMemory.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        inMemory.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        inMemory.updateSubtask(subtask);
        save();
    }

    @Override
    public Task getTaskById(Long id) {
        Task task = inMemory.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(Long id) {
        Epic epic = inMemory.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(Long id) {
        Subtask subtask = inMemory.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public void deleteTaskById(Long id) {
        inMemory.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(Long id) {
        inMemory.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(Long id) {
        inMemory.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteTaskAll() {
        inMemory.deleteTaskAll();
        save();
    }

    @Override
    public void deleteEpicAll() {
        inMemory.deleteEpicAll();
        save();
    }

    @Override
    public void deleteSubtaskAll() {
        inMemory.deleteSubtaskAll();
        save();
    }

    @Override
    public List<Subtask> getListSubtaskIdEpic(Long id) {
        List<Subtask> list = inMemory.getListSubtaskIdEpic(id);
        save();
        return list;
    }

    @Override
    public List<Task> getHistory() {
        List<Task> tasks = inMemory.getHistory();
        save();
        return tasks;
    }

    @Override
    public List<Task> getPrioritizedTaskList() {
        List<Task> tasks = inMemory.getPrioritizedTaskList();
        save();
        return  tasks;
    }

    @Override
    public boolean isPrioritizedValidation(Task task) {
        boolean is = inMemory.isPrioritizedValidation(task);
        save();
        return is;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        CSV csv = new CSV();
        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            while (reader.ready()) {
                csv.fromString(manager, reader.readLine());
            }
            if (reader.ready()) {
                for (long historyId : csv.historyFromString(reader.readLine())) {
                    manager.addHistory(historyId);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при Чтении.");
        }
        return manager;
    }
}