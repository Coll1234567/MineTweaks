package me.jishuna.minetweaks.tweak.mob;

import java.util.List;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import me.jishuna.jishlib.config.annotation.Comment;
import me.jishuna.jishlib.config.annotation.Path;
import me.jishuna.jishlib.event.EventBus;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class WitherHealthTweak extends Tweak {

    @Path("health")
    @Comment("The amount of health withers should have")
    private int health = 600;

    public WitherHealthTweak() {
        super("increased-wither-health", Category.MOB);
        this.description = List.of("<gray><!i>Increases the health of the wither to <green><health><gray>.");
    }

    @Override
    public TagResolver[] getTagResolvers() {
        return new TagResolver[] {
                Formatter.number("health", this.health)
        };
    }

    @Override
    protected void registerEvents(EventBus bus) {
        bus.subscribe(CreatureSpawnEvent.class, this::onSpawn);
    }

    private void onSpawn(CreatureSpawnEvent event) {
        if (event.isCancelled() || (event.getEntityType() != EntityType.WITHER)) {
            return;
        }

        LivingEntity entity = event.getEntity();
        AttributeInstance instance = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        instance.setBaseValue(this.health);
        entity.setHealth(this.health);
    }
}
