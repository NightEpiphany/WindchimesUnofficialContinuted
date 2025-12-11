package com.moigferdsrte.windchimes.compact;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.util.math.MathHelper;

public class ChimeModel extends Model<ChimeModel.ChimeModelState> {

    private final ModelPart platform;
    private final ModelPart rods1;
    private final ModelPart rods2;
    private final ModelPart clapper;

    public ChimeModel(ModelPart root) {
        super(root, RenderLayers::entityCutoutNoCull);
        this.platform = root.getChild("hanger");
        this.rods1 = root.getChild("rod1");
        this.rods2 = root.getChild("rod3");
        this.clapper = root.getChild("clapper");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData platformPartData = modelPartData.addChild("hanger", ModelPartBuilder.create().uv(18, 3).cuboid(-0.5f, -1f, -0.5f, 1f, 1f, 1f), ModelTransform.NONE);
        platformPartData.addChild("platform", ModelPartBuilder.create().uv(0, 0).cuboid( -3f, -2f,   -3f, 6f, 1f, 6f), ModelTransform.NONE);

        ModelPartData rods1PartData = modelPartData.addChild("rod1", ModelPartBuilder.create().uv(0, 7).cuboid(-2f, -21f, -2f, 1f, 15f, 1f), ModelTransform.NONE);
        rods1PartData.addChild("rod2", ModelPartBuilder.create().uv(12,7).cuboid(1f, -15f, 1f, 1f, 9f, 1f), ModelTransform.NONE);

        ModelPartData rods2PartData = modelPartData.addChild("rod3", ModelPartBuilder.create().uv(8, 7).cuboid(1f, -17f, -2f, 1f, 11f, 1f), ModelTransform.NONE);
        rods2PartData.addChild("rod4", ModelPartBuilder.create().uv(4, 7).cuboid(-2f, -19f, 1f, 1f, 13f, 1f), ModelTransform.NONE);

        modelPartData.addChild("clapper", ModelPartBuilder.create().uv(18, 0).cuboid(-1f, -13f, -1f, 2f, 1f, 2f), ModelTransform.NONE);


        return TexturedModelData.of(modelData, 32, 32);
    }

    @Override
    public void setAngles(ChimeModelState state) {
        super.setAngles(state);
        float correctedTicks = (float) (state.time % 314.15) + state.tickDelta;
        this.platform.pitch = MathHelper.sin(correctedTicks * 0.04f) * 0.06f;
        this.platform.roll = MathHelper.sin(correctedTicks * 0.06f) * 0.04f;

        float sway = state.ringingTicks + 1f;
        float strength = (state.ringingTicks) / state.strengthDivisor;

        float animationTick = (float)(state.time % 628.3 + state.tickDelta - sway) * 0.1f;
        float animationTick7 = animationTick * 0.75f;
        float animationTick3 = animationTick * 0.3f;

        this.rods1.pitch = MathHelper.sin(animationTick) * 0.07f * (strength);
        this.rods1.roll = MathHelper.cos(animationTick7) * 0.07f * (strength);
        this.rods1.yaw = MathHelper.cos(animationTick3) * 0.5f * (strength + 1f);
        this.rods2.pitch = MathHelper.cos(animationTick7) * 0.07f * (strength);
        this.rods2.roll = MathHelper.sin(animationTick) * 0.07f * (strength);
        this.rods2.yaw = MathHelper.sin(animationTick3) * 0.5f * (strength + 1f);
        this.clapper.pitch = this.rods1.pitch + this.rods2.pitch;
        this.clapper.roll = this.rods1.roll + this.rods2.roll;
        this.clapper.yaw = this.rods1.yaw + this.rods2.yaw;
    }

    @Environment(EnvType.CLIENT)
    public record ChimeModelState(float ringingTicks, float strengthDivisor, long time, float tickDelta, boolean hanging) {
    }
}
