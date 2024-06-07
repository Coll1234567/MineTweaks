package me.jishuna.minetweaks.command;

import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.jishuna.jishlib.command.CommandInfo;
import me.jishuna.jishlib.command.argument.ArgumentQueue;
import me.jishuna.jishlib.command.node.RootNode;
import me.jishuna.jishlib.inventory.Inventories;
import me.jishuna.minetweaks.inventory.TweakListInventory;

public class ListTweaksCommand extends RootNode {

    protected ListTweaksCommand() {
        super(new CommandInfo("list", "minetweaks.command.list", List.of()));
    }

    @Override
    protected void handleCommand(CommandSender sender, ArgumentQueue arguments) {
        if (!(sender instanceof Player player)) {
            return;
        }

        Inventories.openInventory(player, new TweakListInventory());
    }
}
