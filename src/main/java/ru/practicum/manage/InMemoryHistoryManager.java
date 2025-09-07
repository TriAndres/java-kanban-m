package ru.practicum.manage;

import ru.practicum.model.Node;
import ru.practicum.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Long, Node<Task>> history = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;

    @Override
    public void addHistory(Task task) {
        if (task != null) {
            long id = task.getId();
            if (history.containsKey(id)) {
                removeNode(history.remove(id));
            }
            //linkLast(task);
            history.put(id, linkLast(task));
        }
    }

    @Override
    public void removeHistory(long id) {
        if (history.containsKey(id)) {
            removeNode(history.remove(id));
            history.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> list = new ArrayList<>();
        Node<Task> newNode = head;
        while (newNode != null) {
            list.add(newNode.getDate());
            newNode = newNode.getNext();
        }
        return list;
    }

    //private void linkLast(Task task) {
    private Node<Task> linkLast(Task task) {
//        if (history.isEmpty()) {
//            Node firstNode = new Node(task, null, null);
//            head = firstNode;
//            tail = firstNode;
//            history.put(task.getId(), firstNode);
//        } else {
//            Node nextNode = new Node(task, tail, null);
//            tail.setNext(nextNode);
//            history.put(tail.getDate().getId(), tail);
//            tail = nextNode;
//            history.put(task.getId(), nextNode);
//        }

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
        history.remove(node.getDate().getId());
    }
}