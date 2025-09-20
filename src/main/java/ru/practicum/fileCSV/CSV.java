package ru.practicum.fileCSV;

import ru.practicum.model.*;

import java.util.ArrayList;
import java.util.List;

public class CSV {
    public String toString(Task task) {
        String[] line = task.toString().split("/");
        return String.join(",",
                line[0],
                line[1],
                line[2],
                line[3],
                line[4],
                line[5]
        );
    }

    public static Task fromString(String script) {
        String[] line = script.split(",");

        if (line[0].equals("TASK")) {
            return new Task(
                    Long.parseLong(line[1]),
                    TaskType.valueOf(line[2]),
                    line[3],
                    Status.valueOf(line[4].toUpperCase()),
                    line[5],
                    Long.parseLong(line[6])
            );
        }

        if (line[0].equals("EPIC")) {
            return new Epic(
                    Long.parseLong(line[1]),
                    TaskType.valueOf(line[2]),
                    line[3],
                    Status.valueOf(line[4].toUpperCase()),
                    line[5],
                    Long.parseLong(line[6])
            );

        }

        if (line[0].equals("SUBTASK")) {
            return new Subtask(
                    Long.parseLong(line[1]),
                    TaskType.valueOf(line[2]),
                    line[3],
                    Status.valueOf(line[4].toUpperCase()),
                    line[5],
                    Long.parseLong(line[6])
            );

        }
        return null;
    }

    public static String historyToString(List<Task> history) {

        return "new ArrayList<>()";
    }

    public static List<Long> historyFromString(String script) {
        List<Long> list = new ArrayList<>();
        if (script != null) {
            String[] line1 = script.split(",");
            for (String taskId : line1) {
                list.add(Long.parseLong(taskId));
            }
            return list;
        }
        return list;
    }
}