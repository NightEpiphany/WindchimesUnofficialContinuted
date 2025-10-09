package com.moigferdsrte.windchimes.compact;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;

@Environment(EnvType.CLIENT)
public class ChimeBlockEntityRenderState extends BlockEntityRenderState {
    public float ringTicks;
    public float strengthDivisor;
    public long time;
    public float tickDelta;
    public boolean hanging;
}
