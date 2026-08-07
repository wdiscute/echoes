package com.wdiscute.echoes;

import com.wdiscute.echoes.registry.ECDataAttachments;
import com.wdiscute.echoes.timeless.TimelessData;
import com.wdiscute.utils.ScreenUtils;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.gui.GuiLayer;

public class TimelessGUILayer implements GuiLayer
{
    @Override
    public void render(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker)
    {
        LocalPlayer player = Minecraft.getInstance().player;
        if(player == null) return;

        TimelessData data = player.getData(ECDataAttachments.TIMELESS_DATA.get());

        if(!player.level().dimension().equals(Echoes.TIMELESS))
            return;

        //player has data
        int width = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int height = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        //display time remaining if not Long.MAX_VALUE (hub)
        if(data.timeToExit() != Long.MAX_VALUE)
        {
            int ticksRemaining = Math.toIntExact(data.timeToExit() - player.level().getGameTime());

            if(ticksRemaining < 0) return;

            long seconds = ticksRemaining / 20;
            long minutes = seconds / 60;
            long remainingSeconds = seconds % 60;

            String time = String.format("%02d:%02d", minutes, remainingSeconds);
            ScreenUtils.centeredText(guiGraphics, Minecraft.getInstance().font, Component.literal(time), width / 2, 10, 0xffffffff, true);
        }

        ScreenUtils.centeredText(guiGraphics, Minecraft.getInstance().font, Component.literal("level: " + data.currentStage()), width / 2, 20, 0xffffffff, true);
    }
}
