package ru.practicum.history;

import ru.practicum.model.Task;

import java.util.*;

public class inMemoryHistoryManager implements HistoryManager{
    private final Map<Long, Node<Task>> historyMap = new HashMap<>();
    private Node<Task> first;
    private Node<Task> last;

    @Override
    public void addHistory(Task task) {
        if (Objects.nonNull(task)) {
            long id = task.getId();
            if (historyMap.containsKey(id)) {
                 removeHistory(id);
            }
            historyMap.put(id, linkLast(task));
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
        List<Task> history = new ArrayList<>();
        Node<Task> saved = first;
        if (first != null) {
            history.add(first.element);
            while (saved.next != null) {
                saved = saved.next;
                history.add(saved.element);
            }
        }
        return history;
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

    private void removeNode(Node<Task> x) {
        if (x != null) {
            final Task element = x.element;
            final Node<Task> next = x.next;
            final Node<Task> prev = x.prev;

            if (prev == null) {
                first = next;
            } else {
                prev.next = next;
                x.prev = null;
            }

            if (next == null) {
                last = prev;
            } else {
                next.prev = prev;
                x.next = null;
            }
            x.element = null;
        }
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