package ru.practicum.model;

public class Subtask extends Task {

    public Subtask() {
    }

    public Subtask(Long id, TaskType type, String name, Status status, String description, Long idEpic) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.status = status;
        this.description = description;
        this.taskId = idEpic;
        setType(TaskType.SUBTASK);
        setName("Подзадача");
        setStatus(Status.NEW);
    }
}