package com.moigferdsrte.windchimes.client.render;

import com.moigferdsrte.windchimes.WindChimes;
import com.moigferdsrte.windchimes.chimes.blockentity.BambooChimeBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class BambooChimeRenderer {
    public static class Normal extends CommonChimeRenderer<BambooChimeBlockEntity.Normal> {
        public Normal(BlockEntityRendererProvider.Context context) {
            super(context);
        }
        @Override
        public Identifier getTextureLocation() {
            return Identifier.fromNamespaceAndPath(WindChimes.MOD_ID, "textures/entity/chime/bamboo_chime.png");
        }
        @Override
        public boolean wall() {
            return false;
        }
    }

    public static class Wall extends CommonChimeRenderer<BambooChimeBlockEntity.Wall> {
        public Wall(BlockEntityRendererProvider.Context context) {
            super(context);
        }
        @Override
        public Identifier getTextureLocation() {
            return Identifier.fromNamespaceAndPath(WindChimes.MOD_ID, "textures/entity/chime/bamboo_chime.png");
        }
        @Override
        public boolean wall() {
            return true;
        }
    }
}
