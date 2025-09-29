package ru.practicum.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class Task {
    protected Long id;
    protected TaskType type;
    protected String name;
    protected Status status;
    protected String description;
    protected Duration duration;
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;
    protected Long taskId;
    protected List<Long> subtaskIdList;
    protected final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public Task() {
        this.id = getId();
        this.type = TaskType.TASK;
        this.name = "Задача";
        this.status = Status.NEW;
        this.duration = Duration.ZERO;
        this.startTime = LocalDateTime.now();
        this.endTime = LocalDateTime.now();
    }


    public Task(Long id, TaskType type, String name, Status status, String description, Duration duration, LocalDateTime startTime, LocalDateTime endTime, Long taskId) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.status = status;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
        this.endTime = endTime;
        this.taskId = taskId;
        if (this.id == null || this.id == 0) {
            this.id = getId();
        }
        if (this.type == null) {
            this.type = TaskType.TASK;
        }
        if (this.name == null) {
            this.name = "Задача";
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = startTime;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public List<Long> getSubtaskIdList() {
        return subtaskIdList;
    }

    public void setSubtaskIdList(List<Long> subtaskIdList) {
        this.subtaskIdList = subtaskIdList;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && type == task.type && Objects.equals(name, task.name) && status == task.status && Objects.equals(description, task.description) && Objects.equals(duration, task.duration) && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, name, status, description, duration, startTime);
    }

    @Override
    public String toString() {
        return id +
                "/" + type +
                "/" + name +
                "/" + status +
                "/" + description +
                "/" + duration +
                "/" + startTime +
                "/" + endTime +
                "/" + taskId +
                "\n";
    }
}