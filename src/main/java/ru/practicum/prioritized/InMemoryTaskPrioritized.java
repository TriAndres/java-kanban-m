package ru.practicum.prioritized;

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
            prioritized.add(task);
        }
    }

    @Override
    public List<Task> getPrioritizedTaskList() {
        return List.copyOf(prioritized);
    }

    public boolean validate(Task task) {
        List<Task> tasks = new ArrayList<>(getPrioritizedTaskList());
        if (!task.getStartTime().equals("0") && !task.getEndTime().equals("0")) {
            for (Task listTask : tasks) {
                if (!listTask.getStartTime().equals("0") && !listTask.getEndTime().equals("0")) {
                    if (LocalDateTime.parse(task.getStartTime(), formatter).isBefore(LocalDateTime.parse(listTask.getStartTime(), formatter))
                            && LocalDateTime.parse(task.getEndTime(), formatter).isBefore(LocalDateTime.parse(listTask.getEndTime(), formatter))
                            || LocalDateTime.parse(task.getStartTime(), formatter).isAfter(LocalDateTime.parse(listTask.getStartTime(), formatter))
                            && LocalDateTime.parse(task.getEndTime(), formatter).isAfter(LocalDateTime.parse(listTask.getEndTime(), formatter))) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    System.out.println("listTask : getStartTime() == 0 || getEndTime() == 0");
                }
            }
        } else {
            System.out.println("listTask : getStartTime() == 0 || getEndTime() == 0");
        }
        return false;
    }
}
