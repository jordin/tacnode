package in.jord.tacnode.parsers.primitives;

import com.google.common.collect.ImmutableList;
import in.jord.tacnode.exceptions.InvalidTypeException;
import in.jord.tacnode.parsers.ArgumentParser;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Jordin on 8/9/2017.
 * Jordin is still best hacker.
 */
public class FloatParser implements ArgumentParser<Float> {
    private static final List<String> SUGGESTIONS = ImmutableList.of("0.0F");

    @Override
    public Float parse(Iterator<String> arguments) throws InvalidTypeException {
        try {
            return Float.parseFloat(arguments.next());
        } catch (NumberFormatException numberFormatException) {
            throw new InvalidTypeException("float", numberFormatException);
        }
    }

    @Override
    public List<String> provideSuggestions() {
        return SUGGESTIONS;
    }
}
