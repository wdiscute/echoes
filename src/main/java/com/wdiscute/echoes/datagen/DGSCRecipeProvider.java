package com.wdiscute.echoes.datagen;

import com.wdiscute.echoes.registry.ECItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DGSCRecipeProvider extends RecipeProvider
{
    public DGSCRecipeProvider(HolderLookup.Provider registries, RecipeOutput output)
    {
        super(registries, output);
    }

    public static class Runner extends RecipeProvider.Runner
    {
        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider)
        {
            super(packOutput, provider);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput)
        {
            return new DGSCRecipeProvider(provider, recipeOutput);
        }

        @Override
        public String getName()
        {
            return "Starcatcher Recipes";
        }
    }

    public ResourceKey<Recipe<?>> rk(Identifier rl)
    {
        return ResourceKey.create(Registries.RECIPE, rl);
    }

    @Override
    protected void buildRecipes()
    {
        //bobber
        shaped(RecipeCategory.MISC, Items.ECHO_SHARD)
                .define('S', Items.AMETHYST_SHARD)
                .define('T', ECItems.SCULK_TISSUE)
                .pattern("TTT")
                .pattern("TST")
                .pattern("TTT")
                .unlockedBy("has_tissue", has(ECItems.SCULK_TISSUE))
                .save(output);

    }
}
