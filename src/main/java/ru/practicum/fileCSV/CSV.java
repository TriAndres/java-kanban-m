package ru.practicum.fileCSV;

import ru.practicum.history.HistoryManager;
import ru.practicum.memory.InMemoryTaskManager;
import ru.practicum.model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CSV {
    protected static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public String toString(Task task) {
        String[] line = task.toString().split("/");
        return String.join(",",
                line[0],
                line[1],
                line[2],
                line[3],
                line[4],
                line[5],
                line[6],
                line[7]
        );
    }

    public void fromString(FileBackedTaskManager manage, String script) {
        String[] line = script.split(",");
        if (line[0].equals("ID_TASK")) {
            manage.createTask(
                    new Task(
                            Long.parseLong(line[1]),
                            TaskType.valueOf(line[2]),
                            line[3],
                            Status.valueOf(line[4].toUpperCase()),
                            line[5],
                            Duration.parse(line[6]),
                            LocalDateTime.parse(line[7], formatter),
                            Long.parseLong(line[8])
                    )
            );
        }
        if (line[0].equals("ID_EPIC")) {
            manage.createEpic(
                    new Epic(
                            Long.parseLong(line[1]),
                            TaskType.valueOf(line[2]),
                            line[3],
                            Status.valueOf(line[4].toUpperCase()),
                            line[5],
                            Duration.parse(line[6]),
                            LocalDateTime.parse(line[7], formatter),
                            Long.parseLong(line[8])
                    )
            );
        }
        if (line[0].equals("ID_SUBTASK")) {
            manage.createSubtask(
                    new Subtask(
                            Long.parseLong(line[1]),
                            TaskType.valueOf(line[2]),
                            line[3],
                            Status.valueOf(line[4].toUpperCase()),
                            line[5],
                            Duration.parse(line[6]),
                            LocalDateTime.parse(line[7], formatter),
                            Long.parseLong(line[8])
                    )
            );
        }
    }

//    public String historyToString(String script) {
//        String[] lines = script.split("/");
//        String line = "";
//        for (String taskId : lines) {
//            System.out.println(taskId);
//            line += taskId + ",";
//        }
//        return line;
//    }

    public String historyToString(Task task) {
        return String.join(",",String.valueOf(task.getId()) + ",");
    }

    public List<Long> historyFromString(String script) {
        List<Long> list = new ArrayList<>();
        String[] line1 = script.split(",");
        for (String taskId : line1) {
            list.add(Long.parseLong(taskId));
        }
        return list;
    }
}