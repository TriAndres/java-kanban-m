package ru.practicum.fileCSV;

import ru.practicum.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSV {
    public static String toString(Task task) {
        String[] toJoin = {
                    String.valueOf(task.getId()),
                    String.valueOf(task.getTaskType()),
                    task.getTitle(),
                    String.valueOf(task.getStatus()),
                    task.getDescription(),
                    String.valueOf(task.getTaskId())
            };
            return String.join(",", toJoin) ;
    }

    public Task fromString(String value) {
//        String[] line = value.split(",");
//        if (line[1].equals("TASK")) {
//            String[] line1 = value.split(",");
//            return new Task(
//                    Long.parseLong(line1[0]),
//                    TaskType.valueOf(line1[1]),
//                    line1[2],
//                    Status.valueOf(line1[3].toUpperCase()),
//                    line1[4]
//            );
//        }
//
//        line = value.split("");
//        if (line[0].equals("EPIC")) {
//            line[0] = null;
//            String[] line1 = value.split(",");
//            return new Epic(
//                    Long.parseLong(line1[0]),
//                    TaskType.valueOf(line1[1]),
//                    line1[2],
//                    Status.valueOf(line1[3].toUpperCase()),
//                    line1[4],
//                    Long.parseLong(line1[5])
//            );
//        }
//
//        line = value.split("");
//        if (line[0].equals("SUBTASK")) {
//            line[0] = null;
//            String[] line1 = value.split(",");
//            return new Subtask(
//                    Long.parseLong(line1[0]),
//                    TaskType.valueOf(line1[1]),
//                    line1[2],
//                    Status.valueOf(line1[3].toUpperCase()),
//                    line[14],
//                    Long.parseLong(line1[5])
//            );
//        }
//
//        if (line[0].equals("History")) {
//            return null;
//        }
        return null;
    }

    public static String historyToString(List<Task> history) {
        //StringBuilder line = new StringBuilder();

//        for (Task task : history) {
//            line.append(task.getId()).append(",");
//        }
//        return String.join("",line.toString());
        List<String> list = new ArrayList<>();
        for (Task task : history) {
            list.add(String.valueOf(task.getId()));
        }
        System.out.println(list);
        return String.join(",",list);


        String[] toJoin = {
                String.valueOf(task.getId()),
                String.valueOf(task.getTaskType()),
                task.getTitle(),
                String.valueOf(task.getStatus()),
                task.getDescription(),
                String.valueOf(task.getTaskId())
        };
        return String.join(",", toJoin) ;
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