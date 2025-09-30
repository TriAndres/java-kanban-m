package ru.practicum.memory;

import ru.practicum.controller.Managers;
import ru.practicum.history.inMemoryHistoryManager;
import ru.practicum.model.Epic;
import ru.practicum.model.Status;
import ru.practicum.model.Subtask;
import ru.practicum.model.Task;
import ru.practicum.prioritized.TaskPrioritized;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManage {
    private static long idCount;
    private final HashMap<Long, Task> taskMap = new HashMap<>();
    private final HashMap<Long, Subtask> subtaskMap = new HashMap<>();
    private final HashMap<Long, Epic> epicMap = new HashMap<>();

    private final inMemoryHistoryManager historyManager;
    private final TaskPrioritized prioritized;

    public InMemoryTaskManager() {
        historyManager = Managers.getDefaultHistory();
        prioritized = Managers.getDefaultTaskPrioritized();
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
            validate(task);
            taskMap.put(task.getId(), task);
            //prioritizedAdd(task);
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
            //setEpicDuration(epic);//
            return epic;
        }
        return null;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        if (subtask != null) {
            subtask.setId(getNextId());
            subtaskMap.put(subtask.getId(), subtask);
            validate(subtask);
            Long idEpic = subtask.getTaskId();
            if (epicMap.containsKey(idEpic)) {
                Epic epic = epicMap.get(idEpic);
                List<Long> list = epic.getSubtaskIdList();
                list.add(subtask.getId());
                epic.setSubtaskIdList(list);
                epicMap.put(epic.getId(), epic);
                //prioritizedAdd(subtask);
                statusEpic(epic);
                //setEpicDuration(epic);//
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
                //prioritizedRemove(oldTask);
                oldTask.setDescription(task.getDescription());
                oldTask.setEndTime(task.getEndTime());
                oldTask.setTaskId(task.getTaskId());
                taskMap.put(oldTask.getId(), oldTask);
                addHistory(oldTask.getId());
                //prioritizedAdd(oldTask);
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
                oldEpic.setEndTime(oldEpic.getEndTime());
                oldEpic.setTaskId(oldEpic.getTaskId());
                epicMap.put(oldEpic.getId(), oldEpic);
                addHistory(oldEpic.getId());
                statusEpic(oldEpic);
                //setEpicDuration(oldEpic);//
            }
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask != null) {
            if (subtaskMap.containsKey(subtask.getId())) {
                Subtask oldSubtask = subtaskMap.get(subtask.getId());
                removeHistory(oldSubtask.getId());
                //prioritizedRemove(oldSubtask);
                oldSubtask.setDescription(subtask.getDescription());
                oldSubtask.setEndTime(subtask.getEndTime());
                oldSubtask.setTaskId(subtask.getTaskId());
                subtaskMap.put(oldSubtask.getId(), oldSubtask);
                addHistory(oldSubtask.getId());
                statusEpic(epicMap.get(subtask.getTaskId()));
                setEpicDuration(epicMap.get(subtask.getTaskId()));//
                //prioritizedAdd(oldSubtask);
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
        //prioritizedRemove(taskMap.get(id));
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
            //setEpicDuration(epic);//
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
                //setEpicDuration(epic);//
            }
            removeHistory(id);
            //prioritizedRemove(subtaskMap.get(id));
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
                //prioritizedRemove(subtaskMap.get(subtaskId));
            }
            statusEpic(epic);
            //setEpicDuration(epic);//
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
        } else {

        }
        return null;
    }

    @Override
    public void statusEpic(Epic epic) {
        if (epic != null) {
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
                setEpicDuration(epic);
            }
        }
    }

    public void setEpicDuration(Epic epic) {
 //       if (epic != null) {
 //           if (epic.getStartTime() != null && epic.getEndTime() != null) {
//                List<Long> subtaskList = epic.getSubtaskIdList();
 //               Duration epicDuration = Duration.ZERO;
//                LocalDateTime startTime = LocalDateTime.MAX;
//                LocalDateTime endTime = LocalDateTime.MIN;
//                if (!subtaskList.isEmpty()) {
//                    for (Long taskId : subtaskList) {
//                        Subtask subtask = subtaskMap.get(taskId);
//                        if (subtask.getStartTime().isBefore(startTime)) {
//                            startTime = subtask.getStartTime();
//                        }
//                        if (subtask.getEndTime().isAfter(endTime)) {
//                            endTime = subtask.getEndTime();
//                        }
//                    }
//                    epicDuration = Duration.between(startTime, endTime);
//                    epic.setStartTime(startTime);
//                    epic.setEndTime(endTime);

 //               epicDuration = Duration.between(epic.getStartTime(), epic.getEndTime());
//                epic.setStartTime(startTime);
//                epic.setEndTime(endTime);
//                }
 //               epic.setDuration(epicDuration);
 //           }
 //      }

        //if (epic != null) {
//            Task task = taskMap.get(epicMap.get(epic.getId()).getTaskId());
//            Epic epic1 = epicMap.get(epic.getId());
//            if (!epic1.getSubtaskIdList().isEmpty()) {
//                for (Long subtaskId : epic1.getSubtaskIdList()) {
//                    Subtask subtask = subtaskMap.get(subtaskId);
//                    subtask.setStartTime(LocalDateTime.now());
//                    if ((subtaskMap.get(subtaskId).getStatus().equals(Status.DONE))) {
//                        subtask.setEndTime(LocalDateTime.now());
//                        subtask.setDuration(Duration.between(subtask.getStartTime(), subtask.getEndTime()));
//                    } else {
//                        subtask.setEndTime(subtask.getStartTime());
//                        subtask.setDuration(Duration.ZERO);
//                    }
//                    subtaskMap.put(subtask.getId(), subtask);
//                }
//            }
//            epic1.setStartTime(LocalDateTime.now());
//            if (epic1.getStatus().equals(Status.DONE)) {
//                epic1.setEndTime(LocalDateTime.now());
//                epic1.setDuration(Duration.between(epic1.getStartTime(),epic1.getEndTime()));
//            } else {
//                epic1.setEndTime(epic1.getStartTime());
//                epic1.setDuration(Duration.ZERO);
//            }
//            epicMap.put(epic1.getId(), epic1);
//            task.setStartTime(LocalDateTime.now());
//            if (task.getStatus().equals(Status.DONE)) {
//                task.setEndTime(LocalDateTime.now());
//                task.setDuration(Duration.between(task.getStartTime(), task.getEndTime()));
//            } else {
//                task.setEndTime(task.getEndTime());
//                task.setDuration(Duration.ZERO);
//            }
//            taskMap.put(task.getId(), task);
        //}
    }

    public boolean validate(Task task) {
        if (task != null) {
//            List<Task> tasks = new ArrayList<>(getPrioritizedTaskList());
//            if (!tasks.isEmpty()) {
//                for (Task listTask : tasks) {
//                    if (listTask != null) {
//                        if (task.getStartTime().isBefore(listTask.getStartTime())
//                                && task.getEndTime().isBefore(listTask.getEndTime())
//                                || task.getStartTime().isAfter(listTask.getStartTime())
//                                && task.getEndTime().isAfter(listTask.getEndTime())) {
//                        } else {
//                            //throw new ManagerValidateException("Ошибка валидации");
//                        }
//                    }
//                }
//            }
//
        }
        return false;
    }

//    public void prioritizedRemove(Task task) {
//        prioritized.prioritizedRemove(task);
//    }
//
//    public void prioritizedAdd(Task task) {
//
//        if (task instanceof Epic epic) {
//            setEpicDuration(epic);
//        }
//        prioritized.prioritizedAdd(task);
//    }
//
//    public List<Task> getPrioritizedTaskList() {
//        return prioritized.getPrioritizedTaskList();
//    }

    private static long getNextId() {
        return ++idCount;
    }
}