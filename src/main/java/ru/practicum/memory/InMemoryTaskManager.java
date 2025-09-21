package ru.practicum.memory;

import ru.practicum.controller.Managers;
import ru.practicum.exception.ElementNot;
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
    private final  HashMap<Long, Task> taskMap = new HashMap<>();
    private final HashMap<Long, Subtask> subtaskMap = new HashMap<>();
    private final HashMap<Long, Epic> epicMap = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public List<Task> getTaskAll() {
        ArrayList<Task> list = new ArrayList<>();
        if (!taskMap.isEmpty()) {
            for (Long key : taskMap.keySet()) {
                Task task = taskMap.get(key);
                list.add(task);
                addHistory(task.getId());
            }
        }
        return list;
    }

    @Override
    public List<Epic> getEpicAll() {
        ArrayList<Epic> list = new ArrayList<>();
        if (!epicMap.isEmpty()) {
            for (Long key : epicMap.keySet()) {
                Epic epic = epicMap.get(key);
                list.add(epic);
                addHistory(epic.getId());
            }
        }
        return list;
    }

    @Override
    public List<Subtask> getSubtaskAll() {
        ArrayList<Subtask> list = new ArrayList<>();
        if (!subtaskMap.isEmpty()) {
            for (Long key : subtaskMap.keySet()) {
                Subtask subtask = subtaskMap.get(key);
                list.add(subtask);
                addHistory(subtask.getId());
            }
        }
        return list;
    }

    @Override
    public Task createTask(Task task) {
        if (task != null) {
            Long id = getNextId();
            task.setId(id);
            taskMap.put(task.getId(), task);
            return task;
        }
        throw new ElementNot("-> createTask(Task task)");
    }

    @Override
    public Epic createEpic(Epic epic) {
        if (epic != null) {
            Long id = getNextId();
            epic.setId(id);
            epicMap.put(epic.getId(), epic);
            statusEpic(epic);
            return epic;
        }
        throw new ElementNot("-> createEpic(Epic epic)");
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        if (subtask != null) {
            Long id = getNextId();
            subtask.setId(id);
            subtaskMap.put(subtask.getId(), subtask);
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
        throw new ElementNot("-> createSubtask(Subtask subtask)");
    }

    @Override
    public Task updateTask(Task task) {
        if (task != null) {
            if (!taskMap.containsKey(task.getId())) {
                Task oldTask = taskMap.get(task.getId());
                taskMap.put(task.getId(), task);
                removeHistory(oldTask.getId());
                addHistory(task.getId());
                return task;
            }
        }
        throw new ElementNot("-> updateTask(Task task)");
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic != null) {
            if (epicMap.containsKey(epic.getId())) {
                Epic oldEpic = epicMap.get(epic.getId());
                epicMap.put(epic.getId(), epic);
                removeHistory(oldEpic.getId());
                addHistory(epic.getId());
                statusEpic(epic);
            } else {
                //System.out.println("task not id=" + epic.getId());
            }
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask != null) {
            if (subtaskMap.containsKey(subtask.getId())) {
                Subtask oldSubtask = subtaskMap.get(subtask.getId());
                removeHistory(oldSubtask.getId());
                addHistory(subtask.getId());
                Long idEpic = subtask.getTaskId();
                Epic epic = epicMap.get(idEpic);
                List<Long> list = epic.getSubtaskIdList();
                list.add(subtask.getId());
                epic.setSubtaskIdList(list);
                epicMap.put(epic.getId(), epic);
                subtaskMap.put(subtask.getId(), subtask);
                statusEpic(epic);
            } else {
                //System.out.println("task not id=" + subtask.getId());
            }
        }
    }

    @Override
    public Task getTaskById(Long id) {
        if (taskMap.containsKey(id)) {
            Task task = taskMap.get(id);
            addHistory(task.getId());
            return task;
        }
        throw new ElementNot("-> getTaskById(Long id)");
    }

    @Override
    public Epic getEpicById(Long id) {
        if (epicMap.containsKey(id)) {
            Epic epic = epicMap.get(id);
            addHistory(epic.getId());
            return epic;
        }
        throw new ElementNot("-> getEpicById(Long id)");
    }

    @Override
    public Subtask getSubtaskById(Long id) {
        if (subtaskMap.containsKey(id)) {
            Subtask subtask = subtaskMap.get(id);
            addHistory(subtask.getId());
            return subtask;
        }
        throw new ElementNot("-> getSubtaskById(Long id)");
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
            for (Long subtaskId : epic.getSubtaskIdList()) {
                subtaskMap.remove(subtaskId);
                removeHistory(subtaskId);
            }
            statusEpic(epic);
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
        //System.out.println("epic not id=" + id);
        return subtaskArrayList;
    }

    @Override
    public void statusEpic(Epic epic) {
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
                    if (subtask != null) {
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
    }

    private static long getNextId() {
        return ++idCount;
    }
}