package com.wdiscute.echoes;

import com.wdiscute.echoes.registry.ECDataAttachments;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.neoforged.neoforge.client.gui.GuiLayer;

public class TimelessGUILayer implements GuiLayer
{
    @Override
    public void render(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker)
    {
        LocalPlayer player = Minecraft.getInstance().player;
        if(player == null) return;

        TimelessData data = player.getData(ECDataAttachments.TIMELESS_DATA.get());

        if(data.timeToExit() == -1) return;

        //player has data
        int width = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int height = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        long seconds = (data.timeToExit() - System.currentTimeMillis()) / 1000;
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;

        String time = String.format("%02d:%02d", minutes, remainingSeconds);
        centeredText(guiGraphics, Minecraft.getInstance().font, Component.literal(time), width / 2, 10, 0xffffffff, true);
    }


    public static void centeredText(GuiGraphicsExtractor guiGraphics, Font font, Component text, int x, int y, int color, boolean shadow)
    {
        FormattedCharSequence formattedcharsequence = text.getVisualOrderText();
        guiGraphics.text(font, formattedcharsequence, x - font.width(formattedcharsequence) / 2, y, color, shadow);
    }
}
