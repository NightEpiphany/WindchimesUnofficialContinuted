package com.moigferdsrte.windchimes.compact;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.Direction;

@Environment(EnvType.CLIENT)
public class DreamcatcherBlockEntityRenderState extends ChimeBlockEntityRenderState {
    public Direction direction;
    public boolean offSet;
}
