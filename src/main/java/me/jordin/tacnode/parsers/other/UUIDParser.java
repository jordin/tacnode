package me.jordin.tacnode.parsers.other;

import com.google.common.collect.ImmutableList;
import me.jordin.tacnode.exceptions.InvalidTypeException;
import me.jordin.tacnode.parsers.ArgumentParser;

import java.util.*;

/**
 * Created by Jordin on 8/11/2017.
 * Jordin is still best hacker.
 */
public class UUIDParser implements ArgumentParser<UUID> {
    private static final UUID ZERO = UUID.fromString("00000000-0000-0000-0000-000000000000");

    @Override
    public UUID parse(Iterator<String> arguments) throws InvalidTypeException {
        return UUID.fromString(arguments.next());
    }

    @Override
    public List<String> provideSuggestions() {
        return ImmutableList.of(ZERO.toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString());
    }
}
