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
public class ByteParser implements ArgumentParser<Byte> {
    @Override
    public Byte parse(Iterator<String> arguments) throws InvalidTypeException {
        try {
            return Byte.valueOf(arguments.next());
        } catch (NumberFormatException numberFormatException) {
            throw new InvalidTypeException("byte", numberFormatException);
        }
    }

    @Override
    public List<String> provideSuggestions() {
        return CommonSuggestions.ZERO;
    }
}
