package ru.yandex.practicum.filmorate.exeption;

public class ConditionsNotMetException extends RuntimeException{
    public ConditionsNotMetException(String message){
        super(message);
    }
}
