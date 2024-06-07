package me.jishuna.minetweaks.tweak.mob;

import java.util.List;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Breedable;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import me.jishuna.jishlib.config.annotation.Comment;
import me.jishuna.jishlib.config.annotation.Path;
import me.jishuna.jishlib.event.EventBus;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class PoisonPotatoBabyMobsTweak extends Tweak {

    @Path("poison-target")
    @Comment("Toggle whether the target should be poisoned when fed the potato")
    private boolean poison = true;

    public PoisonPotatoBabyMobsTweak() {
        super("poison-potato-baby-mobs", Category.MOB);
        this.description = List.of("<gray>Allows players to feed poisonous potatoes to baby mobs to stop them from becoming an adult permanently.");
    }

    @Override
    protected void registerEvents(EventBus bus) {
        bus.subscribe(PlayerInteractEntityEvent.class, this::onEntityInteract);
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
