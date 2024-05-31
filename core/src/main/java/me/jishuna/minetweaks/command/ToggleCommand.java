package me.jishuna.minetweaks.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import me.jishuna.jishlib.command.argument.ArgumentQueue;
import me.jishuna.jishlib.command.node.LeafNode;
import me.jishuna.minetweaks.Registries;
import me.jishuna.minetweaks.tweak.ToggleableTweak;
import me.jishuna.minetweaks.tweak.Tweak;

public class ToggleCommand extends LeafNode {

    protected ToggleCommand() {
        super("minetweaks.command.toggle");
    }

    @Override
    public void handleCommand(CommandSender sender, ArgumentQueue args) {
        if (!(sender instanceof Player player)) {
            return;
        }

        Tweak tweak = Registries.TWEAK.get(args.poll());
        if (tweak == null || !(tweak instanceof ToggleableTweak toggleable)) {
            return;
        }

        toggleable.toggle(player);
        StatusCommand.sendStatus(player);
    }

    @Override
    protected List<String> handleTabComplete(CommandSender sender, ArgumentQueue args) {
        if (args.size() == 1) {
            List<String> toggleable = Registries.TWEAK.getToggleableTweaks().stream().map(Tweak.class::cast).map(Tweak::getName).toList();
            return StringUtil.copyPartialMatches(args.poll(), toggleable, new ArrayList<>());
        }

        return Collections.emptyList();
    }
}
