package ru.practicum.history;

import ru.practicum.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Long, Node<Task>> historyMap = new HashMap<>();
    private Node<Task> first;
    private Node<Task> last;

    @Override
    public void addHistory(Task task) {
        if (Objects.nonNull(task)) {
            long id = task.getId();
            if (historyMap.containsKey(id)) {
                removeHistory(id);
            } else {
                historyMap.put(id, linkLast(task));
            }
        }

    }

    @Override
    public void removeHistory(long id) {
        if (historyMap.containsKey(id)) {
            removeNode(historyMap.get(id));
            historyMap.remove(id);
        }
    }

    @Override
    public List<Task> getHistoryMap() {
        List<Task> tasks = new ArrayList<>();
        Node<Task> saved = first;
        if (first != null) {
            tasks.add(first.element);
            while (saved.next != null) {
                saved = saved.next;
                tasks.add(saved.element);
            }
        }
        return new ArrayList<>(tasks);
    }

    private Node<Task> linkLast(Task task) {
        if (historyMap.isEmpty()) {
            final Node<Task> f = first;
            final Node<Task> newNode = new Node<>(null, task, f);
            first = newNode;
            if (f == null)
                last = newNode;
            else
                f.prev = newNode;
            return newNode;
        } else {
            final Node<Task> l = last;
            final Node<Task> newNode = new Node<>(l, task, null);
            last = newNode;
            if (l == null)
                first = newNode;
            else
                l.next = newNode;
            return newNode;
        }
    }

    private void removeNode(Node<Task> task) {
        final Task element = task.element;
        final Node<Task> prev = task.prev;
        task.element = null;
        task.prev = null; // help GC
        last = prev;
        if (prev == null)
            first = null;
        else
            prev.next = null;
        historyMap.remove(element.getId());
    }

    private static class Node<E extends Task> {
        E element;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.element = element;
            this.next = next;
            this.prev = prev;
        }
    }
}