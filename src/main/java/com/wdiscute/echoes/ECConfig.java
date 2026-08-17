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

    public static final ModConfigSpec.LongValue BASE_TIMELESS_DURATION = BUILDER_SERVER
            .comment("The base timer when entering the Timeless.")
            .translation("echoes.configuration.base_timeless_duration")
            .defineInRange("base_timeless_duration", 1200, 0, Long.MAX_VALUE);

    public static final ModConfigSpec.LongValue TIMELESS_DURATION_ADDED = BUILDER_SERVER
            .comment("How many ticks to add each time a portal is entered inside the Timeless. e.g. lvl3 -> lvl4")
            .translation("echoes.configuration.timeless_duration_added")
            .defineInRange("timeless_duration_added", 1200, 0, Long.MAX_VALUE);

    static final ModConfigSpec SPEC_SERVER = BUILDER_SERVER.build();
}
