package me.jishuna.minetweaks.inventory;

import java.util.Collection;
import java.util.Comparator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.profile.PlayerProfile;
import me.jishuna.jishlib.Constants;
import me.jishuna.jishlib.inventory.ClickContext;
import me.jishuna.jishlib.inventory.PagedCustomInventory;
import me.jishuna.jishlib.item.ItemBuilder;
import me.jishuna.jishlib.message.Messages;
import me.jishuna.jishlib.util.Utils;
import me.jishuna.minetweaks.Registries;
import me.jishuna.minetweaks.tweak.Tweak;

public class TweakListInventory extends PagedCustomInventory<Tweak> {
    private static final PlayerProfile ENABLED = Utils.createProfile("ac01f6796eb63d0e8a759281d037f7b3843090f9a456a74f786d049065c914c7");
    private static final PlayerProfile DISABLED = Utils.createProfile("548d7d1e03e1af145b0125ab841285672b421265da2ab915015f9058438ba2d8");

    public TweakListInventory() {
        super(54, Messages.get("gui.tweaks.name", Formatter.number("tweak_count", Registries.TWEAK.size())), getSortedTweaks(), 45);

        cancelAllClicks();

        populate();
        refreshOptions();
    }

    private void populate() {
        setButton(49, InventoryConstants.CLOSE, context -> context.session().close());

        setItem(InventoryConstants.FILLER, 45, 46, 47, 48, 50, 51, 52, 53);
    }

    @Override
    protected ItemStack asItemStack(Tweak tweak) {
        Component[] description = tweak
                .getDescription()
                .stream()
                .map(s -> Constants.MINI_MESSAGE.deserialize(s, tweak.getTagResolvers()))
                .toArray(Component[]::new);

        return ItemBuilder
                .of(Material.PLAYER_HEAD)
                .name(Messages.get("tweak.name", Placeholder.unparsed("name", tweak.getDisplayName())))
                .lore(description)
                .skullProfile(tweak.isEnabled() ? ENABLED : DISABLED)
                .build();
    }

    @Override
    protected void onItemClicked(ClickContext context, Tweak tweak) {
        // TODO Auto-generated method stub
    }

    private static Collection<Tweak> getSortedTweaks() {
        return Registries.TWEAK.getValues().stream().sorted(Comparator.comparing(Tweak::getDisplayName)).toList();
    }
}
