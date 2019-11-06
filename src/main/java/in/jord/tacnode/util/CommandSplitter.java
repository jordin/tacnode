package in.jord.tacnode.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jordin on 8/10/2017.
 * Jordin is still best hacker.
 */
public class CommandSplitter {
    private static final Pattern pattern = Pattern.compile("([^\"]\\S*|\".+?\")\\s*");

    public static List<String> split(String command) {
        List<String> split = new ArrayList<>();
        Matcher m = pattern.matcher(command);

        while (m.find())
            split.add(m.group(1).replace("\"", ""));

        if (command.endsWith(" ")) {
            split.add("");
        }

        return split;

        // return Arrays.asList(command.split(" ", -1));
        /*List<String> split = new ArrayList<>();
        Matcher m = pattern.matcher(command);

        while (m.find())
            split.add(m.group(1).replace("\"", ""));

        return split;*/
    }

    /*public static List<String> split(String command) {
        List<String> split = new ArrayList<>();
        split.addAll(Arrays.asList(command.split(" ")));
        return split;
    }

    public static List<String> splitRespectingQuotes(String command) {
        List<String> split = new ArrayList<>();
        Matcher m = pattern.matcher(command);

        while (m.find())
            split.add(m.group(1).replace("\"", ""));

        return split;
    }*/

}
