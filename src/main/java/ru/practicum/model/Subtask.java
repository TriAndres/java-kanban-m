package ru.practicum.model;

public class Subtask extends Task {

    public Subtask() {
    }

    public Subtask(Long id, TaskType taskType, String title, Status status, String description, Long idEpic) {
        this.id = id;
        this.taskType = taskType;
        this.title = title;
        this.status = status;
        this.description = description;
        this.taskId = idEpic;
        if (this.id == null || this.id == 0) {
            this.id = getId();
        }
        if (this.taskType == null) {
            this.taskType = TaskType.SUBTASK;
        }
        if (title == null) {
            this.title = "Подзадача";
        }
        if (this.status == null) {
            this.status = Status.NEW;
        }
    }

    @Override
    public String toString() {
        return id +
                "/" + taskType +
                "/" + title +
                "/" + status +
                "/" + description +
                "/" + taskId +
                "\n";
    }
}