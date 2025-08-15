package ru.practicum.model;

public class Subtask extends Task {
    private Long idEpic;

    public Subtask() {
        this(null,null,null,null,null);
        if (title == null) {
            this.title = "Подзадача";
        }
        if (this.status == null) {
            this.status = Status.NEW;
        }
    }

    public Subtask(Long id, String title, String description, Status status, Long idEpic) {
        this.idEpic = idEpic;
        if (this.id == null || this.id == 0) {
            this.id = getId();
        }
        if (title == null) {
            this.title = "Подзадача";
        }
        if (this.status == null) {
            this.status = Status.NEW;
        }
    }

    public Long getIdEpic() {
        return idEpic;
    }

    public void setIdEpic(Long idEpic) {
        this.idEpic = idEpic;
    }
}