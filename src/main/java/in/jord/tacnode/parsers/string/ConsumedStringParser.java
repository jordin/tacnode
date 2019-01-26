package in.jord.tacnode.parsers.string;

import in.jord.tacnode.parsers.ArgumentParser;
import in.jord.tacnode.wrappers.string.ConsumedString;

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
