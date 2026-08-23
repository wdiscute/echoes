package com.wdiscute.echoes.registry;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.wdiscute.echoes.timeless.TimelessData;
import com.wdiscute.echoes.timeless.TimelessInstance;
import com.wdiscute.echoes.timeless.TimelessManager;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class ECCommands
{
    private static final DynamicCommandExceptionType ERROR_ROD = new DynamicCommandExceptionType(
            o -> Component.literal("No Timeless Instance found close-by")
    );

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context)
    {
        dispatcher.register(Commands.literal("timeless")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))

                //timeless reset_progress
                .then(Commands.literal("reset_progress")
                        .executes(c ->
                                resetProgress(
                                        c.getSource().getPlayerOrException()
                                )
                        )
                )

                //timeless advance_time
                .then(Commands.literal("advance_time")
                        .then(Commands.argument("ticks", IntegerArgumentType.integer())
                                .executes(c ->
                                        advanceTime(
                                                c.getSource().getPlayerOrException(),
                                                IntegerArgumentType.getInteger(c, "ticks")
                                        )
                                )
                        )
                )
        );
    }

    private static int advanceTime(ServerPlayer player, int ticks) throws CommandSyntaxException
    {
        ServerLevel sl = player.level();
        TimelessInstance closest = TimelessManager.getClosest(sl.getServer(), player.blockPosition());

        if(closest == null)
            throw ERROR_ROD.create(null);

        closest.setTime(sl, closest.timeToExit - ticks);
        return 1;
    }

    private static int resetProgress(ServerPlayer player) throws CommandSyntaxException
    {
        TimelessData.resetProgress(player);
        return 1;
    }
}
