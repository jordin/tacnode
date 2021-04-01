package in.jord.tacnode.parsers.generic;

import in.jord.tacnode.exceptions.InvalidTypeException;
import in.jord.tacnode.parsers.ArgumentParser;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Jordin on 8/11/2017.
 * Jordin is still best hacker.
 */
public class GenericPathParser implements ArgumentParser<Path> {
    private static final Pattern EXTRANEOUS = Pattern.compile("[ \\t_\\-'\"]");
    private final Path directory;
    private final String extension;

    public GenericPathParser(Path directory) {
        this(directory, null);
    }

    public GenericPathParser(Path directory, String extension) {
        this.directory = directory;
        this.extension = extension;
    }

    @Override
    public Path parse(Iterator<String> arguments) throws InvalidTypeException {
        String desiredName = this.normalizeFileName(arguments.next());

        List<Path> candidates = list(this.directory).filter(file -> {
            /* Filter by correct extension */
            return !Files.isDirectory(file) &&
                   (this.extension == null || this.extension.equalsIgnoreCase(FilenameUtils.getExtension(file.toString())));
        }).collect(Collectors.toList());

        Path best = null;

        for (Path candidate : candidates) {
            String name = this.normalizeFileName(FilenameUtils.getBaseName(candidate.toString()));

            if (name.equals(desiredName)) {
                return candidate;
            }

            if (name.contains(desiredName)) {
                best = candidate;
            }
        }

        return best;
    }

    private String normalizeFileName(String input) {
        return EXTRANEOUS.matcher(input).replaceAll("").toLowerCase();
    }

    @Override
    public List<String> provideSuggestions() {
        // TODO: Does this need to be modifiable?
        return list(this.directory)
                .filter(file -> !Files.isDirectory(file))
                .map(file -> FilenameUtils.getBaseName(file.toString()).replace(' ', '_'))
                .collect(Collectors.toList());
    }

    private static Stream<Path> list(Path dir) {
        try {
            return Files.list(dir);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to list directory", e);
        }
    }
}
