package com.moigferdsrte.windchimes.refine;

import com.moigferdsrte.windchimes.refine.bamboo.BambooChimeItem;
import com.moigferdsrte.windchimes.refine.dreamcatcher.DreamcatcherBlock;
import com.moigferdsrte.windchimes.refine.dreamcatcher.DreamcatcherItem;
import com.moigferdsrte.windchimes.refine.utils.WoodType;
import net.minecraft.block.Block;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.function.BiFunction;
import java.util.function.Function;

public class ItemReg {

    public static final Item BAMBOO = register(BlockReg.BAMBOO,
            (block, settings) -> new BambooChimeItem(block, BlockReg.WALL_BAMBOO, Direction.DOWN, settings)
            );


    public static final Item IRON = register(BlockReg.IRON);
    public static final Item COPPER = register(BlockReg.COPPER);
    public static final Item DREAM = register(BlockReg.DREAM, DreamcatcherItem::new, new Item.Settings()
            .maxCount(16)
            .component(DataComponentTypes.BLOCK_STATE, BlockStateComponent.DEFAULT.with(DreamcatcherBlock.WOOD_TYPE, WoodType.OAK)));


    // 1st nest
    public static Item register(Block block) {
        return register(block, BlockItem::new);
    }

    //2nd nest
    public static Item register(Block block, BiFunction<Block, Item.Settings, Item> factory) {
        return register(block, factory, new Item.Settings().maxCount(16));
    }

    //3rd nest
    public static Item register(Block block, BiFunction<Block, Item.Settings, Item> factory, Item.Settings settings) {
        return register(
                keyOf(block.getRegistryEntry().registryKey()), itemSettings -> (Item)factory.apply(block, itemSettings), settings.useBlockPrefixedTranslationKey()
        );
    }

    //4th nest
    public static Item register(RegistryKey<Item> key, Function<Item.Settings, Item> factory, Item.Settings settings) {
        Item item = (Item)factory.apply(settings.registryKey(key));
        if (item instanceof BlockItem blockItem) {
            blockItem.appendBlocks(Item.BLOCK_ITEMS, item);
        }

        return Registry.register(Registries.ITEM, key, item);
    }

    private static RegistryKey<Item> keyOf(RegistryKey<Block> blockKey) {
        return RegistryKey.of(RegistryKeys.ITEM, blockKey.getValue());
    }

    private static RegistryKey<Item> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Refine.MOD_ID, id));
    }

    public static void init(){}
}
