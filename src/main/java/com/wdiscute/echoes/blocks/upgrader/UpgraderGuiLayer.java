package com.wdiscute.echoes.blocks.upgrader;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.Rarity;
import com.wdiscute.echoes.blocks.display.DisplayBlockEntity;
import com.wdiscute.echoes.registry.ECBlocks;
import com.wdiscute.echoes.registry.ECDataAttachments;
import com.wdiscute.echoes.registry.ECDataComponents;
import com.wdiscute.echoes.upgrades.BlacksmithTrade;
import com.wdiscute.echoes.upgrades.PerkInstance;
import com.wdiscute.libtooltips.Tooltips;
import com.wdiscute.utils.InventoryManagement;
import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.ScreenUtils;
import com.wdiscute.utils.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UpgraderGuiLayer implements LayeredDraw.Layer
{
    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker)
    {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        if (Minecraft.getInstance().options.hideGui) return;

        if (Minecraft.getInstance().hitResult instanceof BlockHitResult hitResult)
        {
            BlockState blockState = player.level().getBlockState(hitResult.getBlockPos());

            if (blockState.is(ECBlocks.UPGRADER) && player.level().getBlockEntity(hitResult.getBlockPos()) instanceof UpgraderBlockEntity dbe)
            {
                {
                    if (dbe.item == null) return;

                    int width = Minecraft.getInstance().getWindow().getGuiScaledWidth();
                    int height = Minecraft.getInstance().getWindow().getGuiScaledHeight();
                    Font font = Minecraft.getInstance().font;

                    long time = System.currentTimeMillis() + dbe.timeOffset;
                    int max = 10;
                    int min = 40;
                    float speed = 0.3f;

                    ItemStack stack = dbe.item.toStack();

                    //get all perks to display on left side
                    List<MutableComponent> perkComps = new ArrayList<>();
                    List<PerkInstance> perks = stack.getOrDefault(ECDataComponents.PERKS, List.of());
                    for (PerkInstance perkInstance : perks)
                    {
                        perkComps.addAll(perkInstance.perk().getShopTooltip(stack, perkInstance.amplifiers()));

                        //add all amplifier values, with format controlled by the perk
                        MutableComponent amplifiers = Component.empty();
                        for (int i = 0; i < perkInstance.amplifiers().size(); i++)
                            amplifiers.append(Component.literal(perkInstance.perk().formatAmplifier(perkInstance.amplifiers(), i) + (i == perkInstance.amplifiers().size() - 1 ? "" : " "))
                                    .withStyle(ChatFormatting.WHITE));

                        perkComps.add(amplifiers);
                    }

                    int yPerkOffset = perkComps.size() * 11;

                    int x = width / 2 - 220;
                    int y = height / 2 - 120;

                    //render background
                    guiGraphics.fill(x, y, x + 140, height / 2 + 18 + yPerkOffset, 0x66000000);

                    //Item Name
                    Component hoverName = stack.getHoverName();
                    ScreenUtils.centeredText(guiGraphics, font, hoverName,
                            x + 70, y + 6, 0xffffffff, true);

                    //rarity
                    Rarity rarity = stack.getOrDefault(ECDataComponents.TRADE_INFO.get(), new Utils.Duo<>(Rarity.COMMON, Echoes.MISSINGNO)).first();
                    ScreenUtils.centeredText(guiGraphics,
                            font, Tooltips.resolveTagsToComponentFromTranslationKey(rarity.toTranslationKey()).withStyle(ChatFormatting.BOLD),
                            x + 70, y + 18, 0xffffffff, true);

                    //render item
                    double seconds = 5;
                    double interval = 600;

                    if (player.getGameProfile().getName().equals("Klmbe"))
                    {
                        seconds = 1;
                        interval = 1;
                    }

                    //spinny code generated by <user> not chatgpt
                    //chatgpt had no say in this code it was all me and my amazing math skills
                    //Don't forget to replace the text <user> with your username! Is there anything else I can help with? 😊
                    long duration = (long) (seconds * 1000);
                    long cycle = (long) (interval * 1000);
                    long phase = time % cycle;
                    double t = phase / (double) duration;
                    t = t * t * t * (t * (6 * t - 15) + 10);
                    float value = phase < duration ? (float) (360.0 * t) : 0f;

                    renderItem(guiGraphics, stack,
                            value + 150,
                            (float) (-30 + 25 * (Math.sin(time / 1000.0 * 0.4f) + 1) / 2),
                            (float) (min + (max - min) * (Math.sin(time / 1000.0 * speed) + 1) / 2),
                            -150, -50, 4f);

                    //render perks
                    for (int i = 0; i < perkComps.size(); i++)
                        ScreenUtils.centeredText(guiGraphics, font, perkComps.get(i),
                                x + 70, y + 128 + i * 11, 0xffffffff, true);

                }
                //
                //                              ,--.       ,--.   ,--.
                //,--,--,   ,---.  ,--.  ,--. ,-'  '-.     `--' ,-'  '-.  ,---.  ,--,--,--.
                //|      \ | .-. :  \  `'  /  '-.  .-'     ,--. '-.  .-' | .-. : |        |
                //|  ||  | \   --.  /  /.  \    |  |       |  |   |  |   \   --. |  |  |  |
                //`--''--'  `----' '--'  '--'   `--'       `--'   `--'    `----' `--`--`--'
                //

                {
                    if (dbe.nextItem == null) return;

                    int width = Minecraft.getInstance().getWindow().getGuiScaledWidth();
                    int height = Minecraft.getInstance().getWindow().getGuiScaledHeight();
                    Font font = Minecraft.getInstance().font;

                    long time = System.currentTimeMillis() + dbe.timeOffset;
                    int max = 10;
                    int min = 40;
                    float speed = 0.3f;

                    ItemStack stack = dbe.nextItem.item().toStack();

                    //get all perks to display on left side
                    List<MutableComponent> perkComps = new ArrayList<>();
                    List<PerkInstance> perks = stack.getOrDefault(ECDataComponents.PERKS, List.of());
                    for (PerkInstance perkInstance : perks)
                    {
                        perkComps.addAll(perkInstance.perk().getShopTooltip(stack, perkInstance.amplifiers()));

                        //add all amplifier values, with format controlled by the perk
                        MutableComponent amplifiers = Component.empty();
                        for (int i = 0; i < perkInstance.amplifiers().size(); i++)
                            amplifiers.append(Component.literal(perkInstance.perk().formatAmplifier(perkInstance.amplifiers(), i) + (i == perkInstance.amplifiers().size() - 1 ? "" : " "))
                                    .withStyle(ChatFormatting.WHITE));

                        perkComps.add(amplifiers);
                    }

                    int yPerkOffset = perkComps.size() * 11;

                    int x = width / 2 + 80;
                    int y = height / 2 - 120;

                    //render background
                    guiGraphics.fill(x, y, x + 140, height / 2 + 18 + yPerkOffset, 0x66000000);

                    //Item Name
                    Component hoverName = stack.getHoverName();
                    ScreenUtils.centeredText(guiGraphics, font, hoverName,
                            x + 70, y + 6, 0xffffffff, true);

                    //rarity
                    ScreenUtils.centeredText(guiGraphics,
                            font, Tooltips.resolveTagsToComponentFromTranslationKey(dbe.nextItem.rarity().toTranslationKey()).withStyle(ChatFormatting.BOLD),
                            x + 70, y + 18, 0xffffffff, true);

                    //render item
                    double seconds = 5;
                    double interval = 600;

                    if (player.getGameProfile().getName().equals("Klmbe"))
                    {
                        seconds = 1;
                        interval = 1;
                    }

                    //spinny code generated by <user> not chatgpt
                    //chatgpt had no say in this code it was all me and my amazing math skills
                    //Don't forget to replace the text <user> with your username! Is there anything else I can help with? 😊
                    long duration = (long) (seconds * 1000);
                    long cycle = (long) (interval * 1000);
                    long phase = time % cycle;
                    double t = phase / (double) duration;
                    t = t * t * t * (t * (6 * t - 15) + 10);
                    float value = phase < duration ? (float) (360.0 * t) : 0f;

                    renderItem(guiGraphics, stack,
                            value + 150,
                            (float) (-30 + 25 * (Math.sin(time / 1000.0 * 0.4f) + 1) / 2),
                            (float) (min + (max - min) * (Math.sin(time / 1000.0 * speed) + 1) / 2),
                            150, -50, 4f);

                    //render perks
                    for (int i = 0; i < perkComps.size(); i++)
                        ScreenUtils.centeredText(guiGraphics, font, perkComps.get(i),
                                x + 70, y + 128 + i * 11, 0xffffffff, true);


                }

                //
                //                         ,--.
                // ,---.  ,---.   ,---.  ,-'  '-.
                //| .--' | .-. | (  .-'  '-.  .-'
                //\ `--. ' '-' ' .-'  `)   |  |
                // `---'  `---'  `----'    `--'
                //
                {
                    if (dbe.nextItem == null) return;

                    int width = Minecraft.getInstance().getWindow().getGuiScaledWidth();
                    int height = Minecraft.getInstance().getWindow().getGuiScaledHeight();
                    Font font = Minecraft.getInstance().font;

                    //render background
                    ScreenUtils.fill(guiGraphics, width / 2 - 60, height / 2 + 40, 120, 10 + dbe.nextItem.cost().size() * 16, 0x66000000);

                    int x = width / 2 - 70;
                    int y = height / 2 + 40;

                    //"Material Cost"
                    ScreenUtils.centeredText(guiGraphics, font, Component.literal("Upgrade Cost").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.DARK_AQUA),
                            width / 2, y - 4, 0xffffffff, true);


                    //render costs
                    for (int i = 0; i < dbe.nextItem.cost().size(); i++)
                    {
                        ItemStack costStack = dbe.nextItem.cost().get(i).toStack();

                        boolean hasEnough = InventoryManagement.hasEnoughItems(List.of(new MaybeStack(costStack)), Minecraft.getInstance().player.getInventory());

                        ScreenUtils.text(guiGraphics, font, MutableComponent.create(costStack.getHoverName().getContents()).append(" x" + costStack.getCount()),
                                x + 30, y + 10 + i * 16, hasEnough ? 0xff20a347 : 0xffb74646);
                        ScreenUtils.item(guiGraphics, costStack, x + 20, y + 14 + i * 16, guiGraphics.pose(), 1f);
                    }
                }
            }
        }
    }

    public static void renderItem(GuiGraphics guiGraphics, ItemStack stack, float rotY, float rotX, float rotZ, int xOffset, int yOffset, float scale)
    {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;

        if (player == null || minecraft.level == null || stack.isEmpty())
            return;

        PoseStack poseStack = guiGraphics.pose();
        BakedModel model = minecraft.getItemRenderer().getModel(stack, minecraft.level, player, 0);

        float centerX = guiGraphics.guiWidth() / 2.0F + xOffset;
        float centerY = guiGraphics.guiHeight() / 2.0F + yOffset;

        poseStack.pushPose();

        poseStack.translate(centerX, centerY, 150.0F);

        float itemScale = 16.0F * scale;
        poseStack.scale(itemScale, -itemScale, itemScale);

        poseStack.mulPose(Axis.XP.rotationDegrees(rotX));
        poseStack.mulPose(Axis.YP.rotationDegrees(rotY));
        poseStack.mulPose(Axis.ZP.rotationDegrees(rotZ));

        boolean flatLight = !model.usesBlockLight();
        if (flatLight)
            Lighting.setupForFlatItems();

        minecraft.getItemRenderer().render(
                stack,
                ItemDisplayContext.GUI,
                false,
                poseStack,
                guiGraphics.bufferSource(),
                LightTexture.FULL_BRIGHT,
                OverlayTexture.NO_OVERLAY,
                model
        );

        guiGraphics.flush();

        if (flatLight)
            Lighting.setupFor3DItems();

        poseStack.popPose();
    }
}
