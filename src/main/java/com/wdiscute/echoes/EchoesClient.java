package com.wdiscute.echoes;

import com.wdiscute.echoes.network.ECCBPlaySoundPayload;
import com.wdiscute.libtooltips.RGBEffect;
import com.wdiscute.libtooltips.Tooltips;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.apache.commons.lang3.tuple.Triple;

@Mod(value = Echoes.MOD_ID, dist = Dist.CLIENT)
public class EchoesClient
{
    public EchoesClient(ModContainer modContainer)
    {
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        Tooltips.registerProcessor("eccommon", (t, s, e) -> Component.literal(t));

        Tooltips.registerProcessor("ecuncommon",
                (t, s, e) -> ECTooltipGradient.process(t,
                        Triple.of(11, 185, 2),
                        Triple.of(2, 185, 69)
                ));

        Tooltips.registerProcessor("ecrare",
                (t, s, e) -> ECTooltipGradient.process(t,
                        Triple.of(20, 40, 120),
                        Triple.of(100, 180, 255)
                ));

        Tooltips.registerProcessor("ecepic",
                (t, s, e) -> ECTooltipGradient.process(t,
                        Triple.of(61, 0, 255),
                        Triple.of(255, 0, 224)
                ));

        Tooltips.registerProcessor("eclegendary", RGBEffect::process);
    }

    public static void playSoundPayload(ECCBPlaySoundPayload ecdbPlaySoundPayload)
    {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        SoundEvent sound = switch (ecdbPlaySoundPayload.sound())
        {
            case "shriek" -> SoundEvents.SCULK_SHRIEKER_SHRIEK;
            case "beacon_deactivate" -> SoundEvents.BEACON_DEACTIVATE;
            case "beacon_activate" -> SoundEvents.BEACON_ACTIVATE;
            default -> SoundEvents.TOTEM_USE;
        };

        Minecraft.getInstance().getSoundManager().play(
                SimpleSoundInstance.forUI(sound, ecdbPlaySoundPayload.pitch(), ecdbPlaySoundPayload.volume())
        );
    }

    public static class ECTooltipGradient
    {
        public static MutableComponent process(String text, Triple<Integer, Integer, Integer> firstColor, Triple<Integer, Integer, Integer> secondColor)
        {
            boolean bold;

            if (text.startsWith("§l"))
            {
                bold = true;
                text = text.replaceFirst("§l", "");
            }
            else
                bold = false;


            MutableComponent component = Component.empty();

            double time = System.currentTimeMillis() * 0.0006d;

            for (int i = 0; i < text.length(); i++)
            {
                component.append(Component.literal(String.valueOf(text.charAt(i))).withStyle(Style.EMPTY
                        .withColor((getColorForIndex(i, time, firstColor, secondColor)))
                        .withBold(bold)
                ));
            }

            return component;
        }

        public static int getColorForIndex(int seed, double time, Triple<Integer, Integer, Integer> firstColor, Triple<Integer, Integer, Integer> secondColor)
        {
            double wavelength = 50.0;
            double t = time + (seed / wavelength) * (2.0 * Math.PI);
            double blend = (Math.sin(t) + 1.0) / 2.0;

            // Dark blue
            int r1 = firstColor.getLeft();
            int g1 = firstColor.getMiddle();
            int b1 = firstColor.getRight();

            // Bright blue
            int r2 = secondColor.getLeft();
            int g2 = secondColor.getMiddle();
            int b2 = secondColor.getRight();

            //ThapPGT bit shifting bs
            int r = (int) (r1 + (r2 - r1) * blend);
            int g = (int) (g1 + (g2 - g1) * blend);
            int b = (int) (b1 + (b2 - b1) * blend);

            return (255 << 24) | (r << 16) | (g << 8) | b;
        }
    }
}
