package me.jordin.tacnode.parsers.string;

import me.jordin.tacnode.CommandManager;
import me.jordin.tacnode.parsers.ArgumentParser;
import me.jordin.tacnode.wrappers.string.RawCommand;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Jordin on 8/11/2017.
 * Jordin is still best hacker.
 */
public class RawCommandParser implements ArgumentParser<RawCommand> {
    private CommandManager manager;

    public RawCommandParser(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public RawCommand parse(Iterator<String> arguments) {
        return new RawCommand(arguments);
    }

    @Override
    public List<String> provideSuggestions(Iterator<String> arguments) {
        RawCommand consumedString = new RawCommand(arguments);
        return this.manager.provideSuggestions(consumedString.get());
    }

    @Override
    public boolean isEndless() {
        return true;
    }
}
