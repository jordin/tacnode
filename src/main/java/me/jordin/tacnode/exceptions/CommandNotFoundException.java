package me.jordin.tacnode.exceptions;

/**
 * Created by Jordin on 8/21/2017.
 * Jordin is still best hacker.
 */
public class CommandNotFoundException extends Exception {
    public CommandNotFoundException(String command) {
        super(command);
    }
}
