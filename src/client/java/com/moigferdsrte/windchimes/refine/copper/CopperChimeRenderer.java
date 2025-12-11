package com.moigferdsrte.windchimes.refine.copper;

import com.moigferdsrte.windchimes.compact.ChimeBlockEntityRenderState;
import com.moigferdsrte.windchimes.compact.ChimeModel;
import com.moigferdsrte.windchimes.refine.Refine;
import com.moigferdsrte.windchimes.refine.RefineClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class CopperChimeRenderer implements BlockEntityRenderer<CopperChimeBlockEntity, ChimeBlockEntityRenderState> {

    private final ChimeModel root;

    public CopperChimeRenderer(BlockEntityRendererFactory.Context ctx) {
        this.root = new ChimeModel(ctx.getLayerModelPart(RefineClient.CHIME));
    }

    @Override
    public void updateRenderState(CopperChimeBlockEntity entity, ChimeBlockEntityRenderState state, float tickDelta, Vec3d cameraPos, @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay) {
        BlockEntityRenderer.super.updateRenderState(entity, state, tickDelta, cameraPos, crumblingOverlay);
        state.ringTicks = entity.ringingTicks;
        state.strengthDivisor = entity.strengthDivisor;
        state.tickDelta = tickDelta;
        state.hanging = true;
        assert entity.getWorld() != null;
        state.time = entity.getWorld().getTime();
    }

    @Override
    public ChimeBlockEntityRenderState createRenderState() {
        return new ChimeBlockEntityRenderState();
    }

    @Override
    public void render(ChimeBlockEntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        ChimeModel.ChimeModelState modelState = new ChimeModel.ChimeModelState(state.ringTicks, state.strengthDivisor, state.time, state.tickDelta, state.hanging);
        RenderLayer renderLayer = RenderLayers.entityCutoutNoCull(Identifier.of(Refine.MOD_ID, "textures/entity/chime/copper_chime.png"));
        this.root.setAngles(modelState);
        matrices.translate(0.5f, 1f, 0.5f);
        queue.submitModel(
                this.root,
                modelState,
                matrices,
                renderLayer,
                state.lightmapCoordinates,
                OverlayTexture.DEFAULT_UV,
                -1,
                null,
                0,
                state.crumblingOverlay
        );
    }
}
