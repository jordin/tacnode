package me.jordin.tacnode.parsers;

/**
 * Created by Jordin on 8/10/2017.
 * Jordin is still best hacker.
 */
public interface ArgumentParserProvider {
    ArgumentParser<?> getParser(Class<?> clazz);
}
