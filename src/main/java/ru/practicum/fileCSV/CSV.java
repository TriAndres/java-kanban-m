package ru.practicum.fileCSV;

import ru.practicum.model.*;

import java.util.ArrayList;
import java.util.List;

public class CSV {
    public static String toString(Task task) {
        if (task.getTaskType().equals(TaskType.TASK)) {
            String[] toJoin = {
                    String.valueOf(task.getId()),
                    String.valueOf(task.getTaskType()),
                    task.getTitle(),
                    String.valueOf(task.getStatus()),
                    task.getDescription()
            };
            return String.join(",", toJoin) + "\n";
        } else if (task.getTaskType().equals(TaskType.EPIC)) {
            String[] toJoin = {
                    String.valueOf(task.getId()),
                    String.valueOf(task.getTaskType()),
                    task.getTitle(),
                    String.valueOf(task.getStatus()),
                    task.getDescription(),
                    String.valueOf(task.getTaskId())
            };
            return String.join(",", toJoin) + "\n";
        } else if (task.getTaskType().equals(TaskType.SUBTASK)) {
            String[] toJoin = {
                    String.valueOf(task.getId()),
                    String.valueOf(task.getTaskType()),
                    task.getTitle(),
                    String.valueOf(task.getStatus()),
                    task.getDescription(),
                    String.valueOf(task.getTaskId())
            };
            return String.join(",", toJoin) + "\n";
        }
        return null;
    }

    public Task fromString(String value) {
        String[] line = value.split(",");
        if (line[0].equals("type")) {
            return null;
        }
        if (line[1].equals("TASK")) {
            return new Task(
                    Long.parseLong(line[0]),
                    TaskType.valueOf(line[1]),
                    line[2],
                    Status.valueOf(line[3].toUpperCase()),
                    line[4]
            );
        }

        if (line[1].equals("EPIC")) {
            return  new Epic(
                    Long.parseLong(line[0]),
                    TaskType.valueOf(line[1]),
                    line[2],
                    Status.valueOf(line[3].toUpperCase()),
                    line[4],
                    Long.parseLong(line[5])
            );
        }

        if (line[1].equals("SUBTASK")) {
            return  new Subtask(
                    Long.parseLong(line[0]),
                    TaskType.valueOf(line[1]),
                    line[2],
                    Status.valueOf(line[3].toUpperCase()),
                    line[4],
                    Long.parseLong(line[5])
            );
        }
        if (line[0].equals("History")) {
            return null;
        }
        return null;
    }

    public static String historyToString(List<Task> history) {
        StringBuilder line = new StringBuilder();
        for (Task task : history) {
            line.append(task.getId()).append(",");
        }
        return String.join("", "History ", line.toString());
    }

    static List<Integer> historyFromString(String value) {
        List<Integer> list = new ArrayList<>();
        if (value != null) {
            String[] id = value.split(",");
            for (String number : id) {
                list.add(Integer.parseInt(number));
            }
        }
        return list;
    }
}