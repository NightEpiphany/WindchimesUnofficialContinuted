package com.moigferdsrte.windchimes.refine;

import com.google.common.collect.Sets;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

import java.util.Set;

public class LayerReg {
    private static final Set<EntityModelLayer> LAYERS = Sets.<EntityModelLayer>newHashSet();
    public static final EntityModelLayer BAMBOO = registerMain("bamboo_chime");
    public static final EntityModelLayer COPPER = registerMain("copper_chime");
    public static final EntityModelLayer IRON = registerMain("iron_chime");



    private static EntityModelLayer registerMain(String id) {
        return register(id, "main");
    }

    private static EntityModelLayer register(String id, String layer) {
        EntityModelLayer entityModelLayer = create(id, layer);
        if (!LAYERS.add(entityModelLayer)) {
            throw new IllegalStateException("Duplicate registration for " + entityModelLayer);
        } else {
            return entityModelLayer;
        }
    }

    private static EntityModelLayer create(String id, String layer) {
        return new EntityModelLayer(Identifier.ofVanilla(id), layer);
    }

    public static void init(){}
}
