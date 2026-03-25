package com.moigferdsrte.windchimes.client.render;

import com.moigferdsrte.windchimes.WindChimes;
import com.moigferdsrte.windchimes.chimes.blockentity.CopperChimeBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class CopperChimeRenderer extends CommonChimeRenderer<CopperChimeBlockEntity> {
    public CopperChimeRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public Identifier getTextureLocation() {
        return Identifier.fromNamespaceAndPath(WindChimes.MOD_ID, "textures/entity/chime/copper_chime.png");
    }

    @Override
    public boolean wall() {
        return false;
    }
}
