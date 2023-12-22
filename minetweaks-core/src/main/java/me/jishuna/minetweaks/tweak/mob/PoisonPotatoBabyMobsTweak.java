package me.jishuna.minetweaks.tweak.mob;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Breedable;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import me.jishuna.jishlib.config.annotation.Comment;
import me.jishuna.jishlib.config.annotation.ConfigEntry;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.Tweak;

public class PoisonPotatoBabyMobsTweak extends Tweak {

    @ConfigEntry("poison-target")
    @Comment("Toggle whether the target should be poisoned when fed the potato")
    private boolean poison = true;

    public PoisonPotatoBabyMobsTweak() {
        this.name = "poison-potato-baby-mobs";
        this.category = Category.MOB;

        registerEventConsumer(PlayerInteractEntityEvent.class, this::onEntityInteract);
    }

    private void onEntityInteract(PlayerInteractEntityEvent event) {
        ItemStack item = event.getPlayer().getEquipment().getItem(event.getHand());

        if (event.getRightClicked() instanceof Breedable breedable && !breedable.isAdult() && !breedable.getAgeLock() &&
                item.getType() == Material.POISONOUS_POTATO) {
            event.setCancelled(true);
            breedable.setAgeLock(true);
            if (this.poison) {
                breedable.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 0));
            }

            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1f, 1f);
            if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                item.setAmount(item.getAmount() - 1);
            }
        }
    }
}