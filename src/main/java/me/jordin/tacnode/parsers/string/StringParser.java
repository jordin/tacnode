package me.jordin.tacnode.parsers.string;

import me.jordin.tacnode.parsers.ArgumentParser;

import java.util.Iterator;

/**
 * Created by Jordin on 8/9/2017.
 * Jordin is still best hacker.
 */
public class StringParser implements ArgumentParser<String> {
    @Override
    public String parse(Iterator<String> arguments) {
        return arguments.next();
    }
}
