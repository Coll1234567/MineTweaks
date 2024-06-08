package me.jishuna.minetweaks.tweak.farming;

import java.util.List;
import java.util.Set;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Container;
import org.bukkit.block.data.Directional;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import me.jishuna.jishlib.config.annotation.Comment;
import me.jishuna.jishlib.config.annotation.Path;
import me.jishuna.jishlib.event.EventBus;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class SmallFlowerBonemealTweak extends Tweak {

    @Path("enable-for-players")
    @Comment("Lets players use bonemeal on small flowers")
    private boolean enablePlayer = true;

    @Path("enable-for-dispensers")
    @Comment("Lets dispensers use bonemeal on small flowers")
    private boolean enableDispenser = true;

    @Path("flowers")
    @Comment("List of blocks that can be duplicated with bonemeal")
    private Set<Material> flowers = Set
            .of(Material.POPPY, Material.DANDELION, Material.BLUE_ORCHID,
                    Material.ALLIUM, Material.AZURE_BLUET, Material.RED_TULIP,
                    Material.WHITE_TULIP, Material.ORANGE_TULIP, Material.PINK_TULIP,
                    Material.OXEYE_DAISY, Material.CORNFLOWER, Material.LILY_OF_THE_VALLEY);

    public SmallFlowerBonemealTweak() {
        super("small-flower-bonemealing", Category.FARMING);
        this.description = List
                .of("<gray><!i>Allows players and dispensers to quickly duplicate small flowers with bonemeal.", "",
                        "<gray><!i>Enabled For Players: <green><enable_player>",
                        "<gray><!i>Enabled For Dispensers: <green><enable_dispenser>");
    }

    @Override
    public TagResolver[] getTagResolvers() {
        return new TagResolver[] {
                Placeholder.unparsed("enable_player", String.valueOf(this.enablePlayer)),
                Placeholder.unparsed("enable_dispenser", String.valueOf(this.enableDispenser))
        };
    }

    @Override
    protected void registerEvents(EventBus bus) {
        bus.subscribe(PlayerInteractEvent.class, this::onPlayerInteract);
        bus.subscribe(BlockDispenseEvent.class, this::onBlockDispense);
    }

    private void onPlayerInteract(PlayerInteractEvent event) {
        if (!this.enablePlayer) {
            return;
        }

        ItemStack item = event.getItem();
        Block block = event.getClickedBlock();
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || item == null || item.getType() != Material.BONE_MEAL || !this.flowers.contains(block.getType())) {
            return;
        }

        block.getDrops().forEach(drop -> block.getWorld().dropItem(block.getLocation().add(0.5, 0, 0.5), drop));

        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            item.setAmount(item.getAmount() - 1);
        }
    }

    private void onBlockDispense(BlockDispenseEvent event) {
        if (!this.enableDispenser || event.getBlock().getType() != Material.DISPENSER) {
            return;
        }

        ItemStack item = event.getItem();
        Directional directional = (Directional) event.getBlock().getBlockData();
        BlockFace face = directional.getFacing();
        Block block = event.getBlock().getRelative(face);

        if (item.getType() != Material.BONE_MEAL || !this.flowers.contains(block.getType())) {
            return;
        }

        block.getDrops().forEach(drop -> block.getWorld().dropItem(block.getLocation().add(0.5, 0, 0.5), drop));
        ((Container) event.getBlock().getState()).getInventory().removeItem(new ItemStack(Material.BONE_MEAL));
    }
}