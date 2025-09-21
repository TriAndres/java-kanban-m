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
        setType(TaskType.EPIC);
        setName("Эпик");
        setStatus(Status.NEW);
        setSubtaskIdList(new ArrayList<>());
    }
}