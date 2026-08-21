package com.wdiscute.echoes;

import com.wdiscute.echoes.timeless.TimelessData;
import com.wdiscute.echoes.timeless.TimelessHearts;
import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.ScreenUtils;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.gui.GuiLayer;

import java.util.ArrayList;
import java.util.List;

public class TimelessGUILayer implements GuiLayer
{
    public static final ScreenUtils.Image SOUL_BAR_BACKGROUND = new ScreenUtils.Image(Echoes.rl("textures/gui/soul_bar_background.png"), 250, 20);
    public static final ScreenUtils.Image SOUL_BAR_PROGRESS = new ScreenUtils.Image(Echoes.rl("textures/gui/soul_bar_progress.png"), 250, 20);
    public static final ScreenUtils.Image SOUL_HEART_EMPTY = new ScreenUtils.Image(Echoes.rl("textures/gui/soul_heart_empty.png"), 9, 9);
    public static final ScreenUtils.Image SOUL_HEART_HALF = new ScreenUtils.Image(Echoes.rl("textures/gui/soul_heart_half.png"), 9, 9);
    public static final ScreenUtils.Image SOUL_HEART_FULL = new ScreenUtils.Image(Echoes.rl("textures/gui/soul_heart_full.png"), 9, 9);

    public long lastFrame = System.currentTimeMillis();
    public float smoothSouls = 0;
    private static final List<TimedItemStack> loot = new ArrayList<>();
    public static final int DEFAULT_LOOT_TIME = 70;

    public static void addLoot(MaybeStack stack, boolean showNotif)
    {
        ItemStack itemStack = stack.toStack();
        if (!itemStack.isEmpty())
        {
            List<TimedItemStack> list = loot.stream().filter(o -> ItemStack.isSameItemSameComponents(o.stack, itemStack)).toList();
            if (list.isEmpty())
                loot.add(new TimedItemStack(itemStack, showNotif ? DEFAULT_LOOT_TIME : -1));
            else
            {
                list.getFirst().count += itemStack.getCount();
                list.getFirst().ticks = showNotif ? DEFAULT_LOOT_TIME : -1;
            }
        }
    }

    public static void tick(Level level)
    {
        loot.forEach(o -> o.ticks--);
    }

    public static void setLoot(List<MaybeStack> list)
    {
        loot.clear();

        for (MaybeStack maybeStack : list)
            addLoot(maybeStack, false);
    }

    private static class TimedItemStack
    {
        ItemStack stack;
        int ticks;
        int count;

        public TimedItemStack(ItemStack stack, int ticks)
        {
            this.stack = stack;
            this.ticks = ticks;
            this.count = stack.getCount();
        }
    }

    @Override
    public void render(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker)
    {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        if (Minecraft.getInstance().options.hideGui) return;

        TimelessData timelessData = TimelessData.get(player);
        TimelessHearts timelessHearts = TimelessHearts.get(player);

        if (!player.level().dimension().equals(Echoes.TIMELESS))
            return;

        int width = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int height = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        Font font = Minecraft.getInstance().font;

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
        float t = Math.min((now - lastFrame) / 1000000f / smoothingTime, 1.0f);
        smoothSouls = smoothSouls + (timelessData.souls() - smoothSouls) * t;
        lastFrame = now;

        double percentageOfBar = timelessData.maxSouls() / smoothSouls;
        int barWidth = (int) (180 / percentageOfBar);
        SOUL_BAR_BACKGROUND.render(guiGraphics, x, y);
        SOUL_BAR_PROGRESS.render(guiGraphics, x + 35, y, 35, 0, barWidth, 20);

        //
        // ,--.                   ,--.
        // |  |  ,---.   ,---.  ,-'  '-.
        // |  | | .-. | | .-. | '-.  .-'
        // |  | ' '-' ' ' '-' '   |  |
        // `--'  `---'   `---'    `--'
        //

        List<TimedItemStack> filteredLoot = player.isCrouching() ? List.copyOf(loot) : (loot.stream().filter(o -> o.ticks > 0).toList());
        for (int i = 0; i < filteredLoot.size(); i++)
        {
            int cornerOffset = 10;
            TimedItemStack timedItemStack = filteredLoot.get(i);
            ItemStack stack = timedItemStack.stack;

            MutableComponent text = MutableComponent.create(stack.getHoverName().getContents());
            for (Component sibling : text.getSiblings())
                text.append(sibling);
            text.append(Component.literal(" x" + timedItemStack.count));

            ScreenUtils.fill(guiGraphics,
                    cornerOffset - 5,
                    height - cornerOffset - 18 - i * 24, 30 + font.width(text.getVisualOrderText()),
                    20, ((int) (255 - 136 * Math.clamp(DEFAULT_LOOT_TIME - timedItemStack.ticks, 0, 20) / 20.0) << 24)
                        | ((int) (255 * (1 - Math.clamp(DEFAULT_LOOT_TIME - timedItemStack.ticks, 0, 20) / 20.0)) * 0x010101));

            ScreenUtils.text(guiGraphics, font, text,
                    cornerOffset + 20, height - cornerOffset - 12 - i * 24, 0xffffffff, true);

            ScreenUtils.item(guiGraphics, stack, cornerOffset, height - cornerOffset - 16 - i * 24);

        }


        //
        //                          ,--.     ,--.                                 ,--.
        //  ,---.   ,---.  ,--.,--. |  |     |  ,---.   ,---.   ,--,--. ,--.--. ,-'  '-.  ,---.
        // (  .-'  | .-. | |  ||  | |  |     |  .-.  | | .-. : ' ,-.  | |  .--' '-.  .-' (  .-'
        // .-'  `) ' '-' ' '  ''  ' |  |     |  | |  | \   --. \ '-'  | |  |      |  |   .-'  `)
        // `----'   `---'   `----'  `--'     `--' `--'  `----'  `--`--' `--'      `--'   `----'
        //

        //render empty hearts overlay
        for (int i = 0; i < timelessHearts.soulHearts(); i++)
            SOUL_HEART_EMPTY.render(guiGraphics, x + 206 - 8 * i, y - 2);

        //render full hearts
        int fullHearts = timelessHearts.soulHP() / 2;
        for (int i = 0; i < fullHearts; i++)
            SOUL_HEART_FULL.render(guiGraphics, x + 206 - 8 * i, y - 2);

        //render last heart
        if (timelessHearts.soulHP() % 2 == 1)
            SOUL_HEART_HALF.render(guiGraphics, x + 206 - 8 * fullHearts, y - 2);

        //
        //   ,--.   ,--.
        // ,-'  '-. `--' ,--,--,--.  ,---.  ,--.--.
        // '-.  .-' ,--. |        | | .-. : |  .--'
        //   |  |   |  | |  |  |  | \   --. |  |
        //   `--'   `--' `--`--`--'  `----' `--'
        //

        //display time remaining if not Long.MAX_VALUE (isHub)
        if (timelessData.timeToExit() != Long.MAX_VALUE)
        {
            int ticksRemaining = Math.toIntExact(timelessData.timeToExit() - player.level().getGameTime());

            if (ticksRemaining < 0) return;

            long seconds = ticksRemaining / 20;
            long minutes = seconds / 60;
            long remainingSeconds = seconds % 60;

            String time = String.format("%02d:%02d", minutes, remainingSeconds);
            ScreenUtils.centeredText(guiGraphics, font, Component.literal(time), width / 2, 10, 0xffffffff, true);
        }

        if (timelessData.currentStage() > 0)
            ScreenUtils.centeredText(guiGraphics, font, Component.literal(timelessData.currentStage() + ""), width / 2, height - 39, 0xff5798c4, true);
    }
}

