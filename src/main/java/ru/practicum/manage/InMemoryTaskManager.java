package ru.practicum.manage;

import ru.practicum.model.Epic;
import ru.practicum.model.Status;
import ru.practicum.model.Subtask;
import ru.practicum.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static ru.practicum.manage.Managers.getDefaultHistory;

public class InMemoryTaskManager implements TaskManage {
    private static long idCount = 1L;
    private final HashMap<Long, Task> taskMap = new HashMap<>();
    private final HashMap<Long, Subtask> subtaskMap = new HashMap<>();
    private final HashMap<Long, Epic> epicMap = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public List<Task> getTaskAll() {
        ArrayList<Task> list = new ArrayList<>();
        for (Long key : taskMap.keySet()) {
            Task task = taskMap.get(key);
            historyManager.addHistory(task);
            list.add(task);
        }
        return list;
    }

    @Override
    public List<Epic> getEpicAll() {
        ArrayList<Epic> list = new ArrayList<>();
        for (Long key : epicMap.keySet()) {
            Epic epic = epicMap.get(key);
            historyManager.addHistory(epic);
            list.add(epic);
        }
        return list;
    }

    @Override
    public List<Subtask> getSubtaskAll() {
        ArrayList<Subtask> list = new ArrayList<>();
        for (Long key : subtaskMap.keySet()) {
            Subtask subtask = subtaskMap.get(key);
            historyManager.addHistory(subtask);
            list.add(subtask);
        }
        return list;
    }

    @Override
    public Task createTask(Task task) {
        Long id = getNextId();
        task.setId(id);
        taskMap.put(id, task);
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Long id = getNextId();
        epic.setId(id);
        epicMap.put(id, epic);
        statusEpic(epic);
        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        Long id = getNextId();
        subtask.setId(id);
        subtaskMap.put(id, subtask);
        Long idEpic = subtask.getIdEpic();
        Epic epic = epicMap.get(idEpic);
        ArrayList<Subtask> list = (ArrayList<Subtask>) epic.getSubtasks();
        list.add(subtask);
        epic.setSubtasks(list);
        epicMap.put(epic.getId(), epic);
        statusEpic(epic);
        return subtask;
    }

    @Override
    public void updateTask(Task task) {
        if (taskMap.containsKey(task.getId())) {
            taskMap.put(task.getId(), task);
        } else {
            System.out.println("task not id=" + task.getId());
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epicMap.containsKey(epic.getId())) {
            statusEpic(epic);
            epicMap.put(epic.getId(), epic);
        } else {
            System.out.println("task not id=" + epic.getId());
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtaskMap.containsKey(subtask.getId())) {
            Long idEpic = subtask.getIdEpic();
            Epic epic = epicMap.get(idEpic);
            List<Subtask> list = epic.getSubtasks();
            list.add(subtask);
            epic.setSubtasks(list);
            epicMap.put(epic.getId(), epic);
            statusEpic(epic);
            subtaskMap.put(subtask.getId(), subtask);
        } else {
            System.out.println("task not id=" + subtask.getId());
        }
    }

    @Override
    public Task getTaskById(Long id) {
        if (taskMap.containsKey(id)) {
            historyManager.addHistory(getTaskById(id));
            return taskMap.get(id);
        }
        System.out.println("task not id=" + id);
        return null;
    }

    @Override
    public Epic getEpicById(Long id) {
        if (epicMap.containsKey(id)) {
            historyManager.addHistory(epicMap.get(id));
            return epicMap.get(id);
        } else {
            System.out.println("epic not id=" + id);
            return null;
        }
    }

    @Override
    public Subtask getSubtaskById(Long id) {
        if (subtaskMap.containsKey(id)) {
            historyManager.addHistory(subtaskMap.get(id));
            return subtaskMap.get(id);
        }
        System.out.println("subtask not id=" + id);
        return null;
    }

    @Override
    public void deleteTaskById(Long id) {
        historyManager.removeHistory(id);
        taskMap.remove(id);
    }

    @Override
    public void deleteEpicById(Long id) {
        if (epicMap.containsKey(id)) {
            Epic epic = epicMap.get(id);
            for (Subtask subtask : epic.getSubtasks()) {
                if (epic.getId().equals(subtask.getIdEpic())) {
                    historyManager.removeHistory(subtask.getId());
                    subtaskMap.remove(subtask.getId());
                }
            }
            historyManager.removeHistory(id);
            statusEpic(epic);
            epicMap.remove(id);
        }
    }

    @Override
    public void deleteSubtaskById(Long id) {
        if (subtaskMap.containsKey(id)) {
            Epic epic = epicMap.get(subtaskMap.get(id).getIdEpic());
            for (Subtask subtask : epic.getSubtasks()) {
                if (Objects.equals(subtask.getId(), id)) {
                    subtaskMap.remove(id);
                }
            }
            subtaskMap.remove(id);
            statusEpic(epic);
        }
    }

    @Override
    public void deleteTaskAll() {
        for (Long id : taskMap.keySet()) {
            historyManager.removeHistory(id);
        }
        taskMap.clear();
    }

    @Override
    public void deleteEpicAll() {
        for (Long id : epicMap.keySet()) {
            historyManager.removeHistory(id);
        }
        for (Long id : subtaskMap.keySet()) {
            historyManager.removeHistory(id);
        }
        epicMap.clear();
        subtaskMap.clear();
    }

    @Override
    public void deleteSubtaskAll() {
        for (Epic epic : epicMap.values()) {
            epic.getSubtasks().clear();
            statusEpic(epic);
        }
        for (Long id : subtaskMap.keySet()) {
            historyManager.removeHistory(id);
        }
        subtaskMap.clear();
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(historyManager.getHistory());
    }


    public List<Subtask> getListSubtaskIdEpic(Long id) {
        if (epicMap.containsKey(id)) {
            Epic epic = epicMap.get(id);
            return new ArrayList<>(epic.getSubtasks());
        }
        System.out.println("epic not id=" + id);
        return new ArrayList<>();
    }


    public void statusEpic(Epic epic) {
        if (epicMap.containsKey(epic.getId())) {
            List<Subtask> list = epic.getSubtasks();
            Task task = taskMap.get(epic.getIdTask());
            int countStatusNew = 0;
            int countStatusDone = 0;
            if (list == null) {
                epic.setStatus(Status.NEW);
                task.setStatus(Status.NEW);
            } else {
                for (Subtask subtask : list) {
                    if (subtask.getStatus().equals(Status.NEW)) {
                        countStatusNew++;
                        if (countStatusNew == list.size()) {
                            epic.setStatus(Status.NEW);
                            task.setStatus(Status.NEW);
                        }
                    } else if (subtask.getStatus().equals(Status.DONE)) {

                        for (Subtask epicSubtask : epic.getSubtasks()) {
                            countStatusDone++;
                            if (countStatusDone == epic.getSubtasks().size()) {
                                epic.setStatus(Status.DONE);
                                task.setStatus(Status.DONE);
                            }
                        }
                    } else {
                        epic.setStatus(Status.IN_PROGRESS);
                        task.setStatus(Status.IN_PROGRESS);
                    }
                }
            }

        }
    }

    private long getNextId() {
        return idCount++;
    }
}