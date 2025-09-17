package ru.practicum.history;

import ru.practicum.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Long, Node<Task>> historyMap = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;

    @Override
    public void addHistory(Task task) {
        // добаляет новую запись в хешмап
        if (Objects.nonNull(task)) {
            long id = task.getId();
            if (!historyMap.isEmpty() && historyMap.containsKey(id)) {
                removeHistory(id);
            }
            historyMap.put(id, linkLast(task));
        }
    }

    @Override
    public void removeHistory(long id) {
        //удаляет задачу из приложения и истории
        if (historyMap.containsKey(id)) {
            removeNode(historyMap.get(id));
            historyMap.remove(id);
        }
    }

    @Override
    public List<Task> getHistoryMap() {
        //собирает все задачи в обычный ArrayList
        List<Task> list = new ArrayList<>();
        Node<Task> newNode = head;
        while (Objects.nonNull(newNode)) {
            list.add(newNode.getData());
            newNode = newNode.getNext();
        }
        return list;
    }

    private Node<Task> linkLast(Task task) {
        if (head == null) {
            head = new Node<>(task, null, null);
            return head;
        } else if (tail == null) {
            tail = new Node<>(task, null, head);
            head.setNext(tail);
            return tail;
        } else {
            final Node<Task> oldTail = tail;
            final Node<Task> newNode = new Node<>(task, null, oldTail);
            oldTail.setNext(newNode);
            tail = newNode;
            return newNode;
        }
    }

    private void removeNode(Node<Task> node) {
        if (node.equals(head)) {
            Node<Task> newHead = head.getNext();
            newHead.setPrev(null);
            head = newHead;
        } else if (node.equals(tail)) {
            Node<Task> newTail = tail.getPrev();
            newTail.setNext(null);
            tail = newTail;
        } else {
            Node<Task> tempNext = node.getNext();
            Node<Task> tempPrev = node.getPrev();
            tempNext.setPrev(tempPrev);
            tempPrev.setNext(tempNext);
        }
    }

    private static class Node<T extends Task> {
        private final T data;
        private Node<T> next;
        private Node<T> prev;

        public Node(T data, Node<T> next, Node<T> prev) {
            this.data = data;
            setNext(next);
            setPrev(prev);
        }

        public T getData() {
            return data;
        }

        public Node<T> getNext() {
            return next;
        }

        public void setNext(Node<T> next) {
            this.next = next;
        }

        public Node<T> getPrev() {
            return prev;
        }

        public void setPrev(Node<T> prev) {
            this.prev = prev;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Node)) return false;
            Node<?> node = (Node<?>) o;
            return data.equals(node.data) && Objects.equals(next, node.next) && Objects.equals(prev, node.prev);
        }

        @Override
        public int hashCode() {
            return Objects.hash(data, next, prev);
        }
    }
}



/*
public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Long, Node<Task>> historyMap = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;

    @Override
    public void addHistory(Task task) {
        if (task != null) {
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
            //historyMap.remove(id);
        }
    }

    @Override
    public List<Task> getHistoryMap() {
        List<Task> list = new ArrayList<>();
        Node<Task> newNode = head;
        while (newNode != null) {
            list.add(newNode.getDate());
            newNode = newNode.getNext();
        }
        return list;
    }

    private Node<Task> linkLast(Task task) {
        if (head == null) {
            head = new Node<>(task, null, null);
            return head;
        } else if (tail == null) {
            tail = new Node<>(task, null, head);
            head.setNext(tail);
            return tail;
        } else {
            final Node<Task> oldTail = tail;
            final Node<Task> newNode = new Node<>(task, null, oldTail);
            oldTail.setNext(newNode);
            tail = newNode;
            return newNode;
        }
    }

    private void removeNode(Node<Task> node) {
        if (node.equals(head) && node.equals(tail)) {
            head = null;
            tail = null;
        } else if (node.equals(head)) {
            node.getNext().setPrev(null);
            head = node.getNext();
        } else if (node.equals(tail)) {
            node.getPrev().setNext(null);
            tail = node.getPrev();
        } else {
            node.getPrev().setNext(node.getNext());
            node.getNext().setPrev(node.getPrev());
        }
        historyMap.remove(node.getDate().getId());
    }
}

 */