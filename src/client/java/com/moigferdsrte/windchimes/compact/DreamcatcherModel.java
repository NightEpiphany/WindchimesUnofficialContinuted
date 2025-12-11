package com.moigferdsrte.windchimes.compact;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

public class DreamcatcherModel extends Model<DreamcatcherModel.DreamcatcherModelState> {

    private final ModelPart holder;
    private final ModelPart feather_left;
    private final ModelPart feather_right;
    private final ModelPart feather_middle;

    public DreamcatcherModel(ModelPart root) {
        super(root, RenderLayer::getEntityCutout);
        this.holder = root.getChild("holder");
        this.feather_left = root.getChild("feather_left");
        this.feather_right = root.getChild("feather_right");
        this.feather_middle = root.getChild("feather_middle");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData holderPartData = modelPartData.addChild("holder", ModelPartBuilder.create().uv(0, 4).cuboid(-6.0F, -22.0F, -1.0F, 12.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-6.0F, -32.0F, -1.0F, 12.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(36, 0).cuboid(4.0F, -30.0F, -1.0F, 2.0F, 8.0F, 2.0F, new Dilation(0.0F))
                .uv(28, 0).cuboid(-6.0F, -30.0F, -1.0F, 2.0F, 8.0F, 2.0F, new Dilation(0.0F))
                .uv(44, 2).cuboid(-7.0F, -20.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(44, 2).cuboid(-1.0F, -34.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(1, 13).cuboid(-4.0F, -30.0F, -0.5F, 8.0F, 8.0F, 1.0F, new Dilation(0.0F))
                .uv(44, 2).cuboid(5.0F, -20.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(44, 2).cuboid(-1.0F, -20.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.NONE);

        ModelPartData leftFeatherPartData = modelPartData.addChild("feather_left", ModelPartBuilder.create()
                .uv(0, 47)
                .cuboid(4.0F, -20.0F, 0.0F, 16.0F, 16.0F, 1.0F, new Dilation(0.0F)), ModelTransform.NONE);
        leftFeatherPartData.addChild("cube_r1", ModelPartBuilder.create().uv(1, 32).cuboid(0.0F, -16.0F, -15.0F, 1.0F, 16.0F, 16.0F,
                new Dilation(0.0F)), ModelTransform.of(10.53F, -9.0F, 8.5F, 0.6109F, 0.0F, -0.6109F));

        ModelPartData rightFeatherPartData = modelPartData.addChild("feather_right", ModelPartBuilder.create()
                .uv(0, 28)
                .cuboid(-20.0F, -20.0F, -1.0F, 16.0F, 16.0F, 1.0F, new Dilation(0.0F)), ModelTransform.NONE);
        rightFeatherPartData.addChild("cube_r2", ModelPartBuilder.create().uv(1, 32).cuboid(-1.0F, -16.0F, -15.0F, 1.0F, 16.0F, 16.0F,
                new Dilation(0.0F)), ModelTransform.of(-10.53F, -9.0F, 8.5F, 0.6109F, 0.0F, 0.6109F));

        ModelPartData featherPartData = modelPartData.addChild("feather_middle", ModelPartBuilder.create()
                        .uv(42, 16)
                        .cuboid(-4.0F, -18.0F, 0.0F, 8.0F, 17.0F, 1.0F, new Dilation(0.0F))
                        .uv(43, 9).cuboid(0.0F, -18.0F, -3.0F, 1.0F, 17.0F, 8.0F, new Dilation(0.0F))
                , ModelTransform.NONE);

        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(DreamcatcherModelState state) {
        super.setAngles(state);

        float correctedTicks = (float) ((state.time % 314.15) + state.tickDelta);
        this.holder.pitch = MathHelper.sin(correctedTicks * 0.04f) * 0.06f;
        this.holder.roll = MathHelper.sin(correctedTicks * 0.06f) * 0.04f;

        float sway = state.ringingTicks + 1f;
        float strength = (state.ringingTicks) / state.strengthDivisor / 2f;

        float animationTick = (float)((state.time % 628.3) + state.tickDelta - sway) * 0.1f;
        float animationTick7 = animationTick * 0.75f;
        float animationTick3 = animationTick * 0.3f;

        this.feather_middle.pitch = MathHelper.sin(animationTick) * 0.02f * (strength);
        this.feather_middle.roll = MathHelper.cos(animationTick7) * 0.02f * (strength);
        this.feather_middle.yaw = MathHelper.cos(animationTick3) * 0.32f * (strength + 1f);

        this.feather_left.pitch = MathHelper.sin(animationTick7) * 0.02f * (strength);
        this.feather_left.roll = MathHelper.cos(animationTick) * 0.02f * (strength);
        this.feather_left.yaw = MathHelper.cos(animationTick3) * 0.32f * (strength + 1f);

        this.feather_right.pitch = MathHelper.sin(animationTick7) * 0.02f * (strength);
        this.feather_right.roll = MathHelper.cos(animationTick3) * 0.02f * (strength);
        this.feather_right.yaw = MathHelper.cos(animationTick) * 0.32f * (strength + 1f);
    }

    @Environment(EnvType.CLIENT)
    public record DreamcatcherModelState(float ringingTicks, float strengthDivisor, long time, float tickDelta, boolean hanging, Direction direction, boolean offSet, BlockState blockState) {
    }
}
