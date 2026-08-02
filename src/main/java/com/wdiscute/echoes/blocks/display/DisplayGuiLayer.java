package com.wdiscute.echoes.blocks.display;

import com.wdiscute.echoes.registry.ECBlocks;
import com.wdiscute.libtooltips.Tooltips;
import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.screen.ScreenUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.client.gui.GuiLayer;

public class DisplayGuiLayer implements GuiLayer
{
    @Override
    public void render(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker)
    {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        if (Minecraft.getInstance().hitResult instanceof BlockHitResult hitResult)
        {
            BlockState blockState = player.level().getBlockState(hitResult.getBlockPos());

            if (blockState.is(ECBlocks.DISPLAY) && player.level().getBlockEntity(hitResult.getBlockPos()) instanceof DisplayBlockEntity dbe)
            {
                int width = Minecraft.getInstance().getWindow().getGuiScaledWidth();
                int height = Minecraft.getInstance().getWindow().getGuiScaledHeight();

                long time = System.currentTimeMillis();
                int max = 280;
                int min = 260;
                float speed = 0.3f;

                int x = width / 2 - 200;
                int y = height / 2 - 120;

                int costEntries = dbe.trade.cost().size();

                ItemStack stack = dbe.trade.stack().toStack();
                ScreenUtils.renderItem(guiGraphics, stack,
                        (float) 0,
                        (float) (20 + 25 * (Math.sin(time / 1000.0 * 0.2f) + 1) / 2),
                        (float) (min + (max - min) * (Math.sin(time / 1000.0 * speed) + 1) / 2),
                        -260, -90, 5f);

                guiGraphics.fill(x, y, x + 140, y + 188 + costEntries * 16, 0x66000000);
                guiGraphics.fill(x + 10, y + 170, x + 130, y + 178 + costEntries * 16, 0x66000000);

                Font font = Minecraft.getInstance().font;

                ScreenUtils.centeredText(guiGraphics,
                        font, MutableComponent.create(stack.getHoverName().getContents()).withStyle(ChatFormatting.BOLD),
                        x + 70, y + 6, 0xffffffff, true);

                ScreenUtils.centeredText(guiGraphics,
                        font, Tooltips.resolveTagsToComponent("<ltrgb>LEGENDARY</ltrgb>").withStyle(ChatFormatting.BOLD),
                        x + 70, y + 18, 0xffffffff, true);

                guiGraphics.text(font, Component.literal("+2 Damage to Sculk"),
                        x + 18, y + 128, 0xffffffff);

                guiGraphics.text(font, Component.literal("+1.5 kisses from nano"),
                        x + 18, y + 138, 0xffffffff);

                guiGraphics.text(font, Component.literal("Material Cost").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.DARK_AQUA),
                        x + 29, y + 166, 0xffffffff);

                for (int i = 0; i < dbe.trade.cost().size(); i++)
                {
                    ItemStack costStack = dbe.trade.cost().get(i).toStack();

                    guiGraphics.text(font, MutableComponent.create(costStack.getHoverName().getContents()).append(" x" + costStack.count()),
                            x + 30, y + 179 + i * 15, 0xffffffff);

                    ScreenUtils.item(guiGraphics, costStack, x + 20, y + 182 + i * 16, guiGraphics.pose(), 1f);
                }
            }
        }

        //(System.currentTimeMillis() % 10_000) * 360.0f / 10_000.0f,

    }
}
