package com.moigferdsrte.windchimes.client.render;

import com.moigferdsrte.windchimes.chimes.blockentity.abstracts.CommonChimeBlockEntity;
import com.moigferdsrte.windchimes.client.model.ChimeModel;
import com.moigferdsrte.windchimes.client.render.states.ChimeBlockEntityRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public abstract class CommonChimeRenderer <C extends CommonChimeBlockEntity> implements BlockEntityRenderer<C, ChimeBlockEntityRenderState> {
    protected final ChimeModel root;

    protected CommonChimeRenderer(BlockEntityRendererProvider.Context context) {
        this.root = new ChimeModel(context.bakeLayer(ChimeModel.LAYER_LOCATION));
    }

    public abstract Identifier getTextureLocation();

    public abstract boolean wall();

    @Override
    public ChimeBlockEntityRenderState createRenderState() {
        return new ChimeBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(C entity, ChimeBlockEntityRenderState state, float partialTicks, @NonNull Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(entity, state, partialTicks, cameraPosition, breakProgress);
        state.ringTicks = entity.ringingTicks;
        state.strengthDivisor = entity.strengthDivisor;
        state.tickDelta = partialTicks;
        state.hanging = false;
       if (entity.getLevel() != null)
            state.time = entity.getLevel().getGameTime();
    }

    @Override
    public void submit(ChimeBlockEntityRenderState state, @NonNull PoseStack poseStack, @NonNull SubmitNodeCollector submitNodeCollector, @NonNull CameraRenderState camera) {
        ChimeModel.ChimeModelState modelState = new ChimeModel.ChimeModelState(state.ringTicks, state.strengthDivisor, state.time, state.tickDelta, state.hanging);
        RenderType renderLayer = RenderTypes.entityCutout(getTextureLocation());
        this.root.setupAnim(modelState);
        poseStack.translate(0.5f, this.wall() ? 0 : 1.0f, 0.5f);
        submitNodeCollector.submitModel(
                this.root,
                modelState,
                poseStack,
                renderLayer,
                state.lightCoords,
                OverlayTexture.NO_OVERLAY,
                -1,
                null,
                0,
                null
        );
    }
}
