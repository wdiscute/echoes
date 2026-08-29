package com.wdiscute.echoes.registry;

import com.mojang.blaze3d.platform.InputConstants;
import com.wdiscute.echoes.Echoes;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public interface ECKeyMappings
{
    KeyMapping.Category CATEGORY = new KeyMapping.Category(Echoes.rl("echoes"));

    KeyMapping EMOTE =
            new KeyMapping("key.echoes.emote",
                    KeyConflictContext.IN_GAME,
                    InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_TAB),
                    CATEGORY);

}
