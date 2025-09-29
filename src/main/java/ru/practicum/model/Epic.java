package ru.practicum.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {

    public Epic() {
        this.id = getId();
        this.type = TaskType.EPIC;
        this.name = "Эпик";
        this.status = Status.NEW;
        this.duration = Duration.ZERO;
        this.startTime = LocalDateTime.now();
        this.endTime = LocalDateTime.now();
        if (this.subtaskIdList == null) {
            this.subtaskIdList = new ArrayList<>();
        }
    }

    public Epic(Long id, TaskType type, String name, Status status, String description, Duration duration, LocalDateTime startTime, LocalDateTime endTime, Long idTask) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.status = status;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
        this.endTime = endTime;
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
        if (this.duration == null) {
            this.duration = Duration.ZERO;
        }
        if (this.startTime == null) {
            this.startTime = LocalDateTime.now();
        }
        if (this.endTime == null) {
            this.endTime = LocalDateTime.now();
        }
        if (this.subtaskIdList == null) {
            this.subtaskIdList = new ArrayList<>();
        }
    }
}