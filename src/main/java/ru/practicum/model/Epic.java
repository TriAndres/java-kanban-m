package ru.practicum.model;

import java.util.ArrayList;

public class Epic extends Task {

    public Epic() {
        if (this.id == null || this.id == 0) {
            this.id = getId();
        }
        if (this.taskType == null) {
            this.taskType = TaskType.EPIC;
        }
        if (this.title == null) {
            this.title = "Эпик";
        }
        if (this.status == null) {
            this.status = Status.NEW;
        }
        if (this.subtaskIdList == null) {
            this.subtaskIdList = new ArrayList<>();
        }
    }

    public Epic(Long id, TaskType taskType, String title, Status status, String description, Long idTask) {
        this.id = id;
        this.taskType = taskType;
        this.title = title;
        this.status = status;
        this.description = description;
        this.taskId = idTask;
        if (this.id == null || this.id == 0) {
            this.id = getId();
        }
        if (this.taskType == null) {
            this.taskType = TaskType.EPIC;
        }
        if (this.title == null) {
            this.title = "Эпик";
        }
        if (this.status == null) {
            this.status = Status.NEW;
        }
        if (this.subtaskIdList == null) {
            this.subtaskIdList = new ArrayList<>();
        }
    }

    @Override
    public String toString() {
        return id +
                "/" + taskType +
                "/" + title  +
                "/" + status +
                "/" + description +
                "/" + taskId +
                "\n";
    }
}