package ru.practicum.model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Subtask> subtasks;
    private Long idTask;

    public Epic() {
        this(null,null,null,null, null, null);
        if (getTitle() == null) {
            this.title = "Эпик";
        }
        if (getStatus() == null) {
            this.status = Status.NEW;
        }
    }

    public Epic(Long id, String title, String description, Status status, List<Subtask> subtasks, Long idTask) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.subtasks = subtasks;
        this.idTask = idTask;
        if (this.id == null || this.id == 0) {
            this.id = getId();
        }
        if (this.title == null) {
            this.title = "Эпик";
        }
        if (this.status == null) {
            this.status = Status.NEW;
        }
        if (this.subtasks == null) {
            this.subtasks = new ArrayList<>();
        }
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    public Long getIdTask() {
        return idTask;
    }

    public void setIdTask(Long idTask) {
        this.idTask = idTask;
    }
}