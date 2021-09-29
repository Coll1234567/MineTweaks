package me.jishuna.minetweaks.modules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import me.jishuna.minetweaks.api.module.TweakModule;

public class RecipeModule extends TweakModule {

	public RecipeModule(JavaPlugin plugin) {
		super(plugin, "recipes");

		addSubModule("unlock-on-join");
		addSubModule("dispensers-from-droppers");
		addSubModule("uncompress-quartz");
		addSubModule("red-sand-iron");
		addSubModule("red-sand-dye");
		addSubModule("hide-bundle");
		addSubModule("leather-bundle");
		addSubModule("rotten-flesh-to-leather");
		addSubModule("more-trapdoors");
		addSubModule("more-stairs");

		addEventHandler(PlayerJoinEvent.class, this::onJoin);
	}

	// Recipe changes
	@Override
	public void reload() {
		super.reload();

		if (!isEnabled())
			return;

		if (getBoolean("dispensers-from-droppers", true)) {
			ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(getOwningPlugin(), "dropper_dispenser"),
					new ItemStack(Material.DISPENSER));
			recipe.shape("012", "132", "012");
			recipe.setIngredient('1', Material.STICK);
			recipe.setIngredient('2', Material.STRING);
			recipe.setIngredient('3', Material.DROPPER);
			Bukkit.addRecipe(recipe);
		}

		if (getBoolean("uncompress-quartz", true)) {
			ShapelessRecipe recipe = new ShapelessRecipe(new NamespacedKey(getOwningPlugin(), "uncompress_quartz"),
					new ItemStack(Material.QUARTZ, 4));
			recipe.addIngredient(Material.QUARTZ_BLOCK);
			Bukkit.addRecipe(recipe);
		}

		if (getBoolean("red-sand-iron", true)) {
			ShapelessRecipe recipe = new ShapelessRecipe(new NamespacedKey(getOwningPlugin(), "redden_sand"),
					new ItemStack(Material.RED_SAND));
			recipe.addIngredient(Material.SAND);
			recipe.addIngredient(Material.IRON_NUGGET);
			Bukkit.addRecipe(recipe);
		}

		if (getBoolean("red-sand-dye", false)) {
			ShapelessRecipe recipe = new ShapelessRecipe(new NamespacedKey(getOwningPlugin(), "redden_sand_dye"),
					new ItemStack(Material.RED_SAND));
			recipe.addIngredient(Material.SAND);
			recipe.addIngredient(Material.RED_DYE);
			Bukkit.addRecipe(recipe);
		}

		if (getBoolean("hide-bundle", true)) {
			ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(getOwningPlugin(), "hide_bundle"),
					new ItemStack(Material.BUNDLE));
			recipe.shape("121", "202", "222");
			recipe.setIngredient('1', Material.STRING);
			recipe.setIngredient('2', Material.RABBIT_HIDE);
			Bukkit.addRecipe(recipe);
		}

		if (getBoolean("leather-bundle", false)) {
			ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(getOwningPlugin(), "leather_bundle"),
					new ItemStack(Material.BUNDLE));
			recipe.shape("121", "202", "222");
			recipe.setIngredient('1', Material.STRING);
			recipe.setIngredient('2', Material.LEATHER);
			Bukkit.addRecipe(recipe);
		}

		if (getBoolean("rotten-flesh-to-leather", false)) {
			FurnaceRecipe recipe = new FurnaceRecipe(new NamespacedKey(getOwningPlugin(), "rotten_flesh_leather"),
					new ItemStack(Material.LEATHER), Material.ROTTEN_FLESH, 0.1f,
					getInt("rotten-flesh-cook-time", 200));
			Bukkit.addRecipe(recipe);
		}

		Iterator<Recipe> iterator = Bukkit.recipeIterator();
		boolean stairs = getBoolean("more-stairs", true);
		boolean trapdoors = getBoolean("more-trapdoors", true);

		while (iterator.hasNext()) {
			Recipe recipe = iterator.next();
			ItemStack result = recipe.getResult();

			if (stairs && result.getType().toString().endsWith("_STAIRS") && result.getAmount() > 1
					&& recipe instanceof ShapedRecipe shaped) {
				iterator.remove();

				result.setAmount(getInt("more-stairs-amount", 8));
				Bukkit.addRecipe(copyRecipe(shaped, result));
			}

			if (trapdoors && result.getType().toString().endsWith("_TRAPDOOR")
					&& result.getType() != Material.IRON_TRAPDOOR && recipe instanceof ShapedRecipe shaped) {
				iterator.remove();

				result.setAmount(getInt("more-trapdoors-amount", 12));
				Bukkit.addRecipe(copyRecipe(shaped, result));
			}
		}
	}

	private ShapedRecipe copyRecipe(ShapedRecipe original, ItemStack result) {
		ShapedRecipe newRecipe = new ShapedRecipe(
				new NamespacedKey(getOwningPlugin(), result.getType().toString().toLowerCase() + "_extra"), result);
		newRecipe.shape(original.getShape());
		original.getChoiceMap().forEach(newRecipe::setIngredient);
		newRecipe.setGroup(original.getGroup());

		return newRecipe;
	}

	// Join recipes
	private void onJoin(PlayerJoinEvent event) {
		if (!getBoolean("unlock-on-join", true))
			return;

		Iterator<Recipe> iterator = Bukkit.recipeIterator();
		List<NamespacedKey> recipes = new ArrayList<>();

		while (iterator.hasNext()) {
			Recipe recipe = iterator.next();
			if (recipe instanceof Keyed keyed) {
				recipes.add(keyed.getKey());
			}
		}

		event.getPlayer().discoverRecipes(recipes);
	}
}