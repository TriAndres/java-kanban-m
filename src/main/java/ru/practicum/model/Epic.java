package ru.practicum.model;

import java.util.ArrayList;

public class Epic extends Task {

    public Epic() {
    }

    public Epic(Long id, TaskType type, String name, Status status, String description, Long idTask) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.status = status;
        this.description = description;
        this.taskId = idTask;
        if (this.id == null || this.id == 0) {
            this.id = getId();
        }
        if (this.type == null) {
            this.type = TaskType.EPIC;
        }
        if (this.name == null) {
            this.name = "Эпик";
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
                "/" + type +
                "/" + name +
                "/" + status +
                "/" + description +
                "/" + taskId +
                "\n";
    }
}