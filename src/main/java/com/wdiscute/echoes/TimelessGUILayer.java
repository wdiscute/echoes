package com.wdiscute.echoes;

import com.wdiscute.echoes.timeless.TimelessData;
import com.wdiscute.echoes.timeless.TimelessHearts;
import com.wdiscute.utils.ScreenUtils;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.gui.GuiLayer;

public class TimelessGUILayer implements GuiLayer
{
    public static final ScreenUtils.Image SOUL_BAR_BACKGROUND = new ScreenUtils.Image(Echoes.rl("textures/gui/soul_bar_background.png"), 250, 20);
    public static final ScreenUtils.Image SOUL_BAR_PROGRESS = new ScreenUtils.Image(Echoes.rl("textures/gui/soul_bar_progress.png"), 250, 20);
    public static final ScreenUtils.Image SOUL_HEART_0 = new ScreenUtils.Image(Echoes.rl("textures/gui/soul_heart_0.png"), 9, 9);
    public static final ScreenUtils.Image SOUL_HEART_1 = new ScreenUtils.Image(Echoes.rl("textures/gui/soul_heart_1.png"), 9, 9);
    public static final ScreenUtils.Image SOUL_HEART_2 = new ScreenUtils.Image(Echoes.rl("textures/gui/soul_heart_2.png"), 9, 9);
    public static final ScreenUtils.Image SOUL_HEART_3 = new ScreenUtils.Image(Echoes.rl("textures/gui/soul_heart_3.png"), 9, 9);
    public static final ScreenUtils.Image SOUL_HEART_4 = new ScreenUtils.Image(Echoes.rl("textures/gui/soul_heart_4.png"), 9, 9);

    public long lastFrame = System.nanoTime();
    public float smoothSouls = 0;

    @Override
    public void render(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker)
    {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        TimelessData timelessData = TimelessData.get(player);
        TimelessHearts timelessHearts = TimelessHearts.get(player);

        if (!player.level().dimension().equals(Echoes.TIMELESS))
            return;

        int width = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int height = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        //
        //                         ,--.     ,--.
        // ,---.   ,---.  ,--.,--. |  |     |  |-.   ,--,--. ,--.--.
        //(  .-'  | .-. | |  ||  | |  |     | .-. ' ' ,-.  | |  .--'
        //.-'  `) ' '-' ' '  ''  ' |  |     | `-' | \ '-'  | |  |
        //`----'   `---'   `----'  `--'      `---'   `--`--' `--'
        //

        int x = width / 2 - 125;
        int y = height - 37;
        float smoothingTime = 0.2f;
        long now = System.nanoTime();
        float t = Math.min((now - lastFrame) / 1_000_000_000f / smoothingTime, 1.0f);
        smoothSouls = smoothSouls + (timelessData.souls() - smoothSouls) * t;
        lastFrame = now;

        double percentageOfBar = timelessData.maxSouls() / smoothSouls;
        int barWidth = (int) (180 / percentageOfBar);
        SOUL_BAR_BACKGROUND.render(guiGraphics, x, y);
        SOUL_BAR_PROGRESS.render(guiGraphics, x + 35, y, 35, 0, barWidth, 20);



        //
        //                         ,--.     ,--.                                 ,--.
        // ,---.   ,---.  ,--.,--. |  |     |  ,---.   ,---.   ,--,--. ,--.--. ,-'  '-.  ,---.
        //(  .-'  | .-. | |  ||  | |  |     |  .-.  | | .-. : ' ,-.  | |  .--' '-.  .-' (  .-'
        //.-'  `) ' '-' ' '  ''  ' |  |     |  | |  | \   --. \ '-'  | |  |      |  |   .-'  `)
        //`----'   `---'   `----'  `--'     `--' `--'  `----'  `--`--' `--'      `--'   `----'
        //

        //render empty hearts overlay
        for (int i = 0; i < timelessHearts.soulHearts(); i++)
            SOUL_HEART_0.render(guiGraphics, x + 34 + 8 * i, y - 2);

        //render full hearts
        int fullHearts = timelessHearts.soulHP() / 4;
        for (int i = 0; i < fullHearts; i++)
            SOUL_HEART_4.render(guiGraphics, x + 34 + 8 * i, y - 2);

        //render last heart
        int quarterHearts = timelessHearts.soulHP() % 4;
        switch (quarterHearts)
        {
            case 1:
                SOUL_HEART_1.render(guiGraphics, x + 34 + 8 * fullHearts, y - 2);
                break;
            case 2:
                SOUL_HEART_2.render(guiGraphics, x + 34 + 8 * fullHearts, y - 2);
                break;

            case 3:
                SOUL_HEART_3.render(guiGraphics, x + 34 + 8 * fullHearts, y - 2);
                break;

            default:
        }



        //display time remaining if not Long.MAX_VALUE (hub)
        if (timelessData.timeToExit() != Long.MAX_VALUE)
        {
            int ticksRemaining = Math.toIntExact(timelessData.timeToExit() - player.level().getGameTime());

            if (ticksRemaining < 0) return;

            long seconds = ticksRemaining / 20;
            long minutes = seconds / 60;
            long remainingSeconds = seconds % 60;

            String time = String.format("%02d:%02d", minutes, remainingSeconds);
            ScreenUtils.centeredText(guiGraphics, Minecraft.getInstance().font, Component.literal(time), width / 2, 10, 0xffffffff, true);
        }

        ScreenUtils.centeredText(guiGraphics, Minecraft.getInstance().font, Component.literal("level: " + timelessData.currentStage()), width / 2, 20, 0xffffffff, true);
        ScreenUtils.centeredText(guiGraphics, Minecraft.getInstance().font, Component.literal("souls: " + timelessData.souls()), width / 2, 30, 0xffffffff, true);
    }
}

