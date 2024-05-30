package me.jishuna.minetweaks.tweak.mob;

import java.util.List;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import me.jishuna.jishlib.event.EventBus;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class SilenceMobsTweak extends Tweak {

    public SilenceMobsTweak() {
        super("silence-mobs", Category.MOB);
        this.description = List.of(ChatColor.GRAY + "Allows players to silence mobs by right clicking them with a block of wool.");
    }

    @Override
    protected void registerEvents(EventBus bus) {
        bus.subscribe(PlayerInteractEntityEvent.class, this::onEntityInteract);
    }

    private void onEntityInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();

        ItemStack item;
        if (event.getHand() == EquipmentSlot.HAND) {
            item = player.getEquipment().getItemInMainHand();
        } else {
            item = player.getEquipment().getItemInOffHand();
        }

        if (event.getRightClicked().isSilent() || !Tag.WOOL.isTagged(item.getType())) {
            return;
        }

        event.setCancelled(true);
        event.getRightClicked().setSilent(true);
        player.playSound(player.getLocation(), Sound.BLOCK_WOOL_PLACE, 1f, 1f);

        if (player.getGameMode() != GameMode.CREATIVE) {
            item.setAmount(item.getAmount() - 1);
        }
    }
}
