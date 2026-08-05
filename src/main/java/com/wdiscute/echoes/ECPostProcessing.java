package com.wdiscute.echoes;

import com.wdiscute.echoes.blocks.portal.PortalBlock;
import com.wdiscute.echoes.blocks.portal.PortalBlockEntity;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.gui.GuiLayer;

public class ECPostProcessing implements GuiLayer
{
    public static int MAX_FISHEYE = 100;
    public static int MAX_FISHEYE_DISTANCE = 7;
    public static int fishEye = 0;

    public static int MAX_CA = 10;
    public static int MAX_CA_DISTANCE = 10;
    public static int ca = 0;

    public static int MAX_CG = 64;
    public static int MAX_CG_DISTANCE = 10;
    public static int cg = 0;

    Vec3 cachedPos = Vec3.ZERO;

    @Override
    public void render(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker)
    {
        LocalPlayer player = Minecraft.getInstance().player;
        Level level = Minecraft.getInstance().level;
        if(player == null || level == null) return;

        if(cachedPos == player.position()) return;
        cachedPos = player.position();

        //pp disable config
        if(!ECConfig.ENABLE_PORTAL_POST_PROCESSING.get())
        {
            reset();
            return;
        }

        if(PortalBlockEntity.portals.isEmpty())
        {
            reset();
            return;
        }

        BlockPos closest = PortalBlockEntity.getClosestOpenPortal(player.blockPosition());

        //skip if there's no portal
        if(closest.equals(BlockPos.ZERO))
        {
            reset();
            return;
        }

        //if portal is no longer open, remove it from pool
        if(level.getBlockState(closest).getValueOrElse(PortalBlock.STATE, PortalBlock.State.CLOSED).equals(PortalBlock.State.CLOSED))
        {
            PortalBlockEntity.portals.remove(closest);
            return;
        }

        double distance = closest.getCenter().distanceTo(player.position());

        //guiGraphics.text(Minecraft.getInstance().font, "fisheye: " + fishEye, 100, 100, 0xffffffff, true);
        //guiGraphics.text(Minecraft.getInstance().font, "ca: " + ca, 100, 110, 0xffffffff, true);
        //guiGraphics.text(Minecraft.getInstance().font, "cg: " + ca, 100, 120, 0xffffffff, true);

        fishEye = (int) (MAX_FISHEYE * Math.max(0f, 1f - distance / MAX_FISHEYE_DISTANCE));
        ca = (int) (MAX_CA * Math.max(0f, 1f - distance / MAX_CA_DISTANCE));
        cg = (int) (MAX_CG * Math.max(0f, 1f - distance / MAX_CG_DISTANCE));
    }

    public static void reset()
    {
        fishEye = 0;
        ca = 0;
        cg = 0;
    }
}
