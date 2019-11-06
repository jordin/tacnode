package in.jord.tacnode.parsers.generic;

import com.google.common.collect.ImmutableList;
import in.jord.tacnode.exceptions.InvalidTypeException;
import in.jord.tacnode.parsers.ArgumentParser;
import org.apache.commons.lang3.text.WordUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Jordin on 8/9/2017.
 * Jordin is still best hacker.
 */
public class GenericEnumParser<E extends Enum<E>> implements ArgumentParser<E> {
    private String expected;
    private List<String> suggestions;
    private Map<String, E> options;

    public GenericEnumParser(Class<? extends E> anEnum) {
        this.expected = getSimpleName(anEnum);

        this.options = new HashMap<>();
        ImmutableList.Builder<String> suggestionBuilder = ImmutableList.builder();

        for (E element : anEnum.getEnumConstants()) {
            String elementName = applyCapitalization(element);
            this.options.put(elementName.toLowerCase(), element);
            suggestionBuilder.add(elementName);
        }
        this.suggestions = suggestionBuilder.build();
    }

    public static <E extends Enum<E>> String applyCapitalization(E element) {
        String elementName = element.toString();
        if (elementName.startsWith("_")) {
            elementName = elementName.substring(1);
        } else if (elementName.equals(elementName.toUpperCase())) {
            elementName = WordUtils.capitalizeFully(elementName, '_').replace("_", "");
        }
        return elementName;
    }

    @Override
    public E parse(Iterator<String> arguments) throws InvalidTypeException {
        E parsed = options.get(arguments.next().toLowerCase());
        if (parsed == null) {
            throw new InvalidTypeException(expected);
        }

        return parsed;
    }

    @Override
    public List<String> provideSuggestions() {
        return this.suggestions;
    }

    private static String getSimpleName(Class<?> clazz) {
        String name = clazz.getName();

        name = name.substring(name.lastIndexOf('.') + 1);
        name = name.substring(name.lastIndexOf("$") + 1);

        return name;
    }
}
