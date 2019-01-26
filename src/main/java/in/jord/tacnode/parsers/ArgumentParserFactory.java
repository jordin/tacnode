package in.jord.tacnode.parsers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Primitives;
import in.jord.tacnode.exceptions.IncompleteArgumentsException;
import in.jord.tacnode.exceptions.InvalidTypeException;
import in.jord.tacnode.parsers.generic.GenericEnumParser;
import in.jord.tacnode.parsers.primitives.*;
import in.jord.tacnode.parsers.other.ColourParser;
import in.jord.tacnode.parsers.other.UUIDParser;
import in.jord.tacnode.parsers.string.ConsumedStringParser;
import in.jord.tacnode.parsers.string.StringParser;
import in.jord.tacnode.wrappers.string.ConsumedString;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;

/**
 * Created by Jordin on 8/9/2017.
 * Jordin is still best hacker.
 */
public class ArgumentParserFactory implements ArgumentParserProvider {
    private static Map<Class<?>, ArgumentParser<?>> DEFAULT_PARSER_MAP = ImmutableMap.<Class<?>, ArgumentParser<?>>builder()
            // Primitives
            .put(boolean.class, new BooleanParser())
            .put(byte.class, new ByteParser())
            .put(char.class, new CharParser())
            .put(double.class, new DoubleParser())
            .put(float.class, new FloatParser())
            .put(int.class, new IntegerParser())
            .put(long.class, new LongParser())
            .put(short.class, new ShortParser())
            // String
            .put(String.class, new StringParser())
            .put(ConsumedString.class, new ConsumedStringParser())
            // Common Objects
            .put(UUID.class, new UUIDParser())
            .put(Color.class, new ColourParser())
            .build();

    private Map<Class<?>, ArgumentParser<?>> parserMap = new HashMap<>(DEFAULT_PARSER_MAP);

    public <T> T parseArgument(Class<T> clazz, Iterator<String> arguments) throws InvalidTypeException, IncompleteArgumentsException {
        return this.parseArgument(clazz, arguments, this);
    }

    @SuppressWarnings("unchecked")
    public <T> T parseArgument(Class<T> clazz, Iterator<String> arguments, ArgumentParserProvider argumentParserProvider) throws InvalidTypeException, IncompleteArgumentsException {
        ArgumentParser<T> parser = null;
        if (Primitives.isWrapperType(clazz)) {
            clazz = Primitives.unwrap(clazz);
        }
        try {
            if (clazz.isArray() && !clazz.getComponentType().isArray()) {
                parser = (ArgumentParser<T>) argumentParserProvider.getParser(clazz.getComponentType());
                if (parser != null) {
                    ImmutableList.Builder<T> builder = ImmutableList.builder();

                    while (arguments.hasNext()) {
                        builder.add(parser.parse(arguments));
                    }

                    List<T> list = builder.build();

                    return (T) list.toArray((Object[]) Array.newInstance(clazz.getComponentType(), list.size()));
                }
            }
            parser = (ArgumentParser<T>) argumentParserProvider.getParser(clazz);
            return parser.parse(arguments);
        } catch (NoSuchElementException noSuchElement) {
            throw new IncompleteArgumentsException(clazz, parser);
        }
    }

    public <T> void registerArgumentParser(Class<T> clazz, ArgumentParser<T> parser) {
        this.parserMap.put(clazz, parser);
    }

    public <E extends Enum<E>> void registerEnum(Class<E> clazz) {
        this.parserMap.put(clazz, new GenericEnumParser<>(clazz));
    }

    public void addArgumentParserBundle(ArgumentParserBundle bundle) {
        this.parserMap.putAll(bundle.getParsers());
    }

    public boolean hasParser(Class<?> clazz) {
        Class<?> toCheck = clazz;
        if (clazz.isArray()) {
            toCheck = clazz.getComponentType();
        }
        return this.parserMap.containsKey(toCheck);
    }

    @Override
    public ArgumentParser<?> getParser(Class<?> clazz) {
        if (Primitives.isWrapperType(clazz)) {
            clazz = Primitives.unwrap(clazz);
        }
        if (clazz.isEnum() && !this.parserMap.containsKey(clazz)) {
            registerEnum((Class<Enum>) clazz);
        }
        return this.parserMap.get(clazz);
    }
}
