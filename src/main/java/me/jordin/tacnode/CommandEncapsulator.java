package me.jordin.tacnode;

import com.google.common.collect.ImmutableList;
import me.jordin.tacnode.exceptions.IncompleteArgumentsException;
import me.jordin.tacnode.exceptions.InvalidTypeException;
import me.jordin.tacnode.parsers.ArgumentParser;
import me.jordin.tacnode.util.ConsumptionResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Jordin on 8/10/2017.
 * Jordin is still best hacker.
 */
public class CommandEncapsulator {
    private CommandData handler;

    private Object instance;
    private Method method;
    private Class<?>[] paramTypes;

    private int minimumArgsRequired;

    public CommandEncapsulator(CommandData handler, Object instance, Method method) {
        this(handler, instance, method, method.getParameterTypes());
    }

    public CommandEncapsulator(CommandData handler, Object instance, Method method, Class<?>[] paramTypes) {
        this.handler = handler;
        this.instance = instance;
        this.method = method;
        this.paramTypes = paramTypes;

        for (Class<?> paramType : paramTypes) {
            Class<?> clazz = paramType.isArray() && !paramType.getComponentType().isArray() ? paramType.getComponentType() : paramType;
            ArgumentParser<?> parser = handler.getParser(clazz);
            if (parser == null) {
                throw new RuntimeException(handler.getCommand() + " Missing parser for: " + clazz);
            }
            this.minimumArgsRequired += parser.minimumArgsRequired();
        }
        this.method.setAccessible(true);
    }

    public void execute(Iterator<String> arguments) throws IncompleteArgumentsException, InvalidTypeException {
        Object[] parsed = new Object[paramTypes.length];
        int i = 0;
        for (Class<?> paramType : paramTypes) {
            parsed[i++] = handler.parseArgument(paramType, arguments);
        }
        try {
            method.invoke(this.instance, parsed);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public ConsumptionResult consume(Iterator<String> clonedArgs) {
        ConsumptionResult result;
        try {
            for (Class<?> paramType : paramTypes) {
                handler.parseArgument(paramType, clonedArgs);
            }
            result = clonedArgs.hasNext() ? ConsumptionResult.TOO_MANY : ConsumptionResult.PERFECT;
        } catch (IncompleteArgumentsException | InvalidTypeException ignored) {
            result = ConsumptionResult.NOT_ENOUGH;
        }

        return result;
    }

    public int getMinimumArgsRequired() {
        return minimumArgsRequired;
    }

    public List<String> provideSuggestions(List<String> args, boolean lastArgIsSpace) throws InvalidTypeException {
        Iterator<String> arguments = args.iterator();

        int i = 0;
        try {
            for (Class<?> paramType : paramTypes) {
                if (handler.getParser(paramType).isEndless()) {
                    if (lastArgIsSpace) {
                        List<String> tempArgs = new ArrayList<>();
                        arguments.forEachRemaining(tempArgs::add);
                        tempArgs.add("");
                        arguments = tempArgs.iterator();
                    }
                    return handler.getParser(paramType).provideSuggestions(arguments);
                }
                handler.parseArgument(paramType, arguments);
                if (arguments.hasNext() || lastArgIsSpace) {
                    i++;
                }
            }
        } catch (IncompleteArgumentsException expected) {
            return handler.getParser(paramTypes[i]).provideSuggestions();
        }

        if (i < paramTypes.length && lastArgIsSpace) {
            return handler.getParser(paramTypes[i]).provideSuggestions();
        }

        return ImmutableList.of();
    }
}
