package rs.doyoulikestai.bigjar.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import rs.doyoulikestai.bigjar.item.BigJarItems;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.data.recipes.ShapedRecipeBuilder.shaped;
import static net.minecraft.data.recipes.ShapelessRecipeBuilder.shapeless;

public class BigJarRecipeProvider extends RecipeProvider {

    public BigJarRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        shaped(RecipeCategory.TOOLS, BigJarItems.BIG_JAR_ITEM)
            .pattern("www")
            .pattern("geg")
            .pattern("ggg")
            .define('w', ItemTags.WOODEN_SLABS)
            .define('g', Items.GLASS)
            .define('e', Items.ENDER_PEARL)
            .unlockedBy("has_ender_pearl", has(Items.ENDER_PEARL))
            .save(output);

        shaped(RecipeCategory.MISC, BigJarItems.LITTLE_JAR, 4)
                .pattern("ww ")
                .pattern("gg ")
                .pattern("gg ")
                .define('w', ItemTags.WOODEN_SLABS)
                .define('g', Items.GLASS)
                .unlockedBy("has_glass", has(Items.GLASS))
                .save(output);

        shapeless(RecipeCategory.TOOLS, BigJarItems.LITTLE_JAR_OF_LUBRICANT, 4)
                .requires(Items.HONEYCOMB)
                .requires(Items.SLIME_BALL)
                .requires(BigJarItems.LITTLE_JAR)
                .requires(BigJarItems.LITTLE_JAR)
                .requires(BigJarItems.LITTLE_JAR)
                .requires(BigJarItems.LITTLE_JAR)
                .unlockedBy("has_big_jar", has(BigJarItems.BIG_JAR_ITEM))
                .save(output);
    }
}
