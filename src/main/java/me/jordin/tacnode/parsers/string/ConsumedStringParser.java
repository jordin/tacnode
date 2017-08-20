package me.jordin.tacnode.parsers.string;

import me.jordin.tacnode.parsers.ArgumentParser;
import me.jordin.tacnode.wrappers.string.ConsumedString;

import java.util.Iterator;

/**
 * Created by Jordin on 8/11/2017.
 * Jordin is still best hacker.
 */
public class ConsumedStringParser implements ArgumentParser<ConsumedString> {
    @Override
    public ConsumedString parse(Iterator<String> arguments) {
        return new ConsumedString(arguments);
    }

    @Override
    public boolean isEndless() {
        return true;
    }
}
