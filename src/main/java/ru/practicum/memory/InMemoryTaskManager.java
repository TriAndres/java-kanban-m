package ru.practicum.memory;

import ru.practicum.controller.Managers;
import ru.practicum.history.HistoryManager;
import ru.practicum.model.Epic;
import ru.practicum.model.Status;
import ru.practicum.model.Subtask;
import ru.practicum.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class InMemoryTaskManager implements TaskManage {
    private static long idCount;
    private final HashMap<Long, Task> taskMap = new HashMap<>();
    private final HashMap<Long, Subtask> subtaskMap = new HashMap<>();
    private final HashMap<Long, Epic> epicMap = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public List<Task> getTaskAll() {
        ArrayList<Task> list = new ArrayList<>();
        for (Long key : taskMap.keySet()) {
            Task task = taskMap.get(key);
            addHistory(task.getId());
            list.add(task);
        }
        return list;
    }

    @Override
    public List<Epic> getEpicAll() {
        ArrayList<Epic> list = new ArrayList<>();
        for (Long key : epicMap.keySet()) {
            Epic epic = epicMap.get(key);
            addHistory(epic.getId());
            list.add(epic);
        }
        return list;
    }

    @Override
    public List<Subtask> getSubtaskAll() {
        ArrayList<Subtask> list = new ArrayList<>();
        for (Long key : subtaskMap.keySet()) {
            Subtask subtask = subtaskMap.get(key);
            addHistory(subtask.getId());
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
        Long idEpic = subtask.getTaskId();
        if (epicMap.containsKey(idEpic)) {
            Epic epic = epicMap.get(idEpic);
            List<Long> list = epic.getSubtaskIdList();
            list.add(subtask.getId());
            epic.setSubtaskIdList(list);
            epicMap.put(epic.getId(), epic);
            statusEpic(epic);
        }
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
            Long idEpic = subtask.getTaskId();
            Epic epic = epicMap.get(idEpic);
            List<Long> list = epic.getSubtaskIdList();
            list.add(subtask.getId());
            epic.setSubtaskIdList(list);
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
            Task task = taskMap.get(id);
            addHistory(task.getId());
            return task;
        }
        System.out.println("task not id=" + id);
        return null;
    }

    @Override
    public Epic getEpicById(Long id) {
        if (epicMap.containsKey(id)) {
            Epic epic = epicMap.get(id);
            addHistory(epic.getId());
            return epic;
        } else {
            System.out.println("epic not id=" + id);
            return null;
        }
    }

    @Override
    public Subtask getSubtaskById(Long id) {
        if (subtaskMap.containsKey(id)) {
            Subtask subtask = subtaskMap.get(id);
            addHistory(subtask.getId());
            return subtask;
        }
        System.out.println("subtask not id=" + id);
        return null;
    }

    @Override
    public void deleteTaskById(Long id) {
        removeHistory(id);
        taskMap.remove(id);
    }

    @Override
    public void deleteEpicById(Long id) {
        if (epicMap.containsKey(id)) {
            Epic epic = epicMap.get(id);
            for (Long subtaskId : epic.getSubtaskIdList()) {
                if (epic.getId().equals(subtaskId)) {
                    removeHistory(subtaskId);
                    subtaskMap.remove(subtaskId);
                }
            }
            removeHistory(id);
            epicMap.remove(id);
            statusEpic(epic);
        }
    }

    @Override
    public void deleteSubtaskById(Long id) {
        if (subtaskMap.containsKey(id)) {
            if (epicMap.containsKey(subtaskMap.get(id).getTaskId())) {
                Epic epic = epicMap.get(subtaskMap.get(id).getTaskId());
                for (Long subtaskId : epic.getSubtaskIdList()) {
                    if (Objects.equals(subtaskId, id)) {
                        subtaskMap.remove(subtaskId);
                    }
                }
                statusEpic(epic);
            }

            removeHistory(id);
            subtaskMap.remove(id);
        }
    }

    @Override
    public void deleteTaskAll() {
        for (Long id : taskMap.keySet()) {
            removeHistory(id);
        }
        taskMap.clear();
    }

    @Override
    public void deleteEpicAll() {
        for (Long id : epicMap.keySet()) {
            removeHistory(id);
        }
        for (Long id : subtaskMap.keySet()) {
            removeHistory(id);
        }
        epicMap.clear();
        subtaskMap.clear();
    }

    @Override
    public void deleteSubtaskAll() {
        for (Epic epic : epicMap.values()) {
            epic.getSubtaskIdList().clear();
            statusEpic(epic);
        }
        for (Long id : subtaskMap.keySet()) {
            removeHistory(id);
        }
        subtaskMap.clear();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistoryMap();
    }

    @Override
    public void addHistory(long id) {
        if (epicMap.containsKey(id)) {
            historyManager.addHistory(epicMap.get(id));
        } else if (subtaskMap.containsKey(id)) {
            historyManager.addHistory(subtaskMap.get(id));
        } else if (taskMap.containsKey(id)) {
            historyManager.addHistory(taskMap.get(id));
        }
    }

    @Override
    public void removeHistory(long id) {
        historyManager.removeHistory(id);
    }

    @Override
    public List<Subtask> getListSubtaskIdEpic(Long id) {
        ArrayList<Subtask> subtaskArrayList = new ArrayList<>();
        if (epicMap.containsKey(id)) {
            Epic epic = epicMap.get(id);
            for (Long subtaskId : epic.getSubtaskIdList()) {
                subtaskArrayList.add(subtaskMap.get(subtaskId));
            }
            return subtaskArrayList;
        }
        System.out.println("epic not id=" + id);
        return subtaskArrayList;
    }


    private void statusEpic(Epic epic) {
        if (epicMap.containsKey(epic.getId())) {
            List<Subtask> list = getListSubtaskIdEpic(epic.getId());
            int countStatusNew = 0;
            int countStatusDone = 0;
            if (list == null) {
                epic.setStatus(Status.NEW);
                if (taskMap.containsKey(epic.getTaskId())) {
                    Task task = taskMap.get(epic.getTaskId());
                    task.setStatus(Status.NEW);
                }
            } else {
                for (Subtask subtask : list) {
                    if (subtask.getStatus().equals(Status.NEW)) {
                        countStatusNew++;
                        if (countStatusNew == list.size()) {
                            epic.setStatus(Status.NEW);
                            if (taskMap.containsKey(epic.getTaskId())) {
                                Task task = taskMap.get(epic.getTaskId());
                                task.setStatus(Status.NEW);
                            }
                        }
                    } else if (subtask.getStatus().equals(Status.DONE)) {

                        for (Subtask epicSubtask : list) {
                            countStatusDone++;
                            if (countStatusDone == list.size()) {
                                epic.setStatus(Status.DONE);
                                if (taskMap.containsKey(epic.getTaskId())) {
                                    Task task = taskMap.get(epic.getTaskId());
                                    task.setStatus(Status.DONE);
                                }
                            }
                        }
                    } else {
                        epic.setStatus(Status.IN_PROGRESS);
                        if (taskMap.containsKey(epic.getTaskId())) {
                            Task task = taskMap.get(epic.getTaskId());
                            task.setStatus(Status.IN_PROGRESS);
                        }
                    }
                }
            }
        }
    }

    private long getNextId() {
        return ++idCount;
    }
}