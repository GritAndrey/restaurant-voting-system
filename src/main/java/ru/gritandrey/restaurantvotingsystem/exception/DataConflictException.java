package ru.gritandrey.restaurantvotingsystem.exception;

public class DataConflictException extends RuntimeException {
    public DataConflictException(String msg) {
        super(msg);
    }
}