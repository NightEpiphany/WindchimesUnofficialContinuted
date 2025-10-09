package com.moigferdsrte.windchimes.refine.iron;

import com.moigferdsrte.windchimes.compact.ChimeBlockEntityRenderState;
import com.moigferdsrte.windchimes.compact.ChimeModel;
import com.moigferdsrte.windchimes.refine.Refine;
import com.moigferdsrte.windchimes.refine.RefineClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.state.CampfireBlockEntityRenderState;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class IronChimeRenderer implements BlockEntityRenderer<IronChimeBlockEntity, ChimeBlockEntityRenderState> {

    private final ChimeModel root;

    public IronChimeRenderer(BlockEntityRendererFactory.Context ctx) {
        this.root = new ChimeModel(ctx.getLayerModelPart(RefineClient.CHIME));
    }

    @Override
    public void updateRenderState(IronChimeBlockEntity entity, ChimeBlockEntityRenderState state, float tickProgress, Vec3d cameraPos, @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay) {
        BlockEntityRenderer.super.updateRenderState(entity, state, tickProgress, cameraPos, crumblingOverlay);
        state.ringTicks = entity.ringingTicks;
        state.strengthDivisor = entity.strengthDivisor;
        state.tickDelta = tickProgress;
        state.hanging = false;
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
        RenderLayer renderLayer = RenderLayer.getEntityCutout(Identifier.of(Refine.MOD_ID, "textures/entity/chime/iron_chime.png"));
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
