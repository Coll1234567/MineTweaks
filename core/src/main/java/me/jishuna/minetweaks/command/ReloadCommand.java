package me.jishuna.minetweaks.command;

import java.util.List;
import org.bukkit.command.CommandSender;
import me.jishuna.jishlib.Plugin;
import me.jishuna.jishlib.command.CommandInfo;
import me.jishuna.jishlib.command.argument.ArgumentQueue;
import me.jishuna.jishlib.command.node.RootNode;
import me.jishuna.jishlib.message.Messages;
import me.jishuna.jishlib.util.Components;

public class ReloadCommand extends RootNode {

    protected ReloadCommand() {
        super(new CommandInfo("reload", "minetweaks.command.reload", List.of()));
    }

    @Override
    protected void handleCommand(CommandSender sender, ArgumentQueue arguments) {
        Plugin.getInstance().reloadConfig();
        Components.sendMessage(sender, Messages.get("command.reload.success"));
    }
}
