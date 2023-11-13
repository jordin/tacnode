package in.jord.tacnode;

import com.google.common.collect.ImmutableList;
import in.jord.tacnode.annotations.Command;
import in.jord.tacnode.annotations.SubCommand;
import in.jord.tacnode.exceptions.CommandNotFoundException;
import in.jord.tacnode.exceptions.IncompleteArgumentsException;
import in.jord.tacnode.exceptions.InvalidTypeException;
import in.jord.tacnode.info.CommandInfo;
import in.jord.tacnode.parsers.ArgumentParser;
import in.jord.tacnode.parsers.ArgumentParserBundle;
import in.jord.tacnode.parsers.ArgumentParserFactory;
import in.jord.tacnode.parsers.string.RawCommandParser;
import in.jord.tacnode.util.CommandCache;
import in.jord.tacnode.util.CommandSplitter;
import in.jord.tacnode.util.ConsumptionResult;
import in.jord.tacnode.wrappers.string.RawCommand;
import in.jord.tacnode.annotations.CommandDescription;
import in.jord.tacnode.annotations.CommandParameters;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;

/**
 * Created by Jordin on 8/10/2017.
 * Jordin is still best hacker.
 *
 * T = return type
 */
public class CommandManager<T> {
    private ArgumentParserFactory parserFactory = new ArgumentParserFactory();
    protected Map<String, CommandData> commandData = new HashMap<>();
    protected Set<String> commands = new HashSet<>();
    private Consumer<Exception> exceptionHook;
    private Class<T> returnClass;

    private Map<String, CommandCache> COMMAND_CACHE = new HashMap<>();

    public CommandManager() {
        addParser(RawCommand.class, new RawCommandParser(this));
    }

    public CommandManager(Class<T> returnClass) {
        this.returnClass = returnClass;
    }

    public void registerCommands(Object instance) {
        registerCommands("", instance);
    }

    public List<CommandData> registerCommands(String overriddenCommand, Object instance) {
        List<CommandData> commandData = new ArrayList<>(); // FIXME: why is this a List?
        boolean overriding = overriddenCommand != null && !overriddenCommand.isEmpty();

        Class<?> clazz = instance.getClass();

        while (clazz != Object.class) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Command.class) || (overriding && method.isAnnotationPresent(SubCommand.class))) {
                    String command = overriddenCommand;
                    String subCommand = "";
                    String[] parameters = new String[0];
                    String description = "";
                    if (method.isAnnotationPresent(Command.class) && !method.getAnnotation(Command.class).value().isEmpty()) {
                        command = method.getAnnotation(Command.class).value();
                    }
                    if (method.isAnnotationPresent(SubCommand.class)) {
                        subCommand = method.getAnnotation(SubCommand.class).value();
                    }
                    if (method.isAnnotationPresent(CommandParameters.class)) {
                        parameters = method.getAnnotation(CommandParameters.class).value();
                    }
                    if (method.isAnnotationPresent(CommandDescription.class)) {
                        description = method.getAnnotation(CommandDescription.class).value();
                    }

                    CommandData data = registerCommand(instance, method, command, subCommand, parameters, description);
                    // FIXME: why do we care about duplicates if we just registered it anyway?
                    if (!commandData.contains(data)) {
                        commandData.add(data);
                    }
                }
            }

            clazz = clazz.getSuperclass();
        }

        return ImmutableList.copyOf(commandData);
    }

    protected CommandData registerCommand(Object instance, Method method, String command, String subCommand, String[] params, String description) {
        CommandData data = commandData.getOrDefault(command.toLowerCase(), new CommandData(this, command));

        CommandEncapsulator encapsulator = new CommandEncapsulator(data, instance, method);
        data.addCommandData(subCommand, params, description, encapsulator);
        commandData.put(command.toLowerCase(), data);
        commands.add(command);
        return data;
    }

    public CommandCallResult<T> callCommand(String command) throws CommandNotFoundException, IncompleteArgumentsException, InvalidTypeException {
        CommandCache cache = getCommandCache(command);
        if (cache == null) {
            throw new CommandNotFoundException(command);
        }
        Iterator<String> arguments = cache.getArgs().iterator();

        if (cache.getEncapsulator() != null) {


            try {
                if (cache.getEncapsulator().getMethod().getReturnType() == this.returnClass) {
                    return new CommandCallResult<>(true, (T) cache.getEncapsulator().execute(arguments));
                }

                cache.getEncapsulator().execute(arguments);
            } catch (InvalidTypeException | IncompleteArgumentsException e) {
                throw e;
            } catch (Exception e) {
                if (this.exceptionHook != null) {
                    this.exceptionHook.accept(e);
                } else {
                    throw e;
                }
            }
        }

        return new CommandCallResult<>(cache.getEncapsulator() != null, null);
    }

    public ConsumptionResult consume(String command) {
        CommandCache cache = getCommandCache(command);
        if (cache == null) {
            return ConsumptionResult.NOT_ENOUGH;
        }
        Iterator<String> arguments = cache.getArgs().iterator();

        if (cache.getEncapsulator() != null) {
            return cache.getEncapsulator().consume(arguments);
        }
        return ConsumptionResult.NOT_ENOUGH;
    }

    public List<String> provideSuggestions(String command) {
        return ImmutableList.copyOf(new HashSet<>(provideSuggestionsWithDuplicates(command)));
    }

    private List<String> provideSuggestionsWithDuplicates(String command) {
        List<String> arguments = CommandSplitter.split(command);
        if (arguments.isEmpty()) {
            return ImmutableList.copyOf(commands);
        }

        String commandID = arguments.remove(0).toLowerCase();
        CommandData data = commandData.get(commandID);

        if (data == null || arguments.isEmpty()) {
            return ImmutableList.copyOf(commands);
        }

        List<String> subCommands = data.getSubCommands();

        if (!arguments.isEmpty()) {
            String subCommand = arguments.get(0).toLowerCase();

            if (!subCommand.isEmpty() && data.getSubCommandsLowerCase().contains(subCommand) && arguments.size() > 1) {
                arguments.remove(0);
                return ImmutableList.copyOf(data.provideSuggestions(subCommand, arguments));
            }

            if (subCommands.contains("")) {
                boolean showSubCommands = arguments.size() == 1;
                List<String> suggestions = new ArrayList<>(data.provideSuggestions("", arguments));
                if (showSubCommands) {
                    suggestions.addAll(subCommands);
                }
                suggestions.remove("");
                return ImmutableList.copyOf(suggestions);
            }

            return subCommands; // subCommands is already an ImmutableList
        }

        return ImmutableList.of();
    }

    public List<CommandInfo> getInfo(String command) {
        CommandData data = this.commandData.get(command.toLowerCase());
        if (data == null) {
            return ImmutableList.of();
        }

        return data.getCommandInfo().getInfo();
    }

    public CommandData getCommandData(String command) {
        return this.commandData.get(command.toLowerCase());
    }

    private CommandCache getCommandCache(String command) {
        if (!COMMAND_CACHE.containsKey(command)) {
            List<String> args = CommandSplitter.split(command);
            CommandEncapsulator encapsulator = getCommandEncapsulator(args);
            if (encapsulator == null) {
                return null;
            }
            COMMAND_CACHE.put(command, new CommandCache(ImmutableList.copyOf(args), encapsulator));
        }
        return COMMAND_CACHE.get(command);
    }

    private CommandEncapsulator getCommandEncapsulator(List<String> arguments) {
        String commandID = arguments.remove(0).toLowerCase();

        CommandData data = commandData.get(commandID);

        if (data == null) {
            return null;
        }

        if (arguments.isEmpty() || !data.hasSubCommands()) {
            return data.getCommandEncapsulator("", arguments);
        } else {
            String subCommand = arguments.get(0).toLowerCase();
            if (data.getSubCommandsLowerCase().contains(subCommand)) {
                arguments.remove(0);
                return data.getCommandEncapsulator(subCommand, arguments);
            } else {
                return data.getCommandEncapsulator("", arguments);
            }
        }
    }

    public <T> void overrideParser(String command, Class<T> clazz, ArgumentParser<T> parser) {
        String casedCommand = command;
        command = command.toLowerCase();
        if (!this.commandData.containsKey(command)) {
            this.commandData.put(command, new CommandData(this, casedCommand));
        }
        this.commandData.get(command).overrideArgumentParser(clazz, parser);
    }

    public <T> void addParser(Class<T> clazz, ArgumentParser<T> parser) {
        this.parserFactory.registerArgumentParser(clazz, parser);
    }

    public void addArgumentParserBundle(ArgumentParserBundle bundle) {
        this.parserFactory.addArgumentParserBundle(bundle);
    }

    public ArgumentParserFactory getParserFactory() {
        return this.parserFactory;
    }

    public void setExceptionHook(Consumer<Exception> exceptionHook) {
        this.exceptionHook = exceptionHook;
    }
}
