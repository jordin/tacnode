package me.jordin.tacnode.info;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Jordin on 8/12/2017.
 * Jordin is still best hacker.
 */
public class CommandSyntaxTreeNode {
    private String data;
    private List<CommandSyntaxTreeNode> children = new LinkedList<>();

    private Map<String, CommandSyntaxTreeNode> dataMap = new LinkedHashMap<>();

    private boolean hasChildren;

    public CommandSyntaxTreeNode(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public CommandSyntaxTreeNode addChild(String data) {
        CommandSyntaxTreeNode foundChild = dataMap.get(data);
        if (foundChild != null)
            return foundChild;

        CommandSyntaxTreeNode node = new CommandSyntaxTreeNode(data);

        if (data != null) {
            hasChildren = true;
        }
        children.add(node);
        dataMap.put(data, node);

        return node;
    }

    public List<CommandSyntaxTreeNode> getChildren() {
        return children;
    }

    public boolean hasChildren() {
        return hasChildren;
    }
}
