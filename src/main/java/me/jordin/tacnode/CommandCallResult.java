package me.jordin.tacnode;

public class CommandCallResult<T> {
    private final boolean success;
    private final T data;

    public CommandCallResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }
}
