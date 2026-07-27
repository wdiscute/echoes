package com.wdiscute.echoes;

import com.wdiscute.echoes.network.ECDBPlaySoundPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public class EchoesClient
{
    public static void playSoundPayload(ECDBPlaySoundPayload ecdbPlaySoundPayload)
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
}
