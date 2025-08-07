package ru.practicum;

import ru.practicum.manage.TaskManageImpl;
import ru.practicum.model.Epic;
import ru.practicum.model.Status;
import ru.practicum.model.Subtask;
import ru.practicum.model.Task;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        TaskManageImpl manageImpl = new TaskManageImpl();

        System.out.println("\nСоздаём 1 задачу задачи");
        System.out.println(manageImpl.createTask(new Task(
                null,
                null,
                "*** 1 ЗАДАЧА ***",
                null
        )));
        Task task1 = manageImpl.getTaskById(1L);

        System.out.println("\nСоздайте эпик 1 с 2 подзадачами");
        System.out.println(manageImpl.createEpic(new Epic(
                null,
                null,
                "*** 1 эпик, 1 задачи ***",
                null,
                new ArrayList<>()
        )));
        Epic epic1 = manageImpl.getEpicById(1L);

        System.out.println(manageImpl.createSubtask(new Subtask(
                null,
                null,
                "*** 1 подзадача , 1 эпика ***",
                null,
                epic1.getId()
        )));
        Subtask subtask1 = manageImpl.getSubtaskById(1L);

        System.out.println(manageImpl.createSubtask(new Subtask(
                null,
                null,
                "*** 2 подзадача, 1 эпика ***",
                null,
                epic1.getId()
        )));
        Subtask subtask2 = manageImpl.getSubtaskById(2L);

        System.out.println("\nСоздаём 2 задачу задачи");
        System.out.println(manageImpl.createTask(new Task(
                null,
                null,
                "*** 2 ЗАДАЧА ***",
                null
        )));
        Task task2 = manageImpl.getTaskById(2L);

        System.out.println("\nСоздайте эпик 2 с 1 подзадачей");
        System.out.println(manageImpl.createEpic(new Epic(
                null,
                null,
                "*** 2 эпик, 1 задачи ***",
                null,
                new ArrayList<>()
        )));
        Epic epic2 = manageImpl.getEpicById(2L);

        System.out.println(manageImpl.createSubtask(new Subtask(
                null,
                null,
                "*** 1 подзадача, 2 эпика***",
                null,
                epic2.getId()
        )));
        Subtask subtask3 = manageImpl.getSubtaskById(3L);

        System.out.println("\n///Вывод списков");
        for (Task task : manageImpl.getTaskAll()) {
            System.out.println(task.toString());
        }

        for (Epic epic : manageImpl.getEpicAll()) {
            System.out.println(epic.toString());
        }

        for (Subtask subtask : manageImpl.getSubtaskAll()) {
            System.out.println(subtask.toString());
        }
        System.out.println("\n//////////////////////////////");
        System.out.println("К 1 задаче, 1 эпика, к первой подзадаче записываем DONE");

        subtask1.setStatus(Status.DONE);
        manageImpl.updateSubtask(subtask1);

        System.out.println("\n///Вывод списков");
        for (Task task : manageImpl.getTaskAll()) {
            System.out.println(task.toString());
        }

        for (Epic epic : manageImpl.getEpicAll()) {
            System.out.println(epic.toString());
        }

        for (Subtask subtask : manageImpl.getSubtaskAll()) {
            System.out.println(subtask.toString());
        }
        System.out.println("\n//////////////////////////////");
        System.out.println("К 1 задаче, 1 эпика, к первой подзадаче записываем DONE");

        subtask2.setStatus(Status.DONE);
        manageImpl.updateSubtask(subtask1);

        System.out.println("\n///Вывод списков");
        for (Task task : manageImpl.getTaskAll()) {
            System.out.println(task.toString());
        }

        for (Epic epic : manageImpl.getEpicAll()) {
            System.out.println(epic.toString());
        }

        for (Subtask subtask : manageImpl.getSubtaskAll()) {
            System.out.println(subtask.toString());
        }
        System.out.println("\n//////////////////////////////");
        System.out.println("Обновляем обратно до  NEW");

        subtask2.setStatus(Status.NEW);
        manageImpl.updateSubtask(subtask1);

        System.out.println("\n///Вывод списков");
        for (Task task : manageImpl.getTaskAll()) {
            System.out.println(task.toString());
        }

        for (Epic epic : manageImpl.getEpicAll()) {
            System.out.println(epic.toString());
        }

        for (Subtask subtask : manageImpl.getSubtaskAll()) {
            System.out.println(subtask.toString());
        }
        System.out.println("\n//////////////////////////////");
        System.out.println("Удалить 1 задачу, удалить 1 эпик");

        manageImpl.deleteTaskById(1L);
        manageImpl.deleteEpicById(1L);

        System.out.println("\n///Вывод списков");
        for (Task task : manageImpl.getTaskAll()) {
            System.out.println(task.toString());
        }

        for (Epic epic : manageImpl.getEpicAll()) {
            System.out.println(epic.toString());
        }

        for (Subtask subtask : manageImpl.getSubtaskAll()) {
            System.out.println(subtask.toString());
        }
        System.out.println("\n//////////////////////////////");
        System.out.println("Удалить все задачу, удалить все эпик");

        manageImpl.deleteTaskAll();
        manageImpl.deleteEpicAll();

        System.out.println("\n///Вывод списков");
        for (Task task : manageImpl.getTaskAll()) {
            System.out.println(task.toString());
        }

        for (Epic epic : manageImpl.getEpicAll()) {
            System.out.println(epic.toString());
        }

        for (Subtask subtask : manageImpl.getSubtaskAll()) {
            System.out.println(subtask.toString());
        }
    }
}