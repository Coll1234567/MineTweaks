package me.jishuna.minetweaks.tweak.crafting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import me.jishuna.jishlib.config.Comment;
import me.jishuna.jishlib.config.Path;
import me.jishuna.jishlib.util.RecipeUtils;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class StairsRecipeTweak extends Tweak {
    private static final List<ShapedRecipe> ORIGINAL_RECIPES = new ArrayList<>();

    @Path("amount")
    @Comment("The number of stairs to craft at once")
    private int amount = 8;

    public StairsRecipeTweak() {
        super("extra-stairs", Category.CRAFTING);
        this.description = List.of("<gray>Changes the amount of stairs recieved when crafting to %amount%.");
    }

    @Override
    public void reload() {
        super.reload();

        if (ORIGINAL_RECIPES.isEmpty()) {
            cacheOriginalRecipes();
        }

        ORIGINAL_RECIPES.forEach(recipe -> Bukkit.removeRecipe(recipe.getKey()));

        if (isEnabled()) {
            registerAlternateRecipes();
        } else {
            ORIGINAL_RECIPES.forEach(Bukkit::addRecipe);
        }

    }

    private void cacheOriginalRecipes() {
        Iterator<Recipe> iterator = Bukkit.recipeIterator();

        while (iterator.hasNext()) {
            Recipe recipe = iterator.next();
            ItemStack result = recipe.getResult();

            if (result.getType().toString().endsWith("_STAIRS") && result.getAmount() > 1
                    && recipe instanceof ShapedRecipe shaped && shaped.getKey().getNamespace().equals("minecraft")) {
                ORIGINAL_RECIPES.add(shaped);
            }
        }
    }

    private void registerAlternateRecipes() {
        for (ShapedRecipe recipe : ORIGINAL_RECIPES) {
            ItemStack result = recipe.getResult();
            result.setAmount(this.amount);

            ShapedRecipe newRecipe = RecipeUtils.copyRecipe(recipe, result);
            Bukkit.addRecipe(newRecipe);
        }
    }
}
