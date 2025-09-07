package ru.practicum.model;

import java.util.Objects;

public class Node<T extends  Task> {
    private T date;
    private Node<T> next;
    private Node<T> prev;

    public Node(T date, Node<T> next, Node<T> prev) {
        this.date = date;
        this.next = next;
        this.prev = prev;
    }

    public T getDate() {
        return date;
    }

    public void setDate(T date) {
        this.date = date;
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
        if (o == null || getClass() != o.getClass()) return false;
        Node<?> node = (Node<?>) o;
        return Objects.equals(date, node.date) && Objects.equals(next, node.next) && Objects.equals(prev, node.prev);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, next, prev);
    }
}