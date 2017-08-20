package me.jordin.tacnode.parsers.generic;

import me.jordin.tacnode.exceptions.InvalidTypeException;
import me.jordin.tacnode.parsers.ArgumentParser;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Jordin on 8/11/2017.
 * Jordin is still best hacker.
 */
public class GenericFileParser implements ArgumentParser<File> {
    private static final String EXTRANEOUS = "[ \\t_\\-'\"]";
    private File directory;
    private String extension;

    public GenericFileParser(File directory) {
        this(directory, null);
    }

    public GenericFileParser(File directory, String extension) {
        this.directory = directory;
        this.extension = extension;
    }

    @Override
    public File parse(Iterator<String> arguments) throws InvalidTypeException {
        String name = stripExtraneous(arguments.next());

        File best = null;
        for (File file : this.directory.listFiles()) {
            if (file.isDirectory()) {
                continue;
            }
            String fileName = stripExtraneous(FilenameUtils.getBaseName(file.getAbsolutePath()));
            String extension = FilenameUtils.getExtension(file.getAbsolutePath());
            if (this.extension == null || this.extension.equalsIgnoreCase(extension)) {
                if (fileName.equals(name)) {
                    best = file;
                    break;
                }
                if (fileName.contains(name)) {
                    best = file;
                }
            }
        }

        return best;
    }

    private String stripExtraneous(String input) {
        return input.replaceAll(EXTRANEOUS, "").toLowerCase();
    }

    @Override
    public List<String> provideSuggestions() {
        List<String> fileNames = new ArrayList<>();
        Arrays.asList(directory.listFiles()).stream().filter(file -> !file.isDirectory()).forEach((file) -> fileNames.add(FilenameUtils.getBaseName(file.getAbsolutePath()).replace(" ", "_")));
        return fileNames;
    }
}
