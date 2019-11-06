package in.jord.tacnode.parsers.primitives;

import in.jord.tacnode.exceptions.InvalidTypeException;
import in.jord.tacnode.parsers.ArgumentParser;
import in.jord.tacnode.util.CommonSuggestions;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Jordin on 8/9/2017.
 * Jordin is still best hacker.
 */
public class ShortParser implements ArgumentParser<Short> {
    @Override
    public Short parse(Iterator<String> arguments) throws InvalidTypeException {
         try {
            return Short.valueOf(arguments.next());
        } catch (NumberFormatException numberFormatException) {
            throw new InvalidTypeException("short", numberFormatException);
        }
    }

    @Override
    public List<String> provideSuggestions() {
        return CommonSuggestions.ZERO;
    }
}
