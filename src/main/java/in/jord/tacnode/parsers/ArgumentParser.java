package in.jord.tacnode.parsers;

import com.google.common.collect.ImmutableList;
import in.jord.tacnode.exceptions.InvalidTypeException;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Jordin on 8/8/2017.
 * Jordin is still best hacker.
 */
public interface ArgumentParser<T> {
    T parse(Iterator<String> arguments) throws InvalidTypeException;

    default int minimumArgsRequired() {
        return 1;
    }

    default List<String> provideSuggestions() {
        return ImmutableList.of();
    }

    default boolean isEndless() {
        return false;
    }

    default List<String> provideSuggestions(Iterator<String> arguments) {
        return ImmutableList.of();
    }
}
