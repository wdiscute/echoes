package com.wdiscute.echoes.datagen;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.registry.ECBlocks;
import com.wdiscute.echoes.registry.ECItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

public class ECDGItemModelProvider extends ItemModelProvider
{
    public ECDGItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper)
    {
        super(output, Echoes.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels()
    {
        //
        //         ,--.                                    ,--.            ,--.
        //  ,---.  |  |  ,---.   ,---.  ,--,--,--.  ,---.  |  |  ,--,--. ,-'  '-.  ,---.
        // | .-. | |  | | .-. : | .-. : |        | (  .-'  |  | ' ,-.  | '-.  .-' | .-. :
        // ' '-' ' |  | \   --. \   --. |  |  |  | .-'  `) |  | \ '-'  |   |  |   \   --.
        // .`-  /  `--'  `----'  `----' `--`--`--' `----'  `--'  `--`--'   `--'    `----'
        // `---'

        //non family blocks
        simpleBlockItem(ECBlocks.GLEEMSLATE_GRASS.get());
        simpleBlockItem(ECBlocks.CHISELED_GLEEMSLATE.get());

        simpleBlockItem(ECBlocks.GLEEMSLATE_PILLAR.get());
        simpleBlockItem(ECBlocks.TRIMMED_GLEEMSLATE.get());


        //gleemslate grass
        //made manually

        //gleemslate
        {
            simpleBlockItem(ECBlocks.GLEEMSLATE.get());
            simpleBlockItem(ECBlocks.GLEEMSLATE_STAIRS.get());
            simpleBlockItem(ECBlocks.GLEEMSLATE_SLAB.get());
            wallInventory(
                    "gleemslate_wall",
                    modLoc("block/gleemslate")
            );
        }

        //cut gleemslate
        {
            simpleBlockItem(ECBlocks.CUT_GLEEMSLATE.get());
            simpleBlockItem(ECBlocks.CUT_GLEEMSLATE_STAIRS.get());
            simpleBlockItem(ECBlocks.CUT_GLEEMSLATE_SLAB.get());
            wallInventory(
                    "cut_gleemslate_wall",
                    modLoc("block/cut_gleemslate")
            );
        }

        //gleemslate tiles
        {
            simpleBlockItem(ECBlocks.GLEEMSLATE_TILES.get());
            simpleBlockItem(ECBlocks.GLEEMSLATE_TILES_STAIRS.get());
            simpleBlockItem(ECBlocks.GLEEMSLATE_TILES_SLAB.get());
            wallInventory(
                    "gleemslate_tiles_wall",
                    modLoc("block/gleemslate_tiles")
            );
        }

        //gleemslate bricks
        {
            simpleBlockItem(ECBlocks.GLEEMSLATE_BRICKS.get());
            simpleBlockItem(ECBlocks.GLEEMSLATE_BRICKS_STAIRS.get());
            simpleBlockItem(ECBlocks.GLEEMSLATE_BRICKS_SLAB.get());
            wallInventory(
                    "gleemslate_bricks_wall",
                    modLoc("block/gleemslate_bricks")
            );
        }


        simpleBlockItem(ECBlocks.SCULK_PILLAR.get());
        simpleBlockItem(ECBlocks.TIMELESS_MARKER.get());

        //sculked deepslate
        {
            simpleBlockItem(ECBlocks.SCULKED_DEEPSLATE.get());
            simpleBlockItem(ECBlocks.SCULKED_DEEPSLATE_STAIRS.get());
            simpleBlockItem(ECBlocks.SCULKED_DEEPSLATE_SLAB.get());
            wallInventory(
                    "sculked_deepslate_wall",
                    modLoc("block/sculked_deepslate")
            );
        }

        //sculked deepslate bricks
        {
            simpleBlockItem(ECBlocks.SCULKED_DEEPSLATE_BRICKS.get());
            simpleBlockItem(ECBlocks.SCULKED_DEEPSLATE_BRICKS_STAIRS.get());
            simpleBlockItem(ECBlocks.SCULKED_DEEPSLATE_BRICKS_SLAB.get());
            wallInventory(
                    "sculked_deepslate_bricks_wall",
                    modLoc("block/sculked_deepslate_bricks")
            );
        }


        simpleItem(ECItems.SOUL_HEART_CONTAINER.get());

        //weapons
        //sculk
        simpleItem(ECItems.TIME_REAPER.get());

        //prisma
        simpleItem(ECItems.LUCENT_WILL.get());



        //materials
        //sculk
        simpleItem(ECItems.SCULK_SPAWN.get());
        simpleItem(ECItems.HOLLOWED_SPINE.get());
        simpleItem(ECItems.SCULKED_TEETH.get());
        simpleItem(ECItems.ECHOING_MARROW.get());
        simpleItem(ECItems.ROT_BRAIN.get());

        //prisma
        simpleItem(ECItems.PRISMA_SHARD.get());
        simpleItem(ECItems.LATTICE.get());
        simpleItem(ECItems.LUCENT_SHARD.get());
        simpleItem(ECItems.CRYSTAL_CORE.get());
        simpleItem(ECItems.LUCENT_DIE.get());

        //armor
        simpleItem(ECItems.TIMELOST_HELMET.get());
        simpleItem(ECItems.TIMELOST_CHESTPLATE.get());
        simpleItem(ECItems.TIMELOST_LEGGINGS.get());
        simpleItem(ECItems.TIMELOST_BOOTS.get());
    }

    private ItemModelBuilder simpleItem(Item item)
    {
        String path = BuiltInRegistries.ITEM.getKey(item).getPath();
        return withExistingParent(path, mcLoc("item/generated")).texture("layer0", modLoc("item/" + path));
    }

    private ItemModelBuilder simpleItem(DeferredItem<? extends Item> item)
    {
        return simpleItem(item.asItem());
    }
}
