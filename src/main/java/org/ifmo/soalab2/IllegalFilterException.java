package org.ifmo.soalab2;

public class IllegalFilterException extends IllegalArgumentException {
    public IllegalFilterException(String filterName, String value) {
        super("Неверное значение фильтра " + filterName + ": " + value);
    }
}
