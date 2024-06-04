package me.jishuna.minetweaks.tweak.misc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import me.jishuna.jishlib.event.EventBus;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class ElytraTakeoffTweak extends Tweak {
    private final Map<UUID, Long> forceGliding = new HashMap<>();

    public ElytraTakeoffTweak() {
        super("elytra-takeoff", Category.MISC);
        this.description = List.of("<gray>Allows players to easily take off with an elytra by right clicking with a firework while on the ground.");
    }

    @Override
    protected void registerEvents(EventBus bus) {
        bus.subscribe(PlayerInteractEvent.class, EventPriority.NORMAL, false, this::onPlayerInteract);
        bus.subscribe(EntityToggleGlideEvent.class, this::onToggleGliding);
    }

    private void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.isGliding() || (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        ItemStack item = event.getItem();
        ItemStack chestplate = event.getPlayer().getEquipment().getChestplate();
        if (item == null || item.getType() != Material.FIREWORK_ROCKET || chestplate == null || chestplate.getType() != Material.ELYTRA) {
            return;
        }

        event.setCancelled(true);

        this.forceGliding.put(player.getUniqueId(), System.currentTimeMillis() + 250);

        player.setGliding(true);
        player.fireworkBoost(item);

        if (event.getHand() == EquipmentSlot.HAND) {
            player.swingMainHand();
        } else {
            player.swingOffHand();
        }

        if (player.getGameMode() != GameMode.CREATIVE) {
            item.setAmount(item.getAmount() - 1);
        }
    }

    private void onToggleGliding(EntityToggleGlideEvent event) {
        if (event.getEntityType() != EntityType.PLAYER || event.isGliding()) {
            return;
        }

        if (this.forceGliding.getOrDefault(event.getEntity().getUniqueId(), 0l) > System.currentTimeMillis()) {
            event.setCancelled(true);
        }
    }
}
