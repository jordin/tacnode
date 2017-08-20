package me.jordin.tacnode.parsers.other;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import me.jordin.tacnode.exceptions.InvalidTypeException;
import me.jordin.tacnode.parsers.ArgumentParser;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Jordin on 8/11/2017.
 * Jordin is still best hacker.
 */
public class ColourParser implements ArgumentParser<Color> {
    private static final Map<String, Color> COLOUR_MAP = ImmutableMap.<String, Color>builder()
            .put("white", Color.WHITE)
            .put("light_gray", Color.LIGHT_GRAY)
            .put("gray", Color.GRAY)
            .put("dark_gray", Color.DARK_GRAY)
            .put("black", Color.BLACK)
            .put("red", Color.RED)
            .put("pink", Color.PINK)
            .put("orange", Color.ORANGE)
            .put("yellow", Color.YELLOW)
            .put("green", Color.GREEN)
            .put("magenta", Color.MAGENTA)
            .put("cyan", Color.CYAN)
            .put("blue", Color.BLUE)
            .build();

    private static final List<String> SUGGESTIONS = ImmutableList.copyOf(COLOUR_MAP.keySet());

    private Color defaultColour;

    public ColourParser() {
        this(Color.BLACK);
    }

    public ColourParser(Color defaultColour) {
        this.defaultColour = defaultColour;
    }

    @Override
    public Color parse(Iterator<String> arguments) throws InvalidTypeException {
        String colour = arguments.next().toLowerCase();

        if (COLOUR_MAP.containsKey(colour)) {
            return COLOUR_MAP.get(colour);
        }

        if (colour.length() <= 3 && isDecNumber(colour.replace("%", ""))) { // RGB(A) values 0 -> 255
            int red = parseColourComponent(colour);
            int green = parseColourComponent(arguments.next());
            int blue = parseColourComponent(arguments.next());
            int alpha = arguments.hasNext() ? parseColourComponent(arguments.next()) : 255;

            return new Color(red, green, blue, alpha);
        } else { // Colour like 0xAARRGGBB
            if (colour.startsWith("#")) {
                colour = colour.substring(1);
            } else if (colour.startsWith("0x")) {
                colour = colour.substring(2);
            }

            if (colour.length() == 3 || colour.length() == 4) { // RGB or ARGB
                StringBuilder newColour = new StringBuilder();
                for (int i = 0; i < colour.length(); i++) {
                    newColour.append(colour.charAt(i));
                    newColour.append(colour.charAt(i));
                }
                colour = newColour.toString();
            }

            boolean alpha = colour.length() == 8;
            try {
                int rgba = (int) Long.parseLong(colour, 16);
                return new Color(rgba, alpha);
            } catch (NumberFormatException ignored) { }
        }

        return defaultColour;
    }

    private int parseColourComponent(String arg) throws InvalidTypeException {
        try {
            if (arg.contains("%")) {
                return (int) Math.floor(Double.parseDouble(arg.replace("%", "")) * 2.55); // 2.55 is 1 percent of 255
            }
            Double value = Double.parseDouble(arg);
            if (value < 1) {
                value *= 255;
            }
            return (int) Math.floor(value);
        } catch (NumberFormatException numberFormatException) {
            throw new InvalidTypeException("colour", numberFormatException);
        }
    }

    private boolean isDecNumber(String test) {
        try {
            Double.parseDouble(test);
            return true;
        } catch (NumberFormatException ignored) {
        }
        return false;
    }

    @Override
    public List<String> provideSuggestions() {
        return SUGGESTIONS;
    }
}
