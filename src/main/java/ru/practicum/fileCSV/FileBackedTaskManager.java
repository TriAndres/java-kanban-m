package ru.practicum.fileCSV;

import ru.practicum.exception.ManagerSaveException;
import ru.practicum.memory.InMemoryTaskManager;
import ru.practicum.model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    public final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public void save() {
        CSV csv = new CSV();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {

            writer.write("ID_TYPE,id,type,name,status,description,taskId\n");

            for (Task task : super.getTaskAll()) {
                if (task != null) {
                    writer.write("ID_TASK,");
                    writer.write(csv.toString(task));
                }
            }

            for (Epic epic : super.getEpicAll()) {
                if (epic != null) {
                    writer.write("ID_EPIC,");
                    writer.write(csv.toString(epic));
                }
            }

            for (Subtask subtask : super.getSubtaskAll()) {
                if (subtask != null) {
                    writer.write("ID_SUBTASK,");
                    writer.write(csv.toString(subtask));
                }
            }
            writer.write("ID_HISTORY,");
            for (Task taskHistory : super.getHistory()) {
                if (taskHistory != null) {
                    writer.write(csv.historyToString(String.valueOf(taskHistory.getId())));
                }
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при записи.");
        }
    }

    @Override
    public List<Task> getTaskAll() {
        List<Task> list = super.getTaskAll();
        save();
        return list;
    }

    @Override
    public List<Epic> getEpicAll() {
        List<Epic> list = super.getEpicAll();
        save();
        return list;
    }

    @Override
    public List<Subtask> getSubtaskAll() {
        List<Subtask> list = super.getSubtaskAll();
        save();
        return list;
    }

    @Override
    public Task createTask(Task task) {
        Task task1 = super.createTask(task);
        save();
        return task1;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic epic1 = super.createEpic(epic);
        save();
        return epic1;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        Subtask subtask1 = super.createSubtask(subtask);
        save();
        return subtask1;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public Task getTaskById(Long id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(Long id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(Long id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public void deleteTaskById(Long id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(Long id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(Long id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteTaskAll() {
        super.deleteTaskAll();
        save();
    }

    @Override
    public void deleteEpicAll() {
        super.deleteEpicAll();
        save();
    }

    @Override
    public void deleteSubtaskAll() {
        super.deleteSubtaskAll();
        save();
    }

    @Override
    public List<Subtask> getListSubtaskIdEpic(Long id) {
        List<Subtask> list = super.getListSubtaskIdEpic(id);
        save();
        return list;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        CSV csv = new CSV();
        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            while (reader.ready()) {
                csv.fromString(manager,reader.readLine());
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