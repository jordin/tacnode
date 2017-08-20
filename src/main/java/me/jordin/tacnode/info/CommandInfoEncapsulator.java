package me.jordin.tacnode.info;

import me.jordin.tacnode.CommandData;

import java.util.*;

/**
 * Created by Jordin on 8/10/2017.
 * Jordin is still best hacker.
 */
public class CommandInfoEncapsulator {
    private static final String REQUIRED_PARAM = "<%s>";
    private static final String OPTIONAL_PARAM = "[%s]";
    private List<CommandInfo> info;

    private CommandInfoEncapsulator(List<CommandInfo> info) {
        this.info = info;
    }

    public List<CommandInfo> getInfo() {
        return info;
    }

    public static CommandInfoEncapsulator from(CommandData commandData) {
        List<CommandInfo> info = new ArrayList<>();

        List<String> subCommands = new ArrayList<>(commandData.getSubCommands());
        Collections.sort(subCommands);

        for (String subCommand : subCommands) {
            String description = commandData.getDescription(subCommand);
            String prefix = commandData.getCommand() + (subCommand.isEmpty() ? "" : " " + subCommand) + " ";

            List<String> simplifiedParams = getUsages(commandData.getParams(subCommand));

            if (simplifiedParams.isEmpty()) {
                info.add(new CommandInfo(prefix, description));
                continue;
            }

            List<String> prefixedParams = new ArrayList<>(simplifiedParams.size());
            simplifiedParams.forEach((params) -> prefixedParams.add(prefix + params));
            Collections.sort(prefixedParams);

            for (String params : prefixedParams) {
                info.add(new CommandInfo(params, description));
            }
        }
        return new CommandInfoEncapsulator(info);
    }

    public static List<String> getUsages(List<String[]> argumentSet) {
        CommandSyntaxTreeNode root = new CommandSyntaxTreeNode("");

        int maxLength = argumentSet.stream().map(strings -> strings.length).max(Integer::compareTo).orElse(0);

        for (String[] arguments : argumentSet) {
            CommandSyntaxTreeNode parent = root;

            for (int i = 0; i < maxLength; i++) {
                if (parent.getData() != null) {
                    String data;
                    if (i < arguments.length) {
                        data = arguments[i];
                    } else {
                        data = null;
                    }

                    parent = parent.addChild(data);
                }
            }
        }

        List<String> usages = new LinkedList<>();
        for (CommandSyntaxTreeNode node : root.getChildren()) {
            List<String> parents = new LinkedList<>();
            buildUsages(usages, parents, root, node);
        }

        return usages;
    }

    private static void buildUsages(List<String> usages, List<String> parents, CommandSyntaxTreeNode parent, CommandSyntaxTreeNode node) {
        int parentAmount = parent.getChildren().size();
        boolean hasNull = false;
        for (CommandSyntaxTreeNode sibling : parent.getChildren()) {
            if (sibling.getData() == null) {
                hasNull = true;
                break;
            }
        }

        boolean isRequired = parentAmount == 1 || !hasNull;
        String data = String.format(isRequired ? REQUIRED_PARAM : OPTIONAL_PARAM, node.getData());

        if (!node.hasChildren()) {
            if (node.getData() != null) {
                usages.add(String.join(" ", parents) + (parents.isEmpty() ? "" : " ") + data);
            }
        } else {
            parents.add(data);

            for (CommandSyntaxTreeNode child : node.getChildren()) {
                buildUsages(usages, parents, node, child);
            }
        }
    }
}
