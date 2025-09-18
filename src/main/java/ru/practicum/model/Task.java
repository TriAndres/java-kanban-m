package ru.practicum.model;

import java.util.List;
import java.util.Objects;

public class Task {
    protected Long id;
    protected TaskType taskType;
    protected String title;
    protected Status status;
    protected String description;
    protected Long taskId;
    protected List<Long> subtaskIdList;

    public Task() {
//        if (getId() == null || getId() == 0) {
//            this.id = getId();
//        }
//        if (this.taskType == null) {
//            this.taskType = TaskType.TASK;
//        }
//        if (this.title == null) {
//            this.title = "Задача";
//        }
//        if (this.status == null) {
//            this.status = Status.NEW;
//        }
    }

    public Task(Long id, TaskType taskType, String title, Status status, String description) {
        this.id = id;
        this.taskType = taskType;
        this.title = title;
        this.status = status;
        this.description = description;
        if (this.id == null || this.id == 0) {
            this.id = getId();
        }
        if (this.taskType == null) {
            this.taskType = TaskType.TASK;
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

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        return Objects.equals(id, task.id) && taskType == task.taskType && Objects.equals(title, task.title) && status == task.status && Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, taskType, title, status, description);
    }

    @Override
    public String toString() {
        return id +
                "/" + taskType +
                "/" + title +
                "/" + status +
                "/" + description +
                "\n";
    }
}