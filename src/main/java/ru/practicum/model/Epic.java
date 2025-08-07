package ru.practicum.model;

import java.util.List;

public class Epic extends Task {
    private List<Subtask> subtasks;

    public Epic() {
        if (getTitle() == null) {
            this.title = "Эпик";
        }
        if (getStatus() == null) {
            this.status = Status.NEW;
        }
    }

    public Epic(Long id, String title, String description, Status status, List<Subtask> subtasks) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.subtasks = subtasks;
        if (this.id == null || this.id == 0) {
            this.id = getId();
        }
        if (this.title == null) {
            this.title = "Эпик";
        }
        if (this.status == null) {
            this.status = Status.NEW;
        }
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<Subtask> subtasks) {
        this.subtasks = subtasks;
    }
}