package com.wdiscute.echoes;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ECConfig
{
    private static final ModConfigSpec.Builder BUILDER_CLIENT = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue ENABLE_PORTAL_POST_PROCESSING = BUILDER_CLIENT
            .comment("Whether portals should apply the post-processing effects.")
            .translation("echoes.configuration.enable_portal_post_processing")
            .define("enable_portal_post_processing", true);

    static final ModConfigSpec SPEC = BUILDER_CLIENT.build();


    private static final ModConfigSpec.Builder BUILDER_SERVER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.LongValue GLOBAL_EXTRA_TIMELESS_DURATION = BUILDER_SERVER
            .comment("Adds ticks to the base duration of a level inside the Timeless .")
            .translation("echoes.configuration.global_extra_timeless_duration")
            .defineInRange("global_extra_timeless_duration", 0, 0, Long.MAX_VALUE);

    static final ModConfigSpec SPEC_SERVER = BUILDER_SERVER.build();
}
