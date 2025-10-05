package ru.practicum.memory;

import ru.practicum.controller.Managers;
import ru.practicum.history.HistoryManager;
import ru.practicum.history.inMemoryHistoryManager;
import ru.practicum.model.Epic;
import ru.practicum.model.Status;
import ru.practicum.model.Subtask;
import ru.practicum.model.Task;
import ru.practicum.prioritized.TaskPrioritized;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class InMemoryTaskManager implements TaskManage {
    private static long idCount;
    private final HashMap<Long, Task> taskMap = new HashMap<>();
    private final HashMap<Long, Subtask> subtaskMap = new HashMap<>();
    private final HashMap<Long, Epic> epicMap = new HashMap<>();

    private final HistoryManager historyManager;
    private final TaskPrioritized prioritized;
    private final DateTimeFormatter formatter;

    public InMemoryTaskManager() {
        historyManager = Managers.getDefaultHistory();
        prioritized = Managers.getDefaultTaskPrioritized();
        formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    }

    @Override
    public List<String> getTaskAll() {
        List<String> list = new ArrayList<>();
        if (!taskMap.isEmpty()) {
            for (Task task : taskMap.values()) {
                list.add(task.toString());
                addHistory(task.getId());
            }
            return list;
        }
        return list;
    }

    @Override
    public List<String> getEpicAll() {
        List<String> list = new ArrayList<>();
        if (!epicMap.isEmpty()) {
            for (Epic epic : epicMap.values()) {
                list.add(epic.toString());
                addHistory(epic.getId());
            }
            return list;
        }
        return list;
    }

    @Override
    public List<String> getSubtaskAll() {
        List<String> list = new ArrayList<>();
        if (!subtaskMap.isEmpty()) {
            for (Subtask subtask : subtaskMap.values()) {
                list.add(subtask.toString());
                addHistory(subtask.getId());
            }
            return list;
        }
        return list;
    }

    @Override
    public Task createTask(Task task) {
        if (task != null) {
            task.setId(getNextId());
            taskMap.put(task.getId(), task);
            prioritizedAdd(task);
            return task;
        }
        return null;
    }

    @Override
    public Epic createEpic(Epic epic) {
        if (epic != null) {
            epic.setId(getNextId());
            epicMap.put(epic.getId(), epic);
            statusEpic(epic);
            prioritizedAdd(epic);
            return epic;
        }
        return null;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        if (subtask != null) {
            subtask.setId(getNextId());
            subtaskMap.put(subtask.getId(), subtask);
            Long idEpic = subtask.getTaskId();
            if (epicMap.containsKey(idEpic)) {
                Epic epic = epicMap.get(idEpic);
                List<Long> list = epic.getSubtaskIdList();
                list.add(subtask.getId());
                epic.setSubtaskIdList(list);
                epicMap.put(epic.getId(), epic);
                statusEpic(epic);
                prioritizedAdd(subtask);
            }
            return subtask;
        }
        return null;
    }

    @Override
    public void updateTask(Task task) {
        if (task != null) {
            if (taskMap.containsKey(task.getId())) {
                Task oldTask = taskMap.get(task.getId());
                removeHistory(oldTask.getId());
                prioritizedRemove(oldTask);//
                oldTask.setDescription(task.getDescription());
                oldTask.setEndTime(task.getEndTime());
                oldTask.setTaskId(task.getTaskId());
                taskMap.put(oldTask.getId(), oldTask);
                addHistory(oldTask.getId());
                prioritizedAdd(oldTask);
            }
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic != null) {
            if (epicMap.containsKey(epic.getId())) {
                Epic oldEpic = epicMap.get(epic.getId());
                removeHistory(oldEpic.getId());
                prioritizedRemove(oldEpic);//
                oldEpic.setDescription(epic.getDescription());
                oldEpic.setEndTime(oldEpic.getEndTime());
                oldEpic.setTaskId(oldEpic.getTaskId());
                epicMap.put(oldEpic.getId(), oldEpic);
                addHistory(oldEpic.getId());
                statusEpic(oldEpic);
                prioritizedAdd(epic);
            }
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask != null) {
            if (subtaskMap.containsKey(subtask.getId())) {
                Subtask oldSubtask = subtaskMap.get(subtask.getId());
                removeHistory(oldSubtask.getId());
                prioritizedRemove(oldSubtask);//
                oldSubtask.setDescription(subtask.getDescription());
                oldSubtask.setEndTime(subtask.getEndTime());
                oldSubtask.setTaskId(subtask.getTaskId());
                subtaskMap.put(oldSubtask.getId(), oldSubtask);
                addHistory(oldSubtask.getId());
                statusEpic(epicMap.get(subtask.getTaskId()));
                prioritizedAdd(oldSubtask);
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
        return null;
    }

    @Override
    public Epic getEpicById(Long id) {
        if (epicMap.containsKey(id)) {
            Epic epic = epicMap.get(id);
            addHistory(epic.getId());
            return epic;
        }
        return null;
    }

    @Override
    public Subtask getSubtaskById(Long id) {
        if (subtaskMap.containsKey(id)) {
            Subtask subtask = subtaskMap.get(id);
            addHistory(subtask.getId());
            return subtask;
        }
        return null;
    }

    @Override
    public void deleteTaskById(Long id) {
        prioritizedRemove(taskMap.get(id));
        removeHistory(id);
        taskMap.remove(id);
    }

    @Override
    public void deleteEpicById(Long id) {
        if (epicMap.containsKey(id)) {
            Epic epic = epicMap.get(id);
            for (Long subtaskId : epic.getSubtaskIdList()) {
                if (epic.getId().equals(subtaskId)) {
                    prioritizedRemove(subtaskMap.get(subtaskId));
                    removeHistory(subtaskId);
                    subtaskMap.remove(subtaskId);
                }
            }
            prioritizedRemove(epicMap.get(id));
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
            prioritizedRemove(subtaskMap.get(id));
            subtaskMap.remove(id);
        }
    }

    @Override
    public void deleteTaskAll() {
        for (Long id : taskMap.keySet()) {
            removeHistory(id);
            prioritizedRemove(taskMap.get(id));
        }
        taskMap.clear();
    }

    @Override
    public void deleteEpicAll() {
        for (Long id : epicMap.keySet()) {
            removeHistory(id);
            prioritizedRemove(subtaskMap.get(id));//

        }
        for (Long id : subtaskMap.keySet()) {
            removeHistory(id);
            prioritizedRemove(subtaskMap.get(id));//
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
                prioritizedRemove(subtaskMap.get(subtaskId));//
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
        if (epicMap.containsKey(id)) {
            ArrayList<Subtask> subtaskArrayList = new ArrayList<>();
            Epic epic = epicMap.get(id);
            for (Long subtaskId : epic.getSubtaskIdList()) {
                subtaskArrayList.add(subtaskMap.get(subtaskId));
            }
            return subtaskArrayList;
        }
        return null;
    }

    @Override
    public void statusEpic(Epic epic) {
        if (epic != null) {
            if (epicMap.containsKey(epic.getId())) {
                Task task = getTaskById(epic.getTaskId());
                if (epic.getSubtaskIdList().isEmpty()) {
                    epic.setStatus(Status.NEW);
                    task.setStatus(Status.NEW);
                } else {
                    List<Long> subtaskNew = new ArrayList<>(epic.getSubtaskIdList());
                    int countDone = 0;
                    int countNew = 0;
                    for (Long subtaskId : subtaskNew) {
                        if (subtaskMap.get(subtaskId).getStatus().equals(Status.DONE)) {
                            countDone++;
                        }
                        if (subtaskMap.get(subtaskId).getStatus().equals(Status.NEW)) {
                            countNew++;
                        }
                        if (countDone == epic.getSubtaskIdList().size()) {
                            epic.setStatus(Status.DONE);
                            task.setStatus(Status.DONE);
                        } else if (countNew == epic.getSubtaskIdList().size()) {
                            epic.setStatus(Status.NEW);
                            task.setStatus(Status.NEW);
                        } else {
                            epic.setStatus(Status.IN_PROGRESS);
                            task.setStatus(Status.IN_PROGRESS);
                        }
                    }
                }
            }
            setEpicDuration(epic);
        }
    }

    private void setEpicDuration(Epic epic) {
        if (epic != null) {
            Task task = getTaskById(epic.getTaskId());
            List<Long> subtaskNew = new ArrayList<>(epic.getSubtaskIdList());
            int countDone = 0;
            for (Long subtaskId : subtaskNew) {
                if (subtaskMap.get(subtaskId).getStatus().equals(Status.DONE)) {
                    countDone++;
                }
                if (countDone == epic.getSubtaskIdList().size()) {
                    LocalDateTime localDateTime = LocalDateTime.now();
                    String time = localDateTime.format(formatter);
                    task.setEndTime(time);
                    Long minute = Duration.between(
                                    LocalDateTime.parse(task.getStartTime(), formatter),
                                    LocalDateTime.parse(task.getEndTime(), formatter)
                            )
                            .toMinutes();
                    task.setDuration(String.valueOf(minute));
                    epic.setDuration(task.getDuration());
                    epic.setEndTime(task.getEndTime());
                } else {
                    task.setEndTime("0");
                    task.setDuration("0");
                    epic.setEndTime(task.getEndTime());
                    epic.setDuration(task.getDuration());
                }
            }
        }
    }

    @Override
    public void prioritizedRemove(Task task) {
        prioritized.prioritizedRemove(task);
    }

    @Override
    public void prioritizedAdd(Task task) {
        prioritized.prioritizedAdd(task);
    }

    @Override
    public List<Task> getPrioritizedTaskList() {
        return prioritized.getPrioritizedTaskList();
    }

//    @Override
//    public boolean isPrioritizedValidation(Task task) {
//        return prioritized.validate(task);
//    }

    private static long getNextId() {
        return ++idCount;
    }
}