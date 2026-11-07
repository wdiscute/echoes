package com.wdiscute.echoes.registry;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.timeless.TimelessData;
import com.wdiscute.echoes.timeless.TimelessInstance;
import com.wdiscute.echoes.timeless.TimelessLevelEntry;
import com.wdiscute.echoes.timeless.TimelessManager;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public interface ECCommands
{
    DynamicCommandExceptionType ERROR_ROD = new DynamicCommandExceptionType(
            o -> Component.literal("No Timeless Instance found close-by")
    );

    static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context)
    {
        dispatcher.register(Commands.literal("timeless")
                .requires(sourceStack -> sourceStack.hasPermission(2))

                //timeless reset_progress
                .then(Commands.literal("reset_progress")
                        .executes(c ->
                                resetProgress(
                                        c.getSource().getPlayerOrException()
                                )
                        )
                )

                //timeless reset_progress
                .then(Commands.literal("swap_inventory")
                        .executes(c ->
                                swap(
                                        c.getSource().getPlayerOrException()
                                )
                        )
                )

                //timeless make_item_timeless
                .then(Commands.literal("make_item_timeless")
                        .executes(c ->
                                makeItemTimeless(
                                        c.getSource().getPlayerOrException()
                                )
                        )
                )

                //timeless playtest
                .then(Commands.literal("playtest")
                        .then(Commands.argument("level_id", ResourceLocationArgument.id())
                                .executes(c ->
                                        playtest(
                                                c.getSource().getPlayerOrException(),
                                                ResourceLocationArgument.getId(c, "level_id")
                                        )
                                )
                        )
                )
        );
    }

    private static int playtest(ServerPlayer player, ResourceLocation id) throws CommandSyntaxException
    {
        TimelessInstance instance = TimelessManager.getOrCreate(player.level().getServer(), UUID.randomUUID());

        instance.timelessLevel = new TimelessLevelEntry(id, 0, 0, 0, 0);

        instance.addPlayer(player, player.blockPosition(), player.level().dimension().location());

        return 1;
    }

    private static int swap(ServerPlayer player) throws CommandSyntaxException
    {
        player.getData(ECDataAttachments.TIMELESS_INVENTORY).swap(player);
        return 1;
    }

    private static int makeItemTimeless(ServerPlayer player) throws CommandSyntaxException
    {
        ItemStack mainHandItem = player.getMainHandItem();
        if (!mainHandItem.isEmpty())
        {
            mainHandItem.set(ECDataComponents.TIMELESS, UUID.randomUUID());
        }

        return 1;
    }

    private static int advanceTime(ServerPlayer player, int ticks) throws CommandSyntaxException
    {
        ServerLevel sl = player.serverLevel();
        TimelessInstance closest = TimelessManager.getClosest(sl.getServer(), player.blockPosition());

        if (closest == null)
            throw ERROR_ROD.create(null);

        closest.setTime(sl, closest.timeToExit - ticks);
        return 1;
    }

    private static int addTime(ServerPlayer player, int ticks) throws CommandSyntaxException
    {
        ServerLevel sl = player.serverLevel();
        TimelessInstance closest = TimelessManager.getClosest(sl.getServer(), player.blockPosition());

        if (closest == null)
            throw ERROR_ROD.create(null);

        closest.addTime(sl, ticks);
        return 1;
    }

    private static int resetProgress(ServerPlayer player) throws CommandSyntaxException
    {
        TimelessData.resetProgress(player);
        return 1;
    }
}
