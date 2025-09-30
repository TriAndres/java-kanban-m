package ru.practicum.model;

public class Subtask extends Task {

    public Subtask() {
    }

    public Subtask(Long id,
                   TaskType type,
                   String name,
                   Status status,
                   String description,
                   String duration,
                   String startTime,
                   String endTime,
                   Long idEpic) {
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
    }
}