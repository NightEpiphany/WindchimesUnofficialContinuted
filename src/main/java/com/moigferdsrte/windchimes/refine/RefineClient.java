package com.moigferdsrte.windchimes.refine;

import com.moigferdsrte.windchimes.compact.ChimeModel;
import com.moigferdsrte.windchimes.compact.DreamcatcherModel;
import com.moigferdsrte.windchimes.refine.bamboo.BambooChimeRenderer;
import com.moigferdsrte.windchimes.refine.bamboo.BambooWallChimeRenderer;
import com.moigferdsrte.windchimes.refine.copper.CopperChimeRenderer;
import com.moigferdsrte.windchimes.refine.dreamcatcher.DreamcatcherRenderer;
import com.moigferdsrte.windchimes.refine.iron.IronChimeRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class RefineClient implements ClientModInitializer {

    public static final EntityModelLayer CHIME = new EntityModelLayer(Identifier.of("windchimes", "chime"), "main");

    public static final EntityModelLayer DREAMCATCHER = new EntityModelLayer(Identifier.of("windchimes", "dreamcatcher"), "main");

    @Override
    public void onInitializeClient() {

        BlockEntityRendererFactories.register(BlockEntityReg.BAMBOO, BambooChimeRenderer::new);
        BlockEntityRendererFactories.register(BlockEntityReg.WALL_BAMBOO, BambooWallChimeRenderer::new);
        BlockEntityRendererFactories.register(BlockEntityReg.COPPER, CopperChimeRenderer::new);
        BlockEntityRendererFactories.register(BlockEntityReg.IRON, IronChimeRenderer::new);
        BlockEntityRendererFactories.register(BlockEntityReg.DREAM, DreamcatcherRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(CHIME, ChimeModel::getTexturedModelData);

        EntityModelLayerRegistry.registerModelLayer(DREAMCATCHER, DreamcatcherModel::getTexturedModelData);

        FabricLoader.getInstance().getModContainer(Refine.MOD_ID).ifPresent(container -> ResourceManagerHelper.registerBuiltinResourcePack(asId("classicfeather"), container,
                Text.translatable("resourcePack.windchimes.classicFeather"), ResourcePackActivationType.NORMAL));
    }
    public static Identifier asId(String path) {
        return Identifier.of(Refine.MOD_ID, path);
    }
}
