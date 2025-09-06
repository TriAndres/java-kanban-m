package ru.practicum.manage;

import ru.practicum.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Long, Node> history = new HashMap<>();
    private Node head;
    private Node tail;

    @Override
    public void addHistory(Task task) {
        if (task != null) {
            if (history.containsKey(task.getId())) {
                removeNode(history.remove(task.getId()));
            }
            linkLast(task);
        }
    }

    @Override
    public void removeHistory(long id) {
        if (history.containsKey(id)) {
            removeNode(history.remove(id));
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(getTasks());
    }

    private void linkLast(Task task) {
        if (history.isEmpty()) {
            Node firstNode = new Node(task, null, null);
            head = firstNode;
            tail = firstNode;
            history.put(task.getId(), firstNode);
        } else {
            Node nextNode = new Node(task, tail, null);
            tail.nextTask = nextNode;
            history.put(tail.task.getId(), tail);
            tail = nextNode;
            history.put(task.getId(),nextNode);
        }
    }

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node saved = head;
        if (head != null) {
            tasks.add(head.task);
            while (saved.nextTask != null) {
                saved = saved.nextTask;
                tasks.add(saved.task);
            }
        }
        return new ArrayList<>(tasks);
    }

    private void removeNode(Node node) {
        if (node.equals(head) && node.equals(tail)) {
            head = null;
            tail = null;
        } else if (node.equals(head)) {
            node.nextTask.prevTask = null;
            head = node.nextTask;
        } else if (node.equals(tail)) {
            node.prevTask.nextTask = null;
            tail = node.prevTask;
        } else {
            node.prevTask.nextTask = node.nextTask;
            node.nextTask.prevTask = node.prevTask;
        }
        history.remove(node.task.getId());
    }
}