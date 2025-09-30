package ru.practicum.fileCSV;

import ru.practicum.model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CSV {

    public String toString(String task) {
        String[] line = task.split("/");
        return String.join(",",
                line[0],
                line[1],
                line[2],
                line[3],
                line[4],
                line[5],
                line[6],
                line[7],
                line[8]
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
                            line[6],
                            line[7],
                            line[8],
                            Long.parseLong(line[9])
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
                            line[6],
                            line[7],
                            line[8],
                            Long.parseLong(line[9])
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
                            line[6],
                            line[7],
                            line[8],
                            Long.parseLong(line[9])
                    )
            );
        }
    }

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