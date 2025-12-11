package com.moigferdsrte.windchimes.refine;

import com.moigferdsrte.windchimes.refine.config.WindchimeConfig;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.serialization.Codec;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.rule.GameRule;
import net.minecraft.world.rule.GameRuleCategory;
import net.minecraft.world.rule.GameRuleType;
import net.minecraft.world.rule.GameRuleVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Refine implements ModInitializer {
    public static final String MOD_ID = "windchimes";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static ConfigHolder<WindchimeConfig> configHolder;
    public static WindchimeConfig config;

    public static final GameRule<Integer> EXP_BONUS = Registry.register(Registries.GAME_RULE, Identifier.of(MOD_ID, "exp_given_by_dreamcatcher"),
            new GameRule<>(GameRuleCategory.MISC, GameRuleType.INT, IntegerArgumentType.integer(50, 560), GameRuleVisitor::visitInt, Codec.intRange(50, 560), (value) -> value, 300, FeatureSet.empty()));

    @Override
    public void onInitialize() {
        ItemReg.init();
        BlockReg.init();
        SoundReg.init();
        BlockEntityReg.init();
        AutoConfig.register(WindchimeConfig.class, GsonConfigSerializer::new);
        configHolder = AutoConfig.getConfigHolder(WindchimeConfig.class);
        config = configHolder.getConfig();

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
