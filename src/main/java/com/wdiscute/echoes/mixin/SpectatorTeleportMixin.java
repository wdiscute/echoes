package com.wdiscute.echoes.mixin;

import com.wdiscute.echoes.Echoes;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.game.ServerboundTeleportToEntityPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerboundTeleportToEntityPacket.class)
public class SpectatorTeleportMixin
{
    @Inject(method = "handle(Lnet/minecraft/network/protocol/game/ServerGamePacketListener;)V", at = @At(value = "HEAD"), cancellable = true)
    private void echoes$handle(ServerGamePacketListener listener, CallbackInfo ci)
    {
        if(listener instanceof ServerGamePacketListenerImpl serverGamePacketListener)
        {
            if(serverGamePacketListener.player.level().dimension().equals(Echoes.TIMELESS))
                ci.cancel();
        }
    }
}
