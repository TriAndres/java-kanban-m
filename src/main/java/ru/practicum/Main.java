package ru.practicum;

import ru.practicum.fileCSV.FileBackedTaskManager;
import ru.practicum.history.InMemoryHistoryManager;
import ru.practicum.memory.TaskManage;
import ru.practicum.model.Epic;
import ru.practicum.model.Subtask;
import ru.practicum.model.Task;

import java.util.Objects;
import java.util.Scanner;

import static ru.practicum.controller.Managers.detDefaultFile;
import static ru.practicum.controller.Managers.getDefaultHistory;


public class Main {
    FileBackedTaskManager manage = detDefaultFile();
    InMemoryHistoryManager historyManager = getDefaultHistory();

    public static void main(String[] args) {
        new Main().game();
    }

    public void game() {
        while (true) {
            String line = menu();
            select(line);
            if (line.equals("0")) break;
        }
    }

    private String menu() {
        System.out.println("""
                1 - просмотр истории.
                2 - добавить задачу.
                3 - добавить подзадачу.
                4 - вывод статистику задачи.
                5 - добавить статус подзадаче.
                6 - удалить задачу.
                7 - вывод статистику всех задач.
                8 - удалить все задачи.
                0 - выход из программы.
                """);
        return new Scanner(System.in).next();
    }

    private void select(String line) {
        switch (line) {
            case "0":
                System.out.println("Выход из программы.");
                break;
            case "1":
                showTaskHistory();
                break;
            case "2":
                addTask();
                break;
            case "3":
                addSubtask();
                break;
            case "4":
                showStatisticTaskId();
                break;
            case "5":
                addStatusSubtask();
                break;
            case "6":
                deleteTaskById();
                break;
            case "7":
                showStatisticTaskAll();
                break;
            case "8":
                deleteTaskAll();
                break;
            default:
                System.out.println("выбирите действие из списка.");
                break;
        }
    }

    private void showTaskHistory() {
        if (!manage.getHistory().isEmpty()) {
            for (Task task : manage.getHistory()) {
                System.out.println(task.toString());
            }
            System.out.println("getHistory()\n");
        } else {
            System.out.println("Список пуст, добавьте задачу.\n");
        }
    }

    private void addTask() {
        System.out.println("Введите название задачи:");
        String description = new Scanner(System.in).nextLine();

        Task task = new Task(
                null,
                null,
                null,
                null,
                description
        );
        task = manage.createTask(task);

        Epic epic = new Epic(
                null,
                null,
                null,
                null,
                description,
                task.getId()
        );
        epic = manage.createEpic(epic);

        System.out.println("Записано:\n" + task + epic);
    }

    private void addSubtask() {
        if (!manage.getEpicAll().isEmpty()) {
            System.out.println("Введите id задачи для ввода подзадачи:");
            long id = 0L;
            while (true) {
                if (new Scanner(System.in).hasNextLong()) {
                    id = new Scanner(System.in).nextLong();
                    if (manage.getTaskAll().contains(manage.getTaskById(id))) {
                        break;
                    } else if (id == -1L) {
                        System.out.println("-1 выход из меню");
                    }
                } else {
                    new Scanner(System.in).nextLine();
                }
            }
            if (manage.getTaskAll().contains(manage.getTaskById(id))) {

                Task task = manage.getTaskById(id);
                Epic epic = manage
                        .getEpicAll()
                        .stream()
                        .filter(i -> Objects.equals(i.getId(), task.getId()))
                        .toList()
                        .getFirst();
                epic = manage.getEpicById(epic.getId());


                System.out.println("Введите название подзадачи:");
                String description = new Scanner(System.in).nextLine();
                Subtask subtask = new Subtask(
                        null,
                        null,
                        null,
                        null,
                        description,
                        epic.getId()
                );
                manage.createSubtask(subtask);

                System.out.println("Записано в задаче:\n" + task);
                System.out.println("Записано getListSubtaskIdEpic(epic.getId())");
                for (Subtask subtask1 : manage.getListSubtaskIdEpic(epic.getId())) {
                    System.out.println(subtask1);
                }
            }
        } else {
            System.out.println("Список пуст, добавьте задачу.\n");
        }
    }

    private void showStatisticTaskId() {
//        if (!taskManage.getTaskAll().isEmpty()) {
//            System.out.println("Введите id задачи:");
//            long id = Long.parseLong(new Scanner(System.in).next());
//            if (taskManage.getTaskAll().contains(taskManage.getTaskById(id))) {
//                Task task = taskManage.getTaskById(id);
//                System.out.println(task.toString());
//                Epic epic = taskManage
//                        .getEpicAll()
//                        .stream()
//                        .filter(i -> Objects.equals(i.getId(), task.getId()))
//                        .toList()
//                        .getFirst();
//                for (Subtask subtask : epic.getSubtasks()) {
//                    System.out.println(subtask.toString());
//                }
//            }
//        } else {
//            System.out.println("Список пуст, добавьте задачу.");
//        }
    }

    private void addStatusSubtask() {
//        if (!taskManage.getSubtaskAll().isEmpty()) {
//            System.out.println("Введите id подзадачи:");
//            long id = Long.parseLong(new Scanner(System.in).next());
//            if (taskManage.getSubtaskAll().contains(taskManage.getSubtaskById(id))) {
//                Subtask subtask = taskManage.getSubtaskById(id);
//                while (true) {
//                    System.out.println("""
//                            NEW - не решёная подзадача.
//                            DONE - решил подзадачу.
//                            """);
//                    String status = new Scanner(System.in).next();
//                    switch (status.toUpperCase()) {
//                        case "NEW":
//                            subtask.setStatus(NEW);
//                            break;
//                        case "DONE":
//                            subtask.setStatus(DONE);
//                            break;
//                        default:
//                            System.out.println("Выбирите команду из списка.");
//                    }
//                    taskManage.updateSubtask(subtask);
//                    System.out.println("Записано:\n" +
//                            subtask.toString());
//                    if (status.equals("NEW") ||
//                            status.equals("new") ||
//                            status.equals("DONE") ||
//                            status.equals("done")) break;
//                }
//            } else {
//                System.out.println("Такой подзадачи нет с id=" + id);
//            }
//        } else {
//            System.out.println("Список пуст, добавmте подзадачу.");
//        }
    }

    private void deleteTaskById() {
//        if (!taskManage.getTaskAll().isEmpty()) {
//            System.out.println("Введите id задачи:");
//            long id = Long.parseLong(new Scanner(System.in).next());
//            if (taskManage.getTaskAll().contains(taskManage.getTaskById(id))) {
//                Task task = taskManage.getTaskById(id);
//                Epic epic = taskManage
//                        .getEpicAll()
//                        .stream()
//                        .filter(i -> Objects.equals(i.getId(), task.getId()))
//                        .toList()
//                        .getFirst();
//                taskManage.deleteTaskById(id);
//                taskManage.deleteEpicById(epic.getId());
//                for (Subtask subtask : taskManage.getListSubtaskIdEpic(id)) {
//                    taskManage.deleteSubtaskById(subtask.getId());
//                }
//            }
//        } else {
//            System.out.println("Список пуст, добавте подзадачу.");
//        }
    }

    private void showStatisticTaskAll() {
//        System.out.println("getTaskAll()");
//        for (Task task : taskManage.getTaskAll()) {
//            System.out.println(task.toString());
//        }
//        System.out.println("getEpicAll()");
//        for (Epic epic : taskManage.getEpicAll()) {
//            System.out.println(epic.toString());
//            for (Subtask subtask : taskManage.getEpicById(epic.getId()).getSubtasks()) {
//                System.out.println(subtask.toString());
//            }
//        }
//        System.out.println("getSubtaskAll()");
//        for (Subtask subtask : taskManage.getSubtaskAll()) {
//            System.out.println(subtask.toString());
//        }
//
//        System.out.println("getListSubtaskIdEpic(Long id)");
//        for (Epic epic : taskManage.getEpicAll()) {
//            for (Subtask subtask : taskManage.getListSubtaskIdEpic(epic.getId())) {
//                System.out.println(subtask.toString());
//            }
//        }
    }

    private void deleteTaskAll() {
//        taskManage.deleteTaskAll();
//        taskManage.deleteEpicAll();
//        taskManage.deleteSubtaskAll();
    }
}