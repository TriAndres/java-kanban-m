package ru.practicum.prioritized;

import ru.practicum.model.Status;
import ru.practicum.model.Task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class InMemoryTaskPrioritized implements TaskPrioritized {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private final Set<Task> prioritized = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    @Override
    public void prioritizedRemove(Task task) {
        prioritized.removeIf(oldTask -> Objects.equals(oldTask.getId(), task.getId()));
    }

    @Override
    public void prioritizedAdd(Task task) {
        if (task != null) {
            if (prioritized.contains(task)) {
                prioritized.removeIf(oldTask -> Objects.equals(oldTask.getId(), task.getId()));
            }
            prioritized.add(task);
        }
    }

    @Override
    public List<Task> getPrioritizedTaskList() {
        return List.copyOf(prioritized);
    }

    public boolean validate(Task task) {
        boolean flag = true;
            if (prioritized.contains(task)) {
                for (Task listTask : prioritized) {
                        if (LocalDateTime.parse(task.getStartTime(), formatter).isBefore(LocalDateTime.parse(listTask.getStartTime(), formatter))
                                && LocalDateTime.parse(task.getEndTime(), formatter).isBefore(LocalDateTime.parse(listTask.getEndTime(), formatter))
                                || LocalDateTime.parse(task.getStartTime(), formatter).isAfter(LocalDateTime.parse(listTask.getStartTime(), formatter))
                                && LocalDateTime.parse(task.getEndTime(), formatter).isAfter(LocalDateTime.parse(listTask.getEndTime(), formatter))) {
                            flag = false;
                            break;
                        }
                    }
            }
        return flag;
    }
}