package ru.practicum.memory;

import ru.practicum.controller.Managers;
import ru.practicum.exception.ManagerValidateException;
import ru.practicum.history.HistoryManager;
import ru.practicum.model.Epic;
import ru.practicum.model.Status;
import ru.practicum.model.Subtask;
import ru.practicum.model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

public class InMemoryTaskManager implements TaskManage {
    private static long idCount;
    private final  HashMap<Long, Task> taskMap = new HashMap<>();
    private final HashMap<Long, Subtask> subtaskMap = new HashMap<>();
    private final HashMap<Long, Epic> epicMap = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    protected final Set<Task> prioritized = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    @Override
    public List<Task> getTaskAll() {
        if (!taskMap.isEmpty()) {
            return taskMap.values()
                    .stream()
                    .peek(t -> addHistory(t.getId()))
                    .toList();
        }
        return null;
    }

    @Override
    public List<Epic> getEpicAll() {
        if (!epicMap.isEmpty()) {
            return epicMap.values()
                    .stream()
                    .peek(e -> addHistory(e.getId()))
                    .toList();
        }
        return null;
    }

    @Override
    public List<Subtask> getSubtaskAll() {
        if (!subtaskMap.isEmpty()) {
            return subtaskMap.values()
                    .stream()
                    .peek(s -> addHistory(s.getId()))
                    .toList();
        }
        return null;
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
                prioritizedAdd(subtask);
                statusEpic(epic);
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
                prioritizedRemove(oldTask);
                oldTask.setDescription(task.getDescription());
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
                oldEpic.setDescription(epic.getDescription());
                epicMap.put(oldEpic.getId(), oldEpic);
                addHistory(oldEpic.getId());
                statusEpic(oldEpic);
            }
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask != null) {
            if (subtaskMap.containsKey(subtask.getId())) {
                Subtask oldSubtask = subtaskMap.get(subtask.getId());
                removeHistory(oldSubtask.getId());
                prioritizedRemove(oldSubtask);
                oldSubtask.setDescription(subtask.getDescription());
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
            prioritizedRemove(subtaskMap.get(id));
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
                prioritizedRemove(subtaskMap.get(subtaskId));
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
        if (epicMap.containsKey(epic.getId())) {
            setEpicDuration(epic);
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

    public void setEpicDuration(Epic epic) {
        List<Long> subtaskList = epic.getSubtaskIdList();
        Duration epicDuration = Duration.ZERO;
        LocalDateTime startTime = LocalDateTime.MAX;
        LocalDateTime endTime = LocalDateTime.MIN;
        if (!subtaskList.isEmpty()) {
            for (Long taskId : subtaskList) {
                Subtask subtask = subtaskMap.get(taskId);
                if (subtask.getStartTime().isBefore(startTime)) {
                    startTime = subtask.getStartTime();
                }
                if (subtask.getEndTime().isAfter(endTime)) {
                    endTime = subtask.getEndTime();
                }
            }
            epicDuration = Duration.between(startTime, endTime);
            epic.setStartTime(startTime);
            epic.setEndTime(endTime);
        } else {
            epic.setStartTime(LocalDateTime.MAX.minusSeconds(59).minusNanos(999999999));
            epic.setEndTime(LocalDateTime.MAX.minusSeconds(59).minusNanos(999999999));
        }
        epic.setDuration(epicDuration);
    }

    public void validate(Task task) {
        List<Task> tasks = new ArrayList<>(getPrioritizedTasks());
        if (!tasks.isEmpty()) {
            for (Task listTask : tasks) {
                if (task.getStartTime().isBefore(listTask.getStartTime())
                && task.getEndTime().isBefore(listTask.getEndTime())
                || task.getStartTime().isAfter(listTask.getStartTime())
                        && task.getEndTime().isAfter(listTask.getEndTime())) {
                } else {
                    throw new ManagerValidateException("Ошибка валидации");
                }
            }
        }
    }

    public void prioritizedRemove(Task task) {
        prioritized.removeIf(oldTask -> Objects.equals(oldTask.getId(), task.getId()));
    }

    public void prioritizedAdd(Task task) {
        prioritized.add(task);
    }

    public List<Task> getPrioritizedTasks() {
        return List.copyOf(prioritized);
    }

    private static long getNextId() {
        return ++idCount;
    }
}