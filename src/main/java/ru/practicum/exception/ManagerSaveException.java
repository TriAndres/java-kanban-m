package ru.practicum.exception;

import java.io.File;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(String message) {
        super(message);
    }

    public ManagerSaveException() {
        super();
    }
}
