package com.wdiscute.echoes.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;

import java.util.concurrent.CompletableFuture;

public class DGSCRecipeProvider extends RecipeProvider
{
    public DGSCRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries);
    }

    public ResourceKey<Recipe<?>> rk(ResourceLocation rl)
    {
        return ResourceKey.create(Registries.RECIPE, rl);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput)
    {

    }
}
