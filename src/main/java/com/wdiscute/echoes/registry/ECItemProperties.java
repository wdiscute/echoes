package com.wdiscute.echoes.registry;

import com.wdiscute.echoes.Echoes;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.entity.player.Player;

public interface ECItemProperties
{
    static void addCustomItemProperties()
    {
        ItemProperties.register(
                ECItems.ECHO_BLADE.get(),
                Echoes.rl("cast"),
                (stack, l, entity, i) ->
                        stack != null && stack.getOrDefault(ECDataComponents.IS_PRISMA_BLADE, false) ? 1 : 0
        );

        ItemProperties.register(
                ECItems.RAMATTRA.get(),
                Echoes.rl("ramattra"),
                (stack, l, entity, i) ->
                {
                    if(entity instanceof Player player && player.isUsingItem() && entity.getUseItem() == stack)
                        return 1;
                    return 0;
                }
        );
    }
}
