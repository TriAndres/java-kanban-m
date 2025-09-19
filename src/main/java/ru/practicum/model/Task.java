package ru.practicum.model;

import java.util.List;
import java.util.Objects;

public class Task {
    protected Long id;
    protected TaskType type;
    protected String name;
    protected Status status;
    protected String description;
    protected Long taskId;
    protected List<Long> subtaskIdList;

    public Task() {
    }

    public Task(Long id, TaskType type, String name, Status status, String description, Long taskId) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.status = status;
        this.description = description;
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
        return Objects.equals(id, task.id) && type == task.type && Objects.equals(name, task.name) && status == task.status && Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, name, status, description);
    }

    @Override
    public String toString() {
        return id +
                "/" + type +
                "/" + name +
                "/" + status +
                "/" + taskId +
                "\n";
    }
}