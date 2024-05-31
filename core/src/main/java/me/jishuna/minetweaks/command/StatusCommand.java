package me.jishuna.minetweaks.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.jishuna.jishlib.command.argument.ArgumentQueue;
import me.jishuna.jishlib.command.node.LeafNode;
import me.jishuna.jishlib.message.Messages;
import me.jishuna.jishlib.util.Components;
import me.jishuna.minetweaks.Registries;
import me.jishuna.minetweaks.tweak.ToggleableTweak;
import me.jishuna.minetweaks.tweak.Tweak;

public class StatusCommand extends LeafNode {

    protected StatusCommand() {
        super("minetweaks.command.status");
    }

    @Override
    public void handleCommand(CommandSender sender, ArgumentQueue args) {
        if (!(sender instanceof Player player)) {
            return;
        }

        sendStatus(player);
    }

    public static void sendStatus(Player player) {
        Components.sendMessage(player, Messages.get("tweak.status-prefix"));
        for (ToggleableTweak tweak : Registries.TWEAK.getToggleableTweaks()) {
            Component state = tweak.isEnabled(player) ? Messages.get("tweak.enabled") : Messages.get("tweak.disabled");
            Component component = Messages
                    .get("tweak.status",
                            Placeholder.unparsed("name", ((Tweak) tweak).getDisplayName()),
                            Placeholder.component("status", state))
                    .clickEvent(ClickEvent.runCommand("/minetweaks toggle " + ((Tweak) tweak).getName()))
                    .hoverEvent(HoverEvent.showText(Messages.get("tweak.status-hover")));

            Components.sendMessage(player, component);
        }
        Components.sendMessage(player, Messages.get("tweak.status-suffix"));
    }
}
