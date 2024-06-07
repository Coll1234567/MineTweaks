package me.jishuna.minetweaks.tweak.farming;

import java.util.List;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import me.jishuna.jishlib.config.annotation.Comment;
import me.jishuna.jishlib.config.annotation.Path;
import me.jishuna.jishlib.event.EventBus;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class FeatherFallingTramplingTweak extends Tweak {
    private static final Enchantment FEATHER_FALLING = Registry.ENCHANTMENT.get(NamespacedKey.minecraft("feather_falling"));

    @Path("min-level")
    @Comment("The minimum feather falling level needed to prevent trampling.")
    @Comment("Setting this to 0 will stop players from trampling farmland under any circumstance.")
    private int minLevel = 1;

    public FeatherFallingTramplingTweak() {
        super("feather-falling-prevents-trampling", Category.FARMING);
        this.description = List
                .of("<gray>Prevents players from trampling farmland when wearing feather falling boots.", "",
                        "<gray>Minimum Enchantment Level: %min%");
    }

    @Override
    protected void registerEvents(EventBus bus) {
        bus.subscribe(PlayerInteractEvent.class, this::onPlayerTrample);
    }

    private void onPlayerTrample(PlayerInteractEvent event) {
        if (event.getAction() != Action.PHYSICAL || event.getClickedBlock().getType() != Material.FARMLAND) {
            return;
        }

        if (this.minLevel < 1) {
            event.setCancelled(true);
            return;
        }

        ItemStack item = event.getPlayer().getEquipment().getBoots();
        if (item == null || item.getType().isAir()) {
            return;
        }

        int level = item.getEnchantmentLevel(FEATHER_FALLING);
        if (level >= this.minLevel) {
            event.setCancelled(true);
        }
    }
}
