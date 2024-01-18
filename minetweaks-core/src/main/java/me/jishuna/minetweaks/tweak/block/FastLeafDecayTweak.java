package me.jishuna.minetweaks.tweak.block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.LeavesDecayEvent;
import me.jishuna.jishlib.JishLib;
import me.jishuna.jishlib.config.annotation.Comment;
import me.jishuna.jishlib.config.annotation.ConfigEntry;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.Tweak;
import me.jishuna.minetweaks.util.ConnectedBlockProcessor;

@RegisterTweak
public class FastLeafDecayTweak extends Tweak {
    private final Set<Block> processing = new HashSet<>();

    @ConfigEntry("max-blocks")
    @Comment("The maximum number of blocks to process for fast decay.")
    private int maxBlocks = 256;

    public FastLeafDecayTweak() {
        super("fast-leaf-decay", Category.BLOCK);
        this.description = List.of(ChatColor.GRAY + "Makes leaves decay much faster when the logs supporting them are broken.");
    }

    @EventHandler(ignoreCancelled = true)
    private void onDecay(LeavesDecayEvent event) {
        Block block = event.getBlock();
        if (this.processing.contains(block)) {
            return;
        }

        ConnectedBlockProcessor processor = new ConnectedBlockProcessor(event.getBlock(), this.maxBlocks, this::check);
        Collection<Block> blocks = processor.process();
        this.processing.addAll(blocks);

        if (!blocks.isEmpty()) {
            new BlockDecayRunnable(this.processing, new ArrayList<>(blocks)).runTaskTimer(JishLib.getPlugin(), 1, 1);
        }
    }

    private boolean check(Block block) {
        if (block.getBlockData() instanceof Leaves leaves) {
            return !leaves.isPersistent() && leaves.getDistance() >= 7;
        }

        return false;
    }
}
