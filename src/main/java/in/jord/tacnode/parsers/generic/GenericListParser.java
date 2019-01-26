package in.jord.tacnode.parsers.generic;

import com.google.common.collect.ImmutableList;
import in.jord.tacnode.exceptions.InvalidTypeException;
import in.jord.tacnode.parsers.ArgumentParser;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by Jordin on 8/11/2017.
 * Jordin is still best hacker.
 */
public class GenericListParser<T> implements ArgumentParser<T> {
    private static final Supplier<List<String>> NO_SUGGESTIONS = ImmutableList::of;
    private Function<String, T> toObjectFunction;
    private Supplier<List<String>> suggestionProvider;
    private List<String> staticSuggestions;

    public GenericListParser(Function<String, T> toObjectFunction) {
        this(toObjectFunction, NO_SUGGESTIONS);
    }

    public GenericListParser(Function<String, T> toObjectFunction, Supplier<List<String>> suggestionProvider) {
        this.toObjectFunction = toObjectFunction;
        this.suggestionProvider = suggestionProvider;
    }
    public GenericListParser(Function<String, T> toObjectFunction, List<String> staticSuggestions) {
        this.toObjectFunction = toObjectFunction;
        this.staticSuggestions = staticSuggestions;
    }

    @Override
    public T parse(Iterator<String> arguments) throws InvalidTypeException {
        return this.toObjectFunction.apply(arguments.next());
    }

    @Override
    public List<String> provideSuggestions() {
        return this.staticSuggestions == null ? this.suggestionProvider.get() : this.staticSuggestions;
    }
}
