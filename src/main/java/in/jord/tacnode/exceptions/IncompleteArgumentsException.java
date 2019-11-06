package in.jord.tacnode.exceptions;

import in.jord.tacnode.parsers.ArgumentParser;

/**
 * Created by Jordin on 8/10/2017.
 * Jordin is still best hacker.
 */
public class IncompleteArgumentsException extends Exception {
    private Class<?> clazz;
    private ArgumentParser<?> parser;

    public IncompleteArgumentsException(Class<?> clazz, ArgumentParser<?> parser) {
        this.clazz = clazz;
        this.parser = parser;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public ArgumentParser<?> getParser() {
        return parser;
    }
}
