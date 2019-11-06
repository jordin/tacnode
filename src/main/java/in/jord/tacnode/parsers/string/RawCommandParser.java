package in.jord.tacnode.parsers.string;

import in.jord.tacnode.wrappers.string.RawCommand;
import in.jord.tacnode.CommandManager;
import in.jord.tacnode.parsers.ArgumentParser;

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
