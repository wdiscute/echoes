package com.wdiscute.echoes.registry;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.wdiscute.echoes.Echoes;
import net.minecraft.client.renderer.RenderPipelines;

public interface ECRenderPipelines
{
    RenderPipeline.Snippet PORTAL_SNIPPET =
            RenderPipeline.builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET, RenderPipelines.FOG_SNIPPET, RenderPipelines.GLOBALS_SNIPPET)
                    .withVertexShader(Echoes.rl("core/rendertype_end_portal"))
                    .withFragmentShader(Echoes.rl("core/rendertype_end_portal"))
                    .withSampler("Sampler0")
                    .withSampler("Sampler1")
                    .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
                    .withVertexFormat(DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS)
                    .withDepthStencilState(DepthStencilState.DEFAULT)
                    .buildSnippet();


    RenderPipeline PORTAL =
            RenderPipeline.builder(PORTAL_SNIPPET)
                    .withLocation(Echoes.rl("pipeline/end_portal"))
                    .withShaderDefine("PORTAL_LAYERS", 15)
                    .build();


}
