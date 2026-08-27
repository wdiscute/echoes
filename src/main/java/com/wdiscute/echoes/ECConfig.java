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
            .comment("Adds ticks to the base duration of a level inside the Timeless.")
            .translation("echoes.configuration.global_extra_timeless_duration")
            .defineInRange("global_extra_timeless_duration", 0, 0, Long.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue GLOBAL_EXTRA_TIMELESS_LOOT_DROPS = BUILDER_SERVER
            .comment("Each level will multiply the loot drops by this amount")
            .translation("echoes.configuration.global_extra_timeless_duration")
            .defineInRange("global_extra_timeless_loot_drops", 0.1f, 0, Float.MAX_VALUE);

    public static final ModConfigSpec.IntValue LEVEL_PENALTY_FOR_DYING = BUILDER_SERVER
            .comment("How many levels should be decreased on death")
            .translation("echoes.configuration.level_penalty_for_dying")
            .defineInRange("level_penalty_for_dying", 0, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue BASE_CHEST_ROLLS = BUILDER_SERVER
            .comment("How many base rolls to do per chest")
            .translation("echoes.configuration.base_chest_rolls")
            .defineInRange("base_chest_rolls", 2, 0, Integer.MAX_VALUE);

    static final ModConfigSpec SPEC_SERVER = BUILDER_SERVER.build();
}
