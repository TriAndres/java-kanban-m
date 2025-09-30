package ru.practicum;

import ru.practicum.controller.Managers;
import ru.practicum.memory.TaskManage;
import ru.practicum.model.Epic;
import ru.practicum.model.Subtask;
import ru.practicum.model.Task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Scanner;

import static ru.practicum.controller.Managers.detDefaultFile;
import static ru.practicum.model.Status.DONE;
import static ru.practicum.model.Status.NEW;


public class Main {
//
//    public static void main(String[] args) {
//
//         final TaskManage manage = detDefaultFile();
//        Epic epic = new Epic(
//                null,
//                null,
//                null,
//                null,
//                "описание эпика 1",
//                null,
//                null,
//                null,
//                0L
//        );
//        manage.createEpic(epic);
//        Epic epic1 = manage.getEpicById(1L);
//        System.out.println(epic1);

////Данная задача должна быть 4 по счёту
//        Subtask subtask1 = new Subtask(
//                null,
//                null,
//                "новая подзадача 1",
//                Status.NEW,
//                "описание подзадачи 1",
//                Duration.ofMinutes(15),
//                LocalDateTime.of(2022, 12, 30, 0, 30).plusDays(2),
//                null,
//                epic.getId()
//        );
//        manage.createSubtask(subtask1);
//        Subtask subtask11 = manage.getSubtaskById(2L);
//        System.out.println(subtask11);
////Данная задача должна быть 1 по счёту
//        Subtask subtask2 = new Subtask(
//                null,
//                null,
//                "новая подзадача 2",
//                Status.NEW,
//                "описание подзадачи 2",
//                Duration.ofMinutes(30),
//                LocalDateTime.of(2022, 12, 30, 0, 30),
//                null,
//                epic.getId()
//        );
//        manage.createSubtask(subtask2);
//        Subtask subtask22 = manage.getSubtaskById(3L);
//        System.out.println(subtask22);
////Данная задача должна быть 3 по счёту
//        Subtask subtask3 = new Subtask(
//                null,
//                null,
//                "новая подзадача 3",
//                Status.NEW,
//                "описание подзадачи 3",
//                Duration.ofMinutes(45),
//                LocalDateTime.of(2022, 12, 30, 0, 30).plusDays(1),
//                null,
//                epic.getId()
//        );
//        manage.createSubtask(subtask3);
//        Subtask subtask33 = manage.getSubtaskById(3L);
//        System.out.println(subtask33);
////Данная задача должна быть 2 по счёту
//        Subtask subtask4 = new Subtask(null,
//                null,
//                "новая подзадача 4",
//                Status.NEW,
//                "описание подзадачи 4",
//                Duration.ofMinutes(60),
//                LocalDateTime.of(2022, 12, 30, 0, 30).plusHours(12),
//                null,
//                epic.getId()
//        );
//        manage.createSubtask(subtask4);
//        Subtask subtask44 = manage.getSubtaskById(4L);
//        System.out.println(subtask44);
    /// /Порядок добавления следующий subtask1 -> subtask2 -> subtask3 -> subtask4
    /// /Порядок следующий subtask2 -> subtask4 -> subtask3 -> subtask1
//        System.out.println(epic.getDuration() + " == " + (subtask1.getDuration().plus(subtask2.getDuration().plus(subtask3.getDuration())).plus(subtask4.getDuration()) + " " + (epic.getDuration().equals(subtask1.getDuration().plus((subtask2.getDuration().plus((subtask3.getDuration()).plus(subtask4.getDuration()))))))));
//        System.out.println(epic.getStartTime() + " == " + subtask2.getStartTime() + " " + epic.getStartTime().equals(subtask2.getStartTime()));
//        System.out.println(epic.getEndTime() + " == " + subtask1.getEndTime() + " " + epic.getEndTime().equals(subtask1.getEndTime()));
//        System.out.println(manage.getPrioritizedTaskList().stream().map(Task::getName).collect(Collectors.toList()));
    //   }


    private final Scanner scanner = Managers.scanner();
    private final TaskManage manage = detDefaultFile();
    protected final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

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
                    epic = manage.getEpicById(manage.getTaskById(id).getTaskId());
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
//        if (manage.getSubtaskAll() != null) {
//            System.out.println("Введите id подзадачи:");
//            long id = 0L;
//            while (true) {
//                if (scanner.hasNextLong()) {
//                    id = scanner.nextLong();
//                    if (manage.getSubtaskAll().contains(manage.getSubtaskById(id))) {
//                        break;
//                    } else if (id == -1L) {
//                        System.out.println("-1 выход из меню");
//                        break;
//                    }
//
//                } else {
//                    scanner.nextLine();
//                }
//            }
//            if (manage.getSubtaskAll().contains(manage.getSubtaskById(id))) {
//                Subtask subtask = manage.getSubtaskById(id);
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
//                    manage.updateSubtask(subtask);
//                    System.out.println("Записано:\n" + subtask.toString());
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

    private void showStatisticTaskId() {
//        if (manage.getTaskAll() != null) {
//            System.out.println("Введите id задачи:");
//            long id = 0L;
//            while (true) {
//                if (scanner.hasNextLong()) {
//                    id = scanner.nextLong();
//                    if (manage.getTaskAll().contains(manage.getTaskById(id))) {
//                        break;
//                    } else if (id == -1L) {
//                        System.out.println("-1 выход из меню");
//                        break;
//                    }
//
//                } else {
//                    scanner.nextLine();
//                }
//            }
//            if (manage.getTaskAll().contains(manage.getTaskById(id))) {
//                Task task = manage.getTaskById(id);
//                System.out.println(task.toString());
//                Epic epic = manage
//                        .getEpicAll()
//                        .stream()
//                        .filter(i -> Objects.equals(i.getTaskId(), task.getId()))
//                        .toList()
//                        .getFirst();
//                manage.getListSubtaskIdEpic(epic.getId()).forEach(s -> System.out.println(s.toString()));
//            }
//        } else {
//            System.out.println("addStatusSubtask() -> exception");
//        }
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
//        if (!manage.getHistory().isEmpty() && manage.getHistory() != null) {
//            manage.getHistory().forEach(h -> System.out.println(h.toString()));
//            System.out.println("getHistory()\n");
//        } else {
//            System.out.println("Список пуст, добавьте задачу.\n");
//        }
    }

    private void deleteTaskById() {
//        if (manage.getTaskAll() != null) {
//            System.out.println("Введите id задачи:");
//            long id = 0L;
//            while (true) {
//                if (scanner.hasNextLong()) {
//                    id = scanner.nextLong();
//                    if (manage.getTaskAll().contains(manage.getTaskById(id))) {
//                        break;
//                    } else if (id == -1L) {
//                        System.out.println("-1 выход из меню");
//                        break;
//                    }
//                } else {
//                    scanner.nextLine();
//                }
//            }
//            if (manage.getTaskAll().contains(manage.getTaskById(id))) {
//                Task task = manage.getTaskById(id);
//                Epic epic = manage
//                        .getEpicAll()
//                        .stream()
//                        .filter(i -> Objects.equals(i.getTaskId(), task.getId()))
//                        .toList()
//                        .getFirst();
//                if (manage.getListSubtaskIdEpic(epic.getId()) != null) {
//                    manage.getListSubtaskIdEpic(epic.getId()).forEach(s -> System.out.println(s.toString()));
//                }
//                manage.deleteEpicById(epic.getId());
//                manage.deleteTaskById(id);
//            }
//        } else {
//            System.out.println("Список пуст, добавте подзадачу.");
//        }
    }

    private void deleteTaskAll() {
//        manage.deleteTaskAll();
//        manage.deleteEpicAll();
//        manage.deleteSubtaskAll();
    }
}