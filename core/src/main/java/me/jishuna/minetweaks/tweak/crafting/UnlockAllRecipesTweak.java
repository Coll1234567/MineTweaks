package me.jishuna.minetweaks.tweak.crafting;

import com.google.common.collect.Streams;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import me.jishuna.jishlib.event.EventBus;
import me.jishuna.jishlib.util.Tasks;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class UnlockAllRecipesTweak extends Tweak {

    public UnlockAllRecipesTweak() {
        super("unlock-all-recipes", Category.CRAFTING);
        this.description = List.of("<gray><!i>Unlocks all recipes for players when they join the server.");
    }

    @Override
    protected void registerEvents(EventBus bus) {
        bus.subscribe(PlayerJoinEvent.class, this::onPlayerJoin);
    }

    private void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        CompletableFuture
                .supplyAsync(() -> Streams
                        .stream(Bukkit.recipeIterator())
                        .filter(Keyed.class::isInstance)
                        .map(Keyed.class::cast)
                        .map(Keyed::getKey)
                        .filter(key -> !player.hasDiscoveredRecipe(key))
                        .toList())
                .thenAccept(keys -> Tasks.run(() -> player.discoverRecipes(keys)));
    }
}
