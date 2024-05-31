package me.jishuna.minetweaks.tweak.item;

import java.util.List;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import me.jishuna.jishlib.event.EventBus;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class HotbarTotemTweak extends Tweak {

    public HotbarTotemTweak() {
        super("hotbar-totems", Category.ITEM);
        this.description = List.of(ChatColor.GRAY + "Allows totems of undying to activate from any hotbar slot.");
    }

    @Override
    protected void registerEvents(EventBus bus) {
        bus.subscribe(EntityDamageEvent.class, this::onDamage);
    }

    private void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player) || player.getHealth() - event.getFinalDamage() > 0) {
            return;
        }

        PlayerInventory inventory = player.getInventory();
        if (inventory.getItemInMainHand().getType() == Material.TOTEM_OF_UNDYING
                || inventory.getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING) {
            return;
        }

        for (int i = 0; i < 9; i++) {
            ItemStack item = inventory.getItem(i);
            if (item == null || item.getType() != Material.TOTEM_OF_UNDYING) {
                continue;
            }

            if (useTotem(player, item)) {
                event.setDamage(0);
            }
            break;
        }
    }

    private boolean useTotem(Player player, ItemStack item) {
        EntityResurrectEvent event = new EntityResurrectEvent(player, EquipmentSlot.HAND);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return false;
        }

        player.playEffect(EntityEffect.TOTEM_RESURRECT);

        player.setHealth(1);
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 45, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 40, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20 * 5, 1));

        item.setAmount(item.getAmount() - 1);
        return true;
    }
}
