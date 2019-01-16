package me.jordin.tacnode;

import com.google.common.collect.ImmutableList;
import me.jordin.tacnode.exceptions.IncompleteArgumentsException;
import me.jordin.tacnode.exceptions.InvalidTypeException;
import me.jordin.tacnode.info.CommandInfoEncapsulator;
import me.jordin.tacnode.parsers.ArgumentParser;
import me.jordin.tacnode.parsers.ArgumentParserFactory;
import me.jordin.tacnode.parsers.ArgumentParserProvider;
import me.jordin.tacnode.util.ConsumptionResult;

import java.util.*;

/**
 * Created by Jordin on 8/10/2017.
 * Jordin is still best hacker.
 */
public class CommandData implements ArgumentParserProvider {
    private CommandManager commandManager;
    private ArgumentParserFactory parserFactory;

    private String command;
    private boolean hasSubCommands;
    private Set<String> subCommands = new HashSet<>();
    private Set<String> subCommandsLowerCase = new HashSet<>();

    private Map<String, List<CommandEncapsulator>> commandEncapsulators = new HashMap<>(); // sub command -> encapsulator list
    private Map<String, String> descriptions = new HashMap<>(); // sub command -> description
    private Map<String, List<String[]>> params = new HashMap<>(); // sub command -> param array
    private Map<Class<?>, ArgumentParser<?>> parserMap = new HashMap<>(); // custom parsers for this command

    private CommandInfoEncapsulator commandInfo;

    public CommandData(CommandManager commandManager, String command) {
        this.commandManager = commandManager;
        this.command = command;
        this.parserFactory = this.commandManager.getParserFactory();
    }

    public String getCommand() {
        return this.command;
    }

    public String getDescription(String subCommand) {
        return this.descriptions.get(subCommand.toLowerCase());
    }

    public List<String[]> getParams(String subCommand) {
        return this.params.get(subCommand.toLowerCase());
    }

    public List<String> getSubCommands() {
        return ImmutableList.copyOf(this.subCommands);
    }

    public List<String> getSubCommandsLowerCase() {
        return ImmutableList.copyOf(this.subCommandsLowerCase);
    }

    public void addCommandData(String subCommand, String[] params, String description, CommandEncapsulator data) {
        String properCaseSubCommand = subCommand;
        subCommand = subCommand.toLowerCase();
        this.commandInfo = null;
        if (!subCommand.isEmpty()) {
            this.hasSubCommands = true;
        }
        List<CommandEncapsulator> encapsulators = this.commandEncapsulators.getOrDefault(subCommand, new ArrayList<>());
        encapsulators.add(data);

        List<String[]> parameters = this.params.getOrDefault(subCommand, new ArrayList<>());
        parameters.add(params);

        this.subCommands.add(properCaseSubCommand);
        this.subCommandsLowerCase.add(properCaseSubCommand.toLowerCase());
        this.descriptions.put(subCommand, (description == null || description.isEmpty()) ? this.descriptions.getOrDefault(subCommand, "") : description);
        this.params.put(subCommand, parameters);
        this.commandEncapsulators.put(subCommand, encapsulators);
    }

    public CommandInfoEncapsulator getCommandInfo() {
        if (this.commandInfo == null) {
            this.commandInfo = CommandInfoEncapsulator.from(this);
        }
        return this.commandInfo;
    }

    public boolean hasSubCommands() {
        return this.hasSubCommands;
    }

    public List<String> provideSuggestions(String subCommand, List<String> arguments) {
        List<String> suggestions = new ArrayList<>();
        boolean lastArgIsSpace = arguments.remove("");
        this.commandEncapsulators.get(subCommand.toLowerCase()).forEach(commandEncapsulator -> {
            try {
                suggestions.addAll(commandEncapsulator.provideSuggestions(arguments, lastArgIsSpace));
            } catch (InvalidTypeException ignored) {
            }
        });
        return suggestions;
    }

    public CommandEncapsulator getCommandEncapsulator(String subCommand, List<String> arguments) {
        List<CommandEncapsulator> encapsulators = this.commandEncapsulators.get(subCommand.toLowerCase());
        if (encapsulators == null || encapsulators.isEmpty()) {
            return null;
        }
        if (encapsulators.size() == 1) {
            return encapsulators.get(0);
        }

        List<CommandEncapsulator> validEncapsulators = new ArrayList<>();
        encapsulators.stream()
                .filter(encapsulator -> arguments.size() >= encapsulator.getMinimumArgsRequired())
                .forEach(validEncapsulators::add);

        if (encapsulators.size() == 1) {
            return encapsulators.get(0);
        }

        CommandEncapsulator best = null;
        for (CommandEncapsulator encapsulator : validEncapsulators) {
            ConsumptionResult result = encapsulator.consume(arguments.iterator());

            if (result == ConsumptionResult.PERFECT) {
                return encapsulator;
            }

            if (result == ConsumptionResult.TOO_MANY) {
                best = encapsulator;
            }
        }

        return best;
    }

    public <T> void overrideArgumentParser(Class<T> clazz, ArgumentParser<T> parser) {
        this.parserMap.put(clazz, parser);
    }

    public <T> T parseArgument(Class<T> clazz, Iterator<String> iterator) throws InvalidTypeException, IncompleteArgumentsException {
        return this.parserFactory.parseArgument(clazz, iterator, this);
    }

    @Override
    public ArgumentParser<?> getParser(Class<?> clazz) {
        return this.parserMap.getOrDefault(clazz, this.parserFactory.getParser(clazz));
    }
}
