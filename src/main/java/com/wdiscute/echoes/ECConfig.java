package com.wdiscute.echoes;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ECConfig
{
    private static final ModConfigSpec.Builder BUILDER_CLIENT = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue ENABLE_PORTAL_POST_PROCESSING = BUILDER_CLIENT
            .comment("Whether portals should apply the post-processing effects.")
            .translation("starcatcher.configuration.enable_portal_post_processing")
            .define("enable_portal_post_processing", true);

    static final ModConfigSpec SPEC = BUILDER_CLIENT.build();


    //private static final ModConfigSpec.Builder BUILDER_SERVER = new ModConfigSpec.Builder();

    //public static final ModConfigSpec.IntValue MAX_TACKLE_BOX_FISH_STORAGE = BUILDER_SERVER
    //        .comment("Sets the maximum number of fishes the tackle box can store in it's 'infinite slot'")
    //        .translation("starcatcher.configuration.max_tackle_box_fish_storage")
    //        .defineInRange("max_tackle_box_fish_storage", 900, 0, 999);

    //static final ModConfigSpec SPEC_SERVER = BUILDER_SERVER.build();
}
