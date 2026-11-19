package rs.doyoulikestai.bigjar.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import rs.doyoulikestai.bigjar.item.BigJarItems;

import java.util.concurrent.CompletableFuture;

public class BigJarRecipeProvider extends RecipeProvider {

    protected BigJarRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
        super(provider, output);
    }

    @Override
    protected void buildRecipes() {
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

    public static class Runner extends RecipeProvider.Runner {

        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, lookupProvider);
        }

        @Override
        protected @NotNull RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
            return new BigJarRecipeProvider(provider, output);
        }

        @Override
        public @NotNull String getName() {
            return "Big Jar Recipes";
        }
    }
}
