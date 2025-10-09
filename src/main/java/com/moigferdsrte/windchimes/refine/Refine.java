package com.moigferdsrte.windchimes.refine;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Refine implements ModInitializer {
    public static final String MOD_ID = "windchimes";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final GameRules.Key<GameRules.IntRule> EXP_BONUS = GameRuleRegistry.register("expGivenByDreamcatcher",
            GameRules.Category.MISC, GameRuleFactory.createIntRule(560, 50)
    );

    @Override
    public void onInitialize() {
        ItemReg.init();
        BlockReg.init();
        SoundReg.init();
        LayerReg.init();
        BlockEntityReg.init();


        Registry.register(Registries.ITEM_GROUP, Identifier.of("windchimes","chime"),
                net.minecraft.item.ItemGroup.create(null,-1).displayName(Text.translatable("itemGroup.windchimes.item_tab"))
                        .icon(()->new ItemStack(Items.BELL))
                        .entries(((displayContext, entries) -> {
                            entries.add(ItemReg.BAMBOO);
                            entries.add(ItemReg.IRON);
                            entries.add(ItemReg.COPPER);
                            entries.add(ItemReg.DREAM);
                        })).build());

        LOGGER.info("Hello Fabric world!");
    }
}
