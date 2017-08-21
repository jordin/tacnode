package me.jordin.tacnode;

import me.jordin.tacnode.util.NConsumer;

import java.lang.reflect.Method;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Created by Jordin on 8/10/2017.
 * Jordin is still best hacker.
 */
// Sorry
public class SimpleCommandManager extends CommandManager {
    private CommandData registerCommand(Object instance, String command, String subCommand, String[] params, String description, Class<?>... classes) {
        CommandData data = commandData.getOrDefault(command.toLowerCase(), new CommandData(this, command));

        CommandEncapsulator encapsulator = new CommandEncapsulator(data, instance, findAcceptMethod(instance.getClass()), classes);
        data.addCommandData(subCommand, params, description, encapsulator);

        commandData.put(command.toLowerCase(), data);
        commands.add(command);
        return data;
    }

    public CommandData registerCommand(String command, String subCommand, String description, NConsumer.Zero consumer) {
        return registerCommand(consumer, command, subCommand, new String[0], description);
    }

    public <A> CommandData registerCommand(String command, String param, String description,
                                           Class<A> classA, Consumer<A> consumer) {
        return registerCommand(consumer, command, "", new String[] { param }, description, classA);
    }

    public <A> CommandData registerCommand(String command, String[] params, String description,
                                           Class<A> classA, Consumer<A> consumer) {
        return registerCommand(consumer, command, "", params, description, classA);
    }

    public <A> CommandData registerCommand(String command, String subCommand, String param, String description,
                                           Class<A> classA, Consumer<A> consumer) {
        return registerCommand(consumer, command, subCommand, new String[] { param }, description, classA);
    }

    public <A, B> CommandData registerCommand(String command, String subCommand, String[] params, String description,
                                              Class<A> classA, Class<B> classB, BiConsumer<A, B> consumer) {
        return registerCommand(consumer, command, subCommand, params, description, classA, classB);
    }

    public <A, B, C> CommandData registerCommand(String command, String subCommand, String[] params, String description,
                                                 Class<A> classA, Class<B> classB, Class<C> classC, NConsumer.Three<A, B, C> consumer) {
        return registerCommand(consumer, command, subCommand, params, description, classA, classB, classC);
    }

    public <A, B, C, D> CommandData registerCommand(String command, String subCommand, String[] params, String description,
                                                    Class<A> classA, Class<B> classB, Class<C> classC, Class<D> classD,
                                                    NConsumer.Four<A, B, C, D> consumer) {
        return registerCommand(consumer, command, subCommand, params, description, classA, classB, classC, classD);
    }

    private Method findAcceptMethod(Class<?> clazz) {
        for (Method method : clazz.getMethods()) {
            if (method.getName().equalsIgnoreCase("accept")) {
                return method;
            }
        }

        return null;
    }
}
