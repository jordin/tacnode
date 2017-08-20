package me.jordin.tacnode.exceptions;

/**
 * Created by Jordin on 8/9/2017.
 * Jordin is still best hacker.
 */
public class InvalidTypeException extends Exception {
    private static final String MESSAGE = "Invalid argument: expected \"%s\".";

    public InvalidTypeException(String expected) {
        super(String.format(MESSAGE, expected));
    }

    public InvalidTypeException(String expected, Throwable cause) {
        super(String.format(MESSAGE, expected), cause);
    }
}
