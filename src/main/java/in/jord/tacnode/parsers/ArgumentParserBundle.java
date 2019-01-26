package in.jord.tacnode.parsers;

import java.util.Map;

/**
 * Created by Jordin on 8/20/2017.
 * Jordin is still best hacker.
 */
public interface ArgumentParserBundle {
    Map<Class<?>, ArgumentParser<?>> getParsers();
}
