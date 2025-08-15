package ru.practicum;

import ru.practicum.manage.TaskManage;
import ru.practicum.model.Epic;
import ru.practicum.model.Subtask;
import ru.practicum.model.Task;

import java.util.Scanner;

import static ru.practicum.manage.Managers.getDefault;

public class Main {
    TaskManage taskManage = getDefault();

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
                1 - вывод список задач.
                2 - добавить задачу.
                3 - добавить подзадачу.
                4 - вывод статистику задачи.
                5 - добавить статус подзадаче.
                6 - удалить задачу.
                7 - вывод статистику всех задач.
                8 - удалить все задачи.
                0 - вызод из программы.
                """);
        return new Scanner(System.in).next();
    }

    private void select(String line) {
        switch (line) {
            case "0":
                System.out.println("Выход из программы.");
                break;
            case "1":
                showTask();
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

    private void showTask() {
        if (!taskManage.getTaskAll().isEmpty()) {
            for (Task task : taskManage.getTaskAll()) {
                System.out.println(task.toString());
            }
            System.out.println("Вывод всех задач.\n");
        } else {
            System.out.println("Список пуст, добавте задачу.\n");
        }

    }

    private void addTask() {
        System.out.println("Введите название задачи:");
        String description = new Scanner(System.in).nextLine();
        Task task = new Task();
        task.setDescription(description);
        Epic epic = new Epic();
        epic.setDescription(description);
        System.out.println("Записано:\n" +
                taskManage.createTask(task) + "\n" +
                taskManage.createEpic(epic) + "\n");
    }

    private void addSubtask() {
        System.out.println("Введите id задачи для ввода подзадачи:");
        long id = Long.parseLong(new Scanner(System.in).next());
        if (taskManage.getTaskAll().contains(taskManage.getTaskById(id))) {
            Task task = taskManage.getTaskById(id);
            Epic epic = taskManage.getEpicById(id);
            Subtask subtask = new Subtask();
            subtask.setIdEpic(epic.getId());
            System.out.println("Введите название подзадачи:");
            String description = new Scanner(System.in).nextLine();
            subtask.setDescription(description);
            taskManage.createSubtask(subtask);
            System.out.println("Записано в задаче:\n" +
                    task.toString());
            System.out.println("Записано epic.getSubtasks");
            for (Subtask subtask1 : epic.getSubtasks()) {
                System.out.println(subtask1.toString());
            }
        }
    }

    private void showStatisticTaskId() {
        if (!taskManage.getTaskAll().isEmpty()) {
            System.out.println("Введите id задачи:");
            long id = Long.parseLong(new Scanner(System.in).next());
            if (taskManage.getTaskAll().contains(taskManage.getTaskById(id))) {
                Task task = taskManage.getTaskById(id);
                System.out.println(task.toString());
                Epic epic = taskManage.getEpicById(id);
                for (Subtask subtask : epic.getSubtasks()) {
                    System.out.println(subtask.toString());
                }
            }
        } else {
            System.out.println("Список пуст, добавте задачу.");
        }
    }

    private void addStatusSubtask() {
        System.out.println("В разработке.");
    }

    private void deleteTaskById() {
        System.out.println("В разработке.");
    }

    private void showStatisticTaskAll() {
        System.out.println("В разработке.");
    }

    private void deleteTaskAll() {
        System.out.println("В разработке.");
    }
}