package me.jishuna.minetweaks.tweak.item;

import java.util.List;
import java.util.Set;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import me.jishuna.jishlib.config.Comment;
import me.jishuna.jishlib.config.Path;
import me.jishuna.jishlib.event.EventBus;
import me.jishuna.jishlib.util.Dye;
import me.jishuna.jishlib.util.Tasks;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class ItemNameDyeingTweak extends Tweak {
    @Path("blacklist")
    @Comment("A list of items that cannot have colored custom names.")
    private final Set<Material> blacklist = Set.of(Material.BEDROCK);

    public ItemNameDyeingTweak() {
        super("item-name-dyeing", Category.ITEM);
        this.description = List.of(ChatColor.GRAY + "Allows dyeing parts or all of an items name by combining it with any color of dye in an anvil.");
    }

    @Override
    protected void registerEvents(EventBus bus) {
        bus.subscribe(PrepareAnvilEvent.class, this::onPrepareAnvil);
    }

    private void onPrepareAnvil(PrepareAnvilEvent event) {
        AnvilInventory inventory = event.getInventory();
        ItemStack first = inventory.getItem(0);
        ItemStack second = inventory.getItem(1);

        if (first == null || second == null || this.blacklist.contains(first.getType())) {
            return;
        }

        Dye dye = Dye.fromItem(second);
        if (dye == null || dye.getDye() != second.getType()) {
            return;
        }

        ItemStack result = first.clone();
        ItemMeta meta = result.getItemMeta();

        String text = ChatColor.stripColor(inventory.getRenameText());
        if (text.isBlank()) {
            return;
        }

        boolean hasDisplayName = meta.hasDisplayName();
        String name = hasDisplayName ? meta.getDisplayName() : "";
        String rawName = ChatColor.stripColor(name);

        String newName;
        int index = text.indexOf(rawName);
        if (index < 0 || text.equals(rawName)) {
            newName = dye.getChatColor() + text;
        } else {
            StringBuilder builder = new StringBuilder();
            if (index > 0) {
                builder.append(dye.getChatColor());
                builder.append(text.substring(0, index));
            }

            builder.append(name);
            if (index + rawName.length() < text.length()) {
                builder.append(dye.getChatColor());
                builder.append(text.substring(index + rawName.length()));
            }
            newName = builder.toString();
        }

        if (name.equals(newName)) {
            return;
        }

        meta.setDisplayName(newName);
        result.setItemMeta(meta);
        event.setResult(result);

        Tasks.run(() -> {
            inventory.setRepairCost(1);
            inventory.setRepairCostAmount(1);
            inventory.getViewers().forEach(en -> ((Player) en).updateInventory());
        });
    }
}
