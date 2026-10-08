package com.wdiscute.echoes.registry;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public interface ECKeyMappings {

    String CATEGORY = "key.categories.echoes";

    KeyMapping EMOTE = new KeyMapping(
            "key.echoes.emote",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_TAB),
            CATEGORY
    );
}