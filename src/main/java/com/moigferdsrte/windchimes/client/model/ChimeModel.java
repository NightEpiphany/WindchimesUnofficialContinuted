package com.moigferdsrte.windchimes.client.model;

import com.moigferdsrte.windchimes.WindChimes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class ChimeModel extends Model<ChimeModel.ChimeModelState> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath(WindChimes.MOD_ID, "chime"), "main");

    private final ModelPart platform;
    private final ModelPart rods1;
    private final ModelPart rods2;
    private final ModelPart clapper;

    public ChimeModel(ModelPart root) {
        super(root, RenderTypes::entityCutout);
        this.platform = root.getChild("hanger");
        this.rods1 = root.getChild("rod1");
        this.rods2 = root.getChild("rod3");
        this.clapper = root.getChild("clapper");
    }

    public static LayerDefinition createLayerDefinition() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        PartDefinition platformPartData = modelPartData.addOrReplaceChild("hanger", CubeListBuilder.create().texOffs(18, 3).addBox(-0.5f, -1f, -0.5f, 1f, 1f, 1f), PartPose.ZERO);
        platformPartData.addOrReplaceChild("platform", CubeListBuilder.create().texOffs(0, 0).addBox( -3f, -2f,   -3f, 6f, 1f, 6f), PartPose.ZERO);

        PartDefinition rods1PartData = modelPartData.addOrReplaceChild("rod1", CubeListBuilder.create().texOffs(0, 7).addBox(-2f, -21f, -2f, 1f, 15f, 1f), PartPose.ZERO);
        rods1PartData.addOrReplaceChild("rod2", CubeListBuilder.create().texOffs(12,7).addBox(1f, -15f, 1f, 1f, 9f, 1f), PartPose.ZERO);

        PartDefinition rods2PartData = modelPartData.addOrReplaceChild("rod3", CubeListBuilder.create().texOffs(8, 7).addBox(1f, -17f, -2f, 1f, 11f, 1f), PartPose.ZERO);
        rods2PartData.addOrReplaceChild("rod4", CubeListBuilder.create().texOffs(4, 7).addBox(-2f, -19f, 1f, 1f, 13f, 1f), PartPose.ZERO);

        modelPartData.addOrReplaceChild("clapper", CubeListBuilder.create().texOffs(18, 0).addBox(-1f, -13f, -1f, 2f, 1f, 2f), PartPose.ZERO);


        return LayerDefinition.create(modelData, 32, 32);
    }

    @Override
    public void setupAnim(ChimeModelState state) {
        super.setupAnim(state);
        float correctedTicks = (float) (state.time % 314.15) + state.tickDelta;
        this.platform.xRot = Mth.sin(correctedTicks * 0.04f) * 0.06f;
        this.platform.zRot = Mth.sin(correctedTicks * 0.06f) * 0.04f;

        float sway = state.ringingTicks + 1f;
        float strength = (state.ringingTicks) / state.strengthDivisor;

        float animationTick = (float)(state.time % 628.3 + state.tickDelta - sway) * 0.1f;
        float animationTick7 = animationTick * 0.75f;
        float animationTick3 = animationTick * 0.3f;

        this.rods1.xRot = Mth.sin(animationTick) * 0.07f * (strength);
        this.rods1.zRot = Mth.cos(animationTick7) * 0.07f * (strength);
        this.rods1.yRot = Mth.cos(animationTick3) * 0.5f * (strength + 1f);
        this.rods2.xRot = Mth.cos(animationTick7) * 0.07f * (strength);
        this.rods2.zRot = Mth.sin(animationTick) * 0.07f * (strength);
        this.rods2.yRot = Mth.sin(animationTick3) * 0.5f * (strength + 1f);
        this.clapper.xRot = this.rods1.xRot + this.rods2.xRot;
        this.clapper.zRot = this.rods1.zRot + this.rods2.zRot;
        this.clapper.yRot = this.rods1.yRot + this.rods2.yRot;
    }

    @Environment(EnvType.CLIENT)
    public record ChimeModelState(float ringingTicks, float strengthDivisor, long time, float tickDelta, boolean hanging) {
    }
}
