package ru.javakids.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
    NOT_STARTED("Не начиналась"),
    IN_PROCESS("В процессе"),
    SENT_FOR_REVIEW("Отправлена на проверку"),
    NOT_CREDITED("Не зачтено"),
    FINISHED("Завершена");

    private final String description;

    @Override
    public String toString() {
        return description;
    }
}
