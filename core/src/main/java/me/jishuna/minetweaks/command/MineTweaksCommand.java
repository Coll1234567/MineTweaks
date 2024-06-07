package me.jishuna.minetweaks.command;

import java.util.List;
import me.jishuna.jishlib.command.CommandInfo;
import me.jishuna.jishlib.command.node.CommandNode;
import me.jishuna.jishlib.command.node.RootNode;

public class MineTweaksCommand extends RootNode {

    public MineTweaksCommand() {
        super(new CommandInfo("minetweaks", "minetweaks.command", List.of("mt")));

        CommandNode listCommand = new ListTweaksCommand();
        setDefaultNode(listCommand);

        addChildNode("list", listCommand);
        addChildNode("reload", new ReloadCommand());
        addChildNode("toggle", new ToggleCommand());
        addChildNode("status", new StatusCommand());
    }
}
