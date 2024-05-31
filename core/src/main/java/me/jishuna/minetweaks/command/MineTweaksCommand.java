package me.jishuna.minetweaks.command;

import me.jishuna.jishlib.command.node.CommandNode;
import me.jishuna.jishlib.command.node.RootNode;

public class MineTweaksCommand extends RootNode {

    public MineTweaksCommand() {
        super("minetweaks.command");

        CommandNode listCommand = new ListTweaksCommand();
        setDefaultNode(listCommand);

        addChildNode("list", listCommand);
        addChildNode("reload", new ReloadCommand());
        addChildNode("toggle", new ToggleCommand());
        addChildNode("status", new StatusCommand());
    }
}
