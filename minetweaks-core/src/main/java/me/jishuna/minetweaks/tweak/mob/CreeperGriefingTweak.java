package me.jishuna.minetweaks.tweak.mob;

import java.util.Iterator;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.event.entity.EntityExplodeEvent;
import me.jishuna.jishlib.config.annotation.Comment;
import me.jishuna.jishlib.config.annotation.ConfigEntry;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.Tweak;

public class CreeperGriefingTweak extends Tweak {

    @ConfigEntry("disable-block-damage")
    @Comment("Disables all block damage from creeper explosions")
    private boolean disableBlockDamage = false;

    @ConfigEntry("drop-percentage")
    @Comment("The percentage of blocks that should drop when blown up by a creeper")
    @Comment("From 0.0 (0%) to 1.0 (100%)")
    @Comment("Ignored if disable-block-damage is true")
    private double dropPercentage = 1f;

    @ConfigEntry("protected-blocks")
    @Comment("A list of blocks that will not be destroyed by creeper explosions")
    @Comment("Ignored if disable-block-damage is true")
    private Set<Material> protectedMaterials = Set.of(Material.CHEST, Material.TRAPPED_CHEST, Material.BARREL);

    public CreeperGriefingTweak() {
        this.name = "nerf-creeper-griefing";
        this.category = Category.MOB;

        registerEventConsumer(EntityExplodeEvent.class, this::onEntityExplode);
    }

    private void onEntityExplode(EntityExplodeEvent event) {
        if (!(event.getEntity() instanceof Creeper)) {
            return;
        }

        if (this.disableBlockDamage) {
            event.blockList().clear();
            return;
        }

        event.setYield((float) this.dropPercentage);

        Iterator<Block> iterator = event.blockList().iterator();
        while (iterator.hasNext()) {
            if (this.protectedMaterials.contains(iterator.next().getType())) {
                iterator.remove();
            }
        }
    }
}