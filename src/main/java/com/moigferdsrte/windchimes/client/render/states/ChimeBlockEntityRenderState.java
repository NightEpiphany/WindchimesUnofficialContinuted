package com.moigferdsrte.windchimes.client.render.states;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;

@Environment(EnvType.CLIENT)
public class ChimeBlockEntityRenderState extends BlockEntityRenderState {
    public float ringTicks;
    public float strengthDivisor;
    public long time;
    public float tickDelta;
    public boolean hanging;
}
