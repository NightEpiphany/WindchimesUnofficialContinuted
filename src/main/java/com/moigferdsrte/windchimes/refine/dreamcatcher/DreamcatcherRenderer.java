package com.moigferdsrte.windchimes.refine.dreamcatcher;

import com.moigferdsrte.windchimes.compact.DreamcatcherBlockEntityRenderState;
import com.moigferdsrte.windchimes.compact.DreamcatcherModel;
import com.moigferdsrte.windchimes.refine.Refine;
import com.moigferdsrte.windchimes.refine.RefineClient;
import com.moigferdsrte.windchimes.refine.utils.WoodType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class DreamcatcherRenderer implements BlockEntityRenderer<DreamcatcherBlockEntity, DreamcatcherBlockEntityRenderState> {

    private final DreamcatcherModel root;

    @Environment(EnvType.CLIENT)
    public DreamcatcherRenderer(BlockEntityRendererFactory.Context context) {
        this.root = new DreamcatcherModel(context.getLayerModelPart(RefineClient.DREAMCATCHER));
    }

    @Override
    public void updateRenderState(DreamcatcherBlockEntity entity, DreamcatcherBlockEntityRenderState state, float tickProgress, Vec3d cameraPos, @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay) {
        BlockEntityRenderer.super.updateRenderState(entity, state, tickProgress, cameraPos, crumblingOverlay);
        state.ringTicks = entity.ringingTicks;
        state.strengthDivisor = entity.strengthDivisor;
        state.tickDelta = tickProgress;
        state.hanging = true;
        assert entity.getWorld() != null;
        state.time = entity.getWorld().getTime();
        state.blockState = entity.getCachedState();
    }

    @Override
    public DreamcatcherBlockEntityRenderState createRenderState() {
        return new DreamcatcherBlockEntityRenderState();
    }

    @Override
    public void render(DreamcatcherBlockEntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        DreamcatcherModel.DreamcatcherModelState modelState = new DreamcatcherModel.DreamcatcherModelState(state.ringTicks, state.strengthDivisor, state.time, state.tickDelta, state.hanging, state.direction, state.offSet, state.blockState);
        RenderLayer renderLayer = getBufferType(state.blockState);
        this.root.setAngles(modelState);

        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0F));
        if (state.blockState.get(DreamcatcherBlock.FACING) == Direction.EAST || state.blockState.get(DreamcatcherBlock.FACING) == Direction.WEST) {
            if (state.blockState.get(DreamcatcherBlock.OFFSET)) {
                matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(45.0F));
                matrices.translate(-0.355F, 2.1875F, 0.755F);
            }
            else {
                matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(90.0F));
                matrices.translate(0.155F, 2.1875F, 0.45F);
            }
        }else {
            if (state.blockState.get(DreamcatcherBlock.OFFSET)) {
                matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(-45.0F));
                matrices.translate(-1.125F, 2.1875F, 0.0);
            }else matrices.translate(-0.825F, 2.1875F, 0.45F);
        }
        matrices.translate(0.35f, -1.0f, 0.0);
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

    public static RenderLayer getBufferType(BlockState state) {
        WoodType woodType = state.get(DreamcatcherBlock.WOOD_TYPE);
        return RenderLayer.getEntityCutout(Identifier.of(Refine.MOD_ID, "textures/entity/dreamcatcher/" + woodType.asString() +".png"));
    }
}
