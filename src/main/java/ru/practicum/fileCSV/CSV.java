package ru.practicum.fileCSV;

import ru.practicum.model.*;

import java.util.ArrayList;
import java.util.List;

public class CSV {
    public static String toString(Task task) {
        String[] toJoin = {
                String.valueOf(task.getId()),
                String.valueOf(task.getType()),
                task.getName(),
                String.valueOf(task.getStatus()),
                task.getDescription(),
                String.valueOf(task.getTaskId())
        };
        return String.join(",", toJoin);
    }

    public static String fromString(String value) {

        return "task";
    }

    public static String historyToString(List<Task> history) {

        return "new ArrayList<>()";
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