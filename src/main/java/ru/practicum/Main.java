package ru.practicum;

import ru.practicum.controller.Managers;
import ru.practicum.exception.NullException;
import ru.practicum.memory.TaskManage;
import ru.practicum.model.Epic;
import ru.practicum.model.Subtask;
import ru.practicum.model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static ru.practicum.controller.Managers.detDefaultFile;
import static ru.practicum.model.Status.DONE;
import static ru.practicum.model.Status.NEW;


public class Main {
private final Scanner scanner = Managers.scanner();
    private final TaskManage manage = detDefaultFile();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

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
                7 - просмотр приоритета.
                8 - удалить задачу.
                9 - удалить все задачи.
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
                createTask();
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
                showPrioritized();
                break;
            case "8":
                deleteTaskEpicSubtaskById();
                break;
            case "9":
                deleteTaskAll();
                break;
            default:
                System.out.println("выбирите действие из списка.");
                break;
        }
    }

    private void createTask() {
        System.out.println("Введите название задачи:");
        String description = scanner.nextLine();

        LocalDateTime localDateTime = LocalDateTime.now();
        String time = localDateTime.format(formatter);

        Task task = manage.createTask(
                new Task(
                        null,
                        null,
                        null,
                        null,
                        description,
                        "0",
                        time,
                        "0",
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
                        "0",
                        time,
                        "0",
                        task.getId()
                )
        );
        task.setTaskId(epic.getId());
        manage.updateTask(task);
        System.out.println("Записано:\n" + task + epic);
    }


    private void addSubtask() {
        if (!manage.getEpicAll().isEmpty()) {
            System.out.println("Введите id задачи:");
            Epic epic;
            while (true) {
                if (scanner.hasNextLong()) {
                    long id = scanner.nextLong();
                    try {
                        epic = manage.getEpicById(manage.getTaskById(id).getTaskId());
                    } catch (Exception e) {
                        throw new NullException("нет в спмске.");
                    }

                    if (epic != null) {
                        break;
                    }
                    if (id == 0) {
                        System.out.println("выход из метода addSubtask()");
                    }
                } else {
                    scanner.nextLine();
                }
            }
            if (!manage.getEpicAll().isEmpty()) {
                LocalDateTime localDateTime = LocalDateTime.now();
                String time = localDateTime.format(formatter);

                System.out.println("Введите название подзадачи:");
                String description = scanner.next();
                Subtask subtask = manage.createSubtask(
                        new Subtask(
                                null,
                                null,
                                null,
                                null,
                                description,
                                "0",
                                time,
                                "0",
                                epic.getId()
                        )
                );

                System.out.println("Записано в задаче:\n" + subtask);
            }
        } else {
            System.out.println("выход из метода addSubtask()\n");
        }
    }

    private void addStatusSubtask() {
        if (!manage.getSubtaskAll().isEmpty()) {
            System.out.println("Введите id подзадачи:");
            Subtask subtask = null;
            while (true) {
                if (scanner.hasNextLong()) {
                    long id = scanner.nextLong();
                    try {
                        subtask = manage.getSubtaskById(id);
                    } catch (Exception e) {
                        throw new NullException("нет в спмске.");
                    }
                    if (subtask != null) {
                        break;
                    }
                    if (id == 0) {
                        System.out.println("выход из метода addStatusSubtask()");
                        break;
                    }
                } else {
                    scanner.nextLine();
                }
            }
            if (!manage.getSubtaskAll().isEmpty()) {
                if (subtask != null) {
                    while (true) {
                        System.out.println("""
                                NEW - не решёная подзадача.
                                DONE - решил подзадачу.
                                """);
                        String status = new Scanner(System.in).next();
                        switch (status.toUpperCase()) {
                            case "NEW":
                                subtask.setStatus(NEW);
                                subtask.setDuration("0");
                                subtask.setEndTime("0");
                                break;
                            case "DONE":
                                subtask.setStatus(DONE);
                                LocalDateTime localDateTime = LocalDateTime.now();
                                String time1 = localDateTime.format(formatter);
                                subtask.setEndTime(time1);
                                Long minute = Duration.between(
                                                LocalDateTime.parse(subtask.getStartTime(), formatter),
                                                LocalDateTime.parse(subtask.getEndTime(), formatter)
                                        )
                                        .toMinutes();
                                subtask.setDescription(String.valueOf(minute));
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
                }
            } else {
                System.out.println("Такой подзадачи нет.");
            }
        } else {
            System.out.println("выход из метода addSubtask()\n");
        }
    }

    private void showStatisticTaskId() {
        if (!manage.getTaskAll().isEmpty()) {
            System.out.println("Введите id задачи:");
            Task task = null;
            while (true) {
                String id = scanner.nextLine();
                if (id.equals("0")) {
                    System.out.println("выход из метода showStatisticTaskId()");
                    break;
                }
                try {
                    task = manage.getTaskById(Long.parseLong(id));
                    if (task.getId() == Long.parseLong(id)) break;
                } catch (RuntimeException e) {
                    System.out.println("Введите id задачи:");
                }
            }
            if (!manage.getTaskAll().isEmpty() && task != null) {
                task = manage.getTaskById(task.getId());
                System.out.println(task.toString());
                Epic epic = manage.getEpicById(task.getTaskId());
                System.out.println(epic.toString());
                for (Subtask subtask : manage.getListSubtaskIdEpic(epic.getId())) {
                    System.out.println(subtask.toString());
                }
            }
        } else {
            System.out.println("выход из метода showStatisticTaskId()\n");
        }
    }

    private void showStatisticTaskAll() {
        if (!manage.getTaskAll().isEmpty()) {
            System.out.println("getTaskAll()");
            manage.getTaskAll().forEach(System.out::println);
        }

        if (!manage.getEpicAll().isEmpty()) {
            System.out.println("getEpicAll()");
            manage.getEpicAll().forEach(System.out::println);
        }

        if (!manage.getSubtaskAll().isEmpty()) {
            System.out.println("getSubtaskAll()");
            manage.getSubtaskAll().forEach(System.out::println);
        }
    }

    private void showTaskHistory() {
        if (!manage.getHistory().isEmpty() && manage.getHistory() != null) {
            manage.getHistory().forEach(h -> System.out.println(h.toString()));
            System.out.println("getHistory()\n");
        } else {
            System.out.println("Список пуст, добавьте задачу.\n");
        }
    }

    public void showPrioritized() {
        if (!manage.getPrioritizedTaskList().isEmpty()) {
            for (Task task : manage.getPrioritizedTaskList()) {
                System.out.println(task);
            }
        }
    }

    private void deleteTaskEpicSubtaskById() {
        if (!manage.getTaskAll().isEmpty()) {
            System.out.println("Введите id задачи:");
            Task task = null;
            while (true) {
                String id = scanner.nextLine();
                if (id.equals("0")) {
                    System.out.println("выход из метода deleteTaskEpicSubtaskById()");
                    break;
                }
                try {
                    task = manage.getTaskById(Long.parseLong(id));
                    if (task.getId() == Long.parseLong(id)) break;
                } catch (RuntimeException e) {
                    System.out.println("Введите id задачи:");
                }
            }
            if (!manage.getTaskAll().isEmpty() && task != null) {
                task = manage.getTaskById(task.getId());
                Epic epic = manage.getEpicById(task.getTaskId());
                for (Subtask subtask : manage.getListSubtaskIdEpic(epic.getId())) {
                    manage.deleteSubtaskById(subtask.getId());
                }
                manage.deleteEpicById(epic.getId());
                manage.deleteTaskById(task.getId());
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