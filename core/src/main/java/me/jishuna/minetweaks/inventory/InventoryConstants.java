package me.jishuna.minetweaks.inventory;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import me.jishuna.jishlib.item.ItemBuilder;
import me.jishuna.jishlib.item.ItemSupplier;
import me.jishuna.jishlib.message.Messages;

public class InventoryConstants {
    public static final ItemStack FILLER = ItemBuilder.of(Material.BLACK_STAINED_GLASS_PANE).hideTooltip().build();
    public static final ItemStack ACCENT_FILLER = ItemBuilder.of(Material.ORANGE_STAINED_GLASS_PANE).hideTooltip().build();

//    public static final ItemProvider NEXT_PAGE = TranslatedItemProvider
//            .create(ItemBuilder
//                    .create(Material.PLAYER_HEAD)
//                    .skullTexture("19bf3292e126a105b54eba713aa1b152d541a1d8938829c56364d178ed22bf"), "button.next-page.name", "button.next-page.name");
//
//    public static final ItemProvider PREVIOUS_PAGE = TranslatedItemProvider
//            .create(ItemBuilder
//                    .create(Material.PLAYER_HEAD)
//                    .skullTexture("bd69e06e5dadfd84e5f3d1c21063f2553b2fa945ee1d4d7152fdc5425bc12a9"), "button.previous-page.name", "button.previous-page.name");
//
//    public static final ItemProvider PREVIOUS_MENU = TranslatedItemProvider
//            .create(ItemBuilder.create(Material.ARROW), "button.previous-menu.name", "button.previous-menu.name");
//
    public static final ItemSupplier CLOSE = ItemBuilder
            .of(Material.BARRIER)
            .name(Messages.get("button.close-inventory.name"));

}
