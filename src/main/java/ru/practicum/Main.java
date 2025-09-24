package ru.practicum;

import ru.practicum.controller.Managers;
import ru.practicum.fileCSV.FileBackedTaskManager;
import ru.practicum.model.Epic;
import ru.practicum.model.Subtask;
import ru.practicum.model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Scanner;

import static ru.practicum.controller.Managers.detDefaultFile;
import static ru.practicum.model.Status.DONE;
import static ru.practicum.model.Status.NEW;


public class Main {
    private Scanner scanner = Managers.scanner();
    private  FileBackedTaskManager manage = detDefaultFile();

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
                1 - добавить задачу.
                2 - добавить подзадачу.
                3 - добавить статус подзадаче.
                4 - вывод статистику задачи.
                5 - вывод статистику всех задач.
                6 - просмотр истории.
                7 - удалить задачу.
                8 - удалить все задачи.
                0 - выход из программы.
                """);
        return scanner.nextLine();
    }

    private void select(String line) {
        switch (line) {
            case "0":
                System.out.println("Выход из программы.");
                break;
            case "1":
                addTask();
                break;
            case "2":
                addSubtask();
                break;
            case "3":
                addStatusSubtask();
                break;
            case "4":
                showStatisticTaskId();
                break;
            case "5":
                showStatisticTaskAll();
                break;
            case "6":
                showTaskHistory();
                break;
            case "7":
                deleteTaskById();
                break;
            case "8":
                deleteTaskAll();
                break;
            default:
                System.out.println("выбирите действие из списка.");
                break;
        }
    }

    private void addTask() {
        System.out.println("Введите название задачи:");
        String description = scanner.nextLine();

        Task task = manage.createTask(
                new Task(
                        null,
                        null,
                        null,
                        null,
                        description,
                        Duration.ZERO,
                        LocalDateTime.now().plusDays(1),
                        0L
                )
        );

        Epic epic = manage.createEpic(
                new Epic(
                        null,
                        null,
                        null,
                        null,
                        description,
                        Duration.ZERO,
                        LocalDateTime.now().plusDays(2),
                        task.getId()
                )
        );
        System.out.println("Записано:\n" + task + epic);
    }

    private void addSubtask() {
        if (!manage.getEpicAll().isEmpty()) {
            manage.save();
            System.out.println("Введите id задачи:");
            long id = 0;
            while (true) {
                if (scanner.hasNextLong()) {
                    id = scanner.nextLong();
                    if (manage.getTaskAll().contains(manage.getTaskById(id))) {
                        break;
                    } else if (id == -1L) {
                        System.out.println("-1 выход из меню");
                        break;
                    }
                } else {
                    scanner.nextLine();
                }
            }
            if (manage.getTaskAll().contains(manage.getTaskById(id))) {
                Task task = manage.getTaskById(id);

                Epic epic = manage
                        .getEpicAll()
                        .stream()
                        .filter(i -> Objects.equals(i.getTaskId(), task.getId()))
                        .toList()
                        .getFirst();

                System.out.println("Введите название подзадачи:");
                String description = scanner.next();
                Subtask subtask = manage.createSubtask(
                        new Subtask(
                                null,
                                null,
                                null,
                                null,
                                description,
                                Duration.ZERO,
                                LocalDateTime.now().plusDays(3),
                                epic.getId()
                        )
                );

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

    private void addStatusSubtask() {
        if (!manage.getSubtaskAll().isEmpty()) {
            System.out.println("Введите id подзадачи:");
            long id = 0L;
            while (true) {
                if (scanner.hasNextLong()) {
                    id = scanner.nextLong();
                    if (manage.getSubtaskAll().contains(manage.getSubtaskById(id))) {
                        break;
                    } else if (id == -1L) {
                        System.out.println("-1 выход из меню");
                        break;
                    }

                } else {
                    scanner.nextLine();
                }
            }
            if (manage.getSubtaskAll().contains(manage.getSubtaskById(id))) {
                Subtask subtask = manage.getSubtaskById(id);
                while (true) {
                    System.out.println("""
                            NEW - не решёная подзадача.
                            DONE - решил подзадачу.
                            """);
                    String status = new Scanner(System.in).next();
                    switch (status.toUpperCase()) {
                        case "NEW":
                            subtask.setStatus(NEW);
                            break;
                        case "DONE":
                            subtask.setStatus(DONE);
                            break;
                        default:
                            System.out.println("Выбирите команду из списка.");
                    }
                    manage.updateSubtask(subtask);
                    System.out.println("Записано:\n" + subtask.toString());
                    if (status.equals("NEW") ||
                            status.equals("new") ||
                            status.equals("DONE") ||
                            status.equals("done")) break;
                }
            } else {
                System.out.println("Такой подзадачи нет с id=" + id);
            }
        } else {
            System.out.println("Список пуст, добавmте подзадачу.");
        }
    }

    private void showStatisticTaskId() {
        if (!manage.getTaskAll().isEmpty()) {
            System.out.println("Введите id задачи:");
            long id = 0L;
            while (true) {
                if (scanner.hasNextLong()) {
                    id = scanner.nextLong();
                    if (manage.getTaskAll().contains(manage.getTaskById(id))) {
                        break;
                    } else if (id == -1L) {
                        System.out.println("-1 выход из меню");
                        break;
                    }

                } else {
                    scanner.nextLine();
                }
            }
            if (manage.getTaskAll().contains(manage.getTaskById(id))) {
                Task task = manage.getTaskById(id);
                System.out.println(task.toString());
                Epic epic = manage
                        .getEpicAll()
                        .stream()
                        .filter(i -> Objects.equals(i.getTaskId(), task.getId()))
                        .toList()
                        .getFirst();
                for (Subtask subtask : manage.getListSubtaskIdEpic(epic.getId())) {
                    System.out.println(subtask.toString());
                }
            }
        } else {
            System.out.println("Список пуст, добавьте задачу.");
        }
    }

    private void showStatisticTaskAll() {
        System.out.println("getTaskAll()");
        for (Task task : manage.getTaskAll()) {
            System.out.print(task.toString());
        }

        System.out.println("getEpicAll()");
        for (Epic epic : manage.getEpicAll()) {
            System.out.print(epic.toString());
        }

        System.out.println("getSubtaskAll()");
        for (Subtask subtask : manage.getSubtaskAll()) {
            System.out.print(subtask.toString());
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

    private void deleteTaskById() {
        if (!manage.getTaskAll().isEmpty()) {
            System.out.println("Введите id задачи:");
            long id = 0L;
            while (true) {
                if (scanner.hasNextLong()) {
                    id = scanner.nextLong();
                    if (manage.getTaskAll().contains(manage.getTaskById(id))) {
                        break;
                    } else if (id == -1L) {
                        System.out.println("-1 выход из меню");
                        break;
                    }
                } else {
                    scanner.nextLine();
                }
            }
            if (manage.getTaskAll().contains(manage.getTaskById(id))) {
                Task task = manage.getTaskById(id);
                Epic epic = manage
                        .getEpicAll()
                        .stream()
                        .filter(i -> Objects.equals(i.getTaskId(), task.getId()))
                        .toList()
                        .getFirst();

                for (Subtask subtask : manage.getListSubtaskIdEpic(epic.getId())) {
                    manage.deleteSubtaskById(subtask.getId());
                }
                manage.deleteEpicById(epic.getId());
                manage.deleteTaskById(id);
            }
        } else {
            System.out.println("Список пуст, добавте подзадачу.");
        }
    }

    private void deleteTaskAll() {
        manage.deleteTaskAll();
        manage.deleteEpicAll();
        manage.deleteSubtaskAll();
    }
}