package me.jishuna.minetweaks.command;

import org.bukkit.command.CommandSender;
import me.jishuna.jishlib.Plugin;
import me.jishuna.jishlib.command.argument.ArgumentQueue;
import me.jishuna.jishlib.command.node.RootNode;
import me.jishuna.jishlib.message.Messages;
import me.jishuna.jishlib.util.Components;

public class ReloadCommand extends RootNode {

    public ReloadCommand() {
        super("minetweaks.command.reload");
    }

    @Override
    protected void handleCommand(CommandSender sender, ArgumentQueue arguments) {
        Plugin.getInstance().reloadConfig();
        Components.sendMessage(sender, Messages.get("command.reload.success"));
    }
}
