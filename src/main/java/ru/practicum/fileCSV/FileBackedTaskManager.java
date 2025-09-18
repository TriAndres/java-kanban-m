package ru.practicum.fileCSV;

import ru.practicum.exception.ManagerSaveException;
import ru.practicum.memory.InMemoryTaskManager;
import ru.practicum.model.Epic;
import ru.practicum.model.Subtask;
import ru.practicum.model.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

public class FileBackedTaskManager extends InMemoryTaskManager {
    public final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public void save() {
        final String FIRST_LINE = "id,type,name,status,description,epic\n";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            writer.write(FIRST_LINE);
            for (Task task : super.getTaskAll()) {
                if (task != null) {
                    writer.write(Objects.requireNonNull(CSV.toString(task)));
                }
            }
            for (Epic epic : super.getEpicAll()) {
                if (epic != null) {
                    writer.write(Objects.requireNonNull(CSV.toString(epic)));
                }
            }
            for (Subtask subtask : super.getSubtaskAll()) {
                if (subtask != null) {
                    writer.write(Objects.requireNonNull(CSV.toString(subtask)));
                }
            }
            writer.write(CSV.historyToString(super.getHistory()));
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

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        CSV csv = new CSV();
        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))){
            while (reader.ready()) {
                String line = reader.readLine();
                Task task = csv.fromString(line);
                if (task instanceof Epic epic) {
                    manager.updateEpic(epic);
                }
                if (task instanceof Subtask subtask) {
                    manager.updateSubtask(subtask);
                }
                if (task instanceof Task task1) {
                    manager.createTask(task1);
                }
            }
            String lineWithHistory = reader.readLine();
            for (int id : Objects.requireNonNull(CSV.historyFromString(lineWithHistory))) {
                manager.addHistory(id);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при Чтении.");
        }
        return manager;
    }
}