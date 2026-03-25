package com.moigferdsrte.windchimes;

import com.moigferdsrte.windchimes.client.model.ChimeModel;
import com.moigferdsrte.windchimes.client.render.BambooChimeRenderer;
import com.moigferdsrte.windchimes.client.render.CopperChimeRenderer;
import com.moigferdsrte.windchimes.client.render.IronChimeRenderer;
import com.moigferdsrte.windchimes.register.BlocksRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class WindChimesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModelLayerRegistry.registerModelLayer(ChimeModel.LAYER_LOCATION, ChimeModel::createLayerDefinition);
        BlockEntityRenderers.register(BlocksRegistry.BlockEntityTypes.IRON_CHIME_BE, IronChimeRenderer::new);
        BlockEntityRenderers.register(BlocksRegistry.BlockEntityTypes.COPPER_CHIME_BE, CopperChimeRenderer::new);
        BlockEntityRenderers.register(BlocksRegistry.BlockEntityTypes.BAMBOO_CHIME_BE, BambooChimeRenderer.Normal::new);
        BlockEntityRenderers.register(BlocksRegistry.BlockEntityTypes.WALL_BAMBOO_CHIME_BE, BambooChimeRenderer.Wall::new);
    }
}
