package ru.yandex.practicum.catsgram.exception;

public class UserNotFoundException extends RuntimeException {
    UserNotFoundException(String msg) {
        super(msg);
    }
}
