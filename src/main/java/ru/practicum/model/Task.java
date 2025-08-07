package ru.practicum.model;

import java.util.Objects;

public class Task {
    protected Long id;
    protected String title;
    protected String description;
    protected Status status;

    public Task() {
        if (getTitle() == null) {
            this.title = "Задача";
        }
        if (getStatus() == null) {
            this.status = Status.NEW;
        }
    }

    public Task(Long id, String title, String description, Status status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        if (this.id == null || this.id == 0) {
            this.id = getId();
        }
        if (this.title == null) {
            this.title = "Задача";
        }
        if (this.status == null) {
            this.status = Status.NEW;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(title, task.title) && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, status);
    }

    @Override
    public String toString() {
        return id + "/" + title + "/" + description + "/" + status;
    }
}