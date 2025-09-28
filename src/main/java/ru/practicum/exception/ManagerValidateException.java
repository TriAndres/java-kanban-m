package ru.practicum.exception;

public class ManagerValidateException extends RuntimeException {
    public ManagerValidateException(String message) {
        super(message);
    }
}