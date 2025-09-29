package ru.practicum.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {

    public Subtask() {
        this.id = getId();
        this.type = TaskType.SUBTASK;
        this.name = "Подзадача";
        this.status = Status.NEW;
        this.duration = Duration.ZERO;
        this.startTime = LocalDateTime.now();
        this.endTime = LocalDateTime.now();
    }

    public Subtask(Long id, TaskType type, String name, Status status, String description, Duration duration, LocalDateTime startTime, LocalDateTime endTime,  Long idEpic) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.status = status;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
        this.endTime = endTime;
        this.taskId = idEpic;
        if (this.id == null || this.id == 0) {
            this.id = getId();
        }
        if (this.type == null) {
            this.type = TaskType.SUBTASK;
        }
        if (this.name == null) {
            this.name = "Подзадача";
        }
        if (this.status == null) {
            this.status = Status.NEW;
        }
        if (this.duration == null) {
            this.duration = Duration.ZERO;
        }
        if (this.startTime == null) {
            this.startTime = LocalDateTime.now();
        }
        if (this.endTime == null) {
            this.endTime = LocalDateTime.now();
        }
    }
}