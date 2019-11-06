package in.jord.tacnode.util;

import in.jord.tacnode.CommandEncapsulator;

import java.util.List;

/**
 * Created by Jordin on 8/10/2017.
 * Jordin is still best hacker.
 */
public class CommandCache {
    private List<String> args;
    private CommandEncapsulator encapsulator;

    public CommandCache(List<String> args, CommandEncapsulator encapsulator) {
        this.args = args;
        this.encapsulator = encapsulator;
    }

    public List<String> getArgs() {
        return args;
    }

    public CommandEncapsulator getEncapsulator() {
        return encapsulator;
    }
}
